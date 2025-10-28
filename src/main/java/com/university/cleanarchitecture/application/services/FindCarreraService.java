package com.university.cleanarchitecture.application.services;

import com.university.cleanarchitecture.application.dto.query.FindCarreraByIdQuery;
import com.university.cleanarchitecture.application.dto.query.FindCarrerasByDuracionQuery;
import com.university.cleanarchitecture.application.dto.query.FindCarrerasByFacultadQuery;
import com.university.cleanarchitecture.application.dto.response.CarreraResponse;
import com.university.cleanarchitecture.application.dto.response.CarreraSummaryResponse;
import com.university.cleanarchitecture.application.mapper.CarreraMapper;
import com.university.cleanarchitecture.application.port.in.carrera.FindCarreraUseCase;
import com.university.cleanarchitecture.application.port.out.CarreraRepositoryPort;
import com.university.cleanarchitecture.application.port.out.FacultadRepositoryPort;
import com.university.cleanarchitecture.domain.exception.CarreraNotFoundException;
import com.university.cleanarchitecture.domain.model.Carrera;
import com.university.cleanarchitecture.domain.model.Facultad;
import com.university.cleanarchitecture.domain.model.valueobjects.CarreraId;
import com.university.cleanarchitecture.domain.model.valueobjects.Duracion;
import com.university.cleanarchitecture.domain.model.valueobjects.FacultadId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementación del caso de uso: Consultas de Carreras.
 *
 * Implementa todas las operaciones de lectura (Queries) según patrón CQRS.
 */
@Service
@Transactional(readOnly = true)
public class FindCarreraService implements FindCarreraUseCase {

    private final CarreraRepositoryPort carreraRepository;
    private final FacultadRepositoryPort facultadRepository;

    public FindCarreraService(
            CarreraRepositoryPort carreraRepository,
            FacultadRepositoryPort facultadRepository) {
        this.carreraRepository = carreraRepository;
        this.facultadRepository = facultadRepository;
    }

    @Override
    public CarreraResponse findById(FindCarreraByIdQuery query) {
        // 1. Validar query
        if (query == null) {
            throw new IllegalArgumentException("El query no puede ser nulo");
        }

        // 2. Buscar carrera por ID
        CarreraId carreraId = CarreraId.of(query.carreraId());
        Carrera carrera = carreraRepository.findById(carreraId)
                .orElseThrow(() -> new CarreraNotFoundException(query.carreraId()));

        // 3. Obtener nombre de facultad
        Facultad facultad = facultadRepository.findById(carrera.getFacultadId())
                .orElseThrow(); // No debería fallar si la integridad referencial es correcta
        String facultadNombre = facultad.getNombre().getValue();

        // 4. Convertir a Response
        return CarreraMapper.toResponse(carrera, facultadNombre);
    }

    @Override
    public List<CarreraSummaryResponse> findByFacultad(FindCarrerasByFacultadQuery query) {
        // 1. Validar query
        if (query == null) {
            throw new IllegalArgumentException("El query no puede ser nulo");
        }

        // 2. Obtener carreras según filtro
        FacultadId facultadId = FacultadId.of(query.facultadId());
        List<Carrera> carreras = query.soloActivas()
                ? carreraRepository.findActiveByFacultadId(facultadId)
                : carreraRepository.findByFacultadId(facultadId);

        // 3. Obtener nombre de facultad (una sola vez)
        Facultad facultad = facultadRepository.findById(facultadId)
                .orElseThrow(); // No debería fallar
        String facultadNombre = facultad.getNombre().getValue();

        // 4. Convertir cada carrera a SummaryResponse
        return carreras.stream()
                .map(carrera -> CarreraMapper.toSummaryResponse(carrera, facultadNombre))
                .collect(Collectors.toList());
    }

    @Override
    public List<CarreraSummaryResponse> findByDuracion(FindCarrerasByDuracionQuery query) {
        // 1. Validar query
        if (query == null) {
            throw new IllegalArgumentException("El query no puede ser nulo");
        }

        // 2. Obtener carreras según tipo de búsqueda
        List<Carrera> carreras;

        if (query.isBusquedaExacta()) {
            // Búsqueda por duración exacta
            Duracion duracion = Duracion.of(query.duracionSemestres());
            carreras = carreraRepository.findByDuracion(duracion);
        } else if (query.isBusquedaPorRango()) {
            // Búsqueda por rango de duración
            int min = query.duracionMinima() != null ? query.duracionMinima() : 6;
            int max = query.duracionMaxima() != null ? query.duracionMaxima() : 14;
            carreras = carreraRepository.findByDuracionBetween(min, max);
        } else {
            // Sin criterio de búsqueda: retornar todas las activas
            carreras = carreraRepository.findAllActive();
        }

        // 3. Filtrar por activas si se solicita
        if (query.soloActivas()) {
            carreras = carreras.stream()
                    .filter(Carrera::isActivo)
                    .collect(Collectors.toList());
        }

        // 4. Convertir a SummaryResponse (con nombre de facultad)
        return carreras.stream()
                .map(carrera -> {
                    // Obtener nombre de facultad para cada carrera
                    Facultad facultad = facultadRepository.findById(carrera.getFacultadId())
                            .orElseThrow();
                    return CarreraMapper.toSummaryResponse(carrera, facultad.getNombre().getValue());
                })
                .collect(Collectors.toList());
    }
}

package com.university.cleanarchitecture.application.services;

import com.university.cleanarchitecture.application.dto.query.FindAllFacultadesQuery;
import com.university.cleanarchitecture.application.dto.query.FindFacultadByIdQuery;
import com.university.cleanarchitecture.application.dto.query.FindFacultadByNombreQuery;
import com.university.cleanarchitecture.application.dto.response.FacultadResponse;
import com.university.cleanarchitecture.application.dto.response.FacultadSummaryResponse;
import com.university.cleanarchitecture.application.mapper.FacultadMapper;
import com.university.cleanarchitecture.application.port.in.facultad.FindFacultadUseCase;
import com.university.cleanarchitecture.application.port.out.CarreraRepositoryPort;
import com.university.cleanarchitecture.application.port.out.FacultadRepositoryPort;
import com.university.cleanarchitecture.domain.exception.FacultadNotFoundException;
import com.university.cleanarchitecture.domain.model.Facultad;
import com.university.cleanarchitecture.domain.model.valueobjects.FacultadId;
import com.university.cleanarchitecture.domain.model.valueobjects.NombreAcademico;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementación del caso de uso: Consultas de Facultades.
 *
 * Implementa todas las operaciones de lectura (Queries) según patrón CQRS.
 *
 * @Transactional(readOnly = true): Optimización para operaciones de solo lectura
 */
@Service
@Transactional(readOnly = true)
public class FindFacultadService implements FindFacultadUseCase {

    private final FacultadRepositoryPort facultadRepository;
    private final CarreraRepositoryPort carreraRepository;

    public FindFacultadService(
            FacultadRepositoryPort facultadRepository,
            CarreraRepositoryPort carreraRepository) {
        this.facultadRepository = facultadRepository;
        this.carreraRepository = carreraRepository;
    }

    @Override
    public FacultadResponse findById(FindFacultadByIdQuery query) {
        // 1. Validar query
        if (query == null) {
            throw new IllegalArgumentException("El query no puede ser nulo");
        }

        // 2. Buscar facultad por ID
        FacultadId facultadId = FacultadId.of(query.facultadId());
        Facultad facultad = facultadRepository.findById(facultadId)
                .orElseThrow(() -> new FacultadNotFoundException(query.facultadId()));

        // 3. Contar carreras asociadas
        long carrerasCount = carreraRepository.countActiveByFacultadId(facultadId);

        // 4. Convertir a Response
        return FacultadMapper.toResponse(facultad, (int) carrerasCount);
    }

    @Override
    public FacultadResponse findByNombre(FindFacultadByNombreQuery query) {
        // 1. Validar query
        if (query == null) {
            throw new IllegalArgumentException("El query no puede ser nulo");
        }

        // 2. Buscar por nombre
        NombreAcademico nombre = NombreAcademico.of(query.nombre());
        Facultad facultad = facultadRepository.findByNombre(nombre)
                .orElseThrow(() -> new FacultadNotFoundException(
                        "No se encontró facultad con nombre: " + nombre.getValue()
                ));

        // 3. Contar carreras
        long carrerasCount = carreraRepository.countActiveByFacultadId(facultad.getId());

        // 4. Convertir a Response
        return FacultadMapper.toResponse(facultad, (int) carrerasCount);
    }

    @Override
    public List<FacultadSummaryResponse> findAll(FindAllFacultadesQuery query) {
        // 1. Validar query
        if (query == null) {
            throw new IllegalArgumentException("El query no puede ser nulo");
        }

        // 2. Obtener facultades según filtro
        List<Facultad> facultades = query.incluirInactivas()
                ? facultadRepository.findAll()
                : facultadRepository.findAllActive();

        // 3. Convertir cada facultad a SummaryResponse
        return facultades.stream()
                .map(facultad -> {
                    // Contar carreras para cada facultad
                    long carrerasCount = carreraRepository.countActiveByFacultadId(facultad.getId());
                    return FacultadMapper.toSummaryResponse(facultad, (int) carrerasCount);
                })
                .collect(Collectors.toList());
    }
}

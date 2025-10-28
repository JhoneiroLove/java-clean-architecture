package com.university.cleanarchitecture.application.services;

import com.university.cleanarchitecture.application.dto.command.RegisterFacultadCommand;
import com.university.cleanarchitecture.application.dto.response.FacultadResponse;
import com.university.cleanarchitecture.application.mapper.FacultadMapper;
import com.university.cleanarchitecture.application.port.in.facultad.RegisterFacultadUseCase;
import com.university.cleanarchitecture.application.port.out.CarreraRepositoryPort;
import com.university.cleanarchitecture.application.port.out.FacultadRepositoryPort;
import com.university.cleanarchitecture.domain.model.Facultad;
import com.university.cleanarchitecture.domain.model.valueobjects.FacultadId;
import com.university.cleanarchitecture.domain.model.valueobjects.NombreAcademico;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Implementación del caso de uso: Registrar Facultad.
 *
 * Responsabilidades:
 * - Orquestar la creación de una facultad
 * - Validar reglas de negocio
 * - Coordinar entre mappers, repositorios y dominio
 *
 * Anotaciones:
 * - @Service: Marca como componente de servicio de Spring
 * - @Transactional: Garantiza atomicidad de la operación
 */
@Service
@Transactional
public class RegisterFacultadService implements RegisterFacultadUseCase {

    private final FacultadRepositoryPort facultadRepository;
    private final CarreraRepositoryPort carreraRepository;

    // Simulador de generación de IDs (en producción vendría de la BD)
    private static final AtomicLong ID_GENERATOR = new AtomicLong(1);

    public RegisterFacultadService(
            FacultadRepositoryPort facultadRepository,
            CarreraRepositoryPort carreraRepository) {
        this.facultadRepository = facultadRepository;
        this.carreraRepository = carreraRepository;
    }

    @Override
    public FacultadResponse register(RegisterFacultadCommand command) {
        // 1. Validar comando (Bean Validation ya lo hizo, pero verificamos)
        if (command == null) {
            throw new IllegalArgumentException("El comando no puede ser nulo");
        }

        // 2. Convertir nombre a Value Object para validación
        NombreAcademico nombre = NombreAcademico.of(command.nombre());

        // 3. Verificar que no exista una facultad con el mismo nombre
        if (facultadRepository.existsByNombre(nombre)) {
            throw new IllegalArgumentException(
                    "Ya existe una facultad con el nombre: " + nombre.getValue()
            );
        }

        // 4. Generar nuevo ID (en producción, esto lo hace la BD)
        FacultadId facultadId = FacultadId.of(ID_GENERATOR.getAndIncrement());

        // 5. Convertir Command → Domain Entity usando Mapper
        Facultad facultad = FacultadMapper.toDomain(command, facultadId);

        // 6. Persistir en el repositorio
        Facultad savedFacultad = facultadRepository.save(facultad);

        // 7. Contar carreras asociadas (nueva facultad = 0 carreras)
        long carrerasCount = carreraRepository.countActiveByFacultadId(savedFacultad.getId());

        // 8. Convertir Domain → Response usando Mapper
        return FacultadMapper.toResponse(savedFacultad, (int) carrerasCount);
    }
}




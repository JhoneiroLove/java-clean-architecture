package com.university.cleanarchitecture.application.services;

import com.university.cleanarchitecture.application.dto.command.UpdateFacultadCommand;
import com.university.cleanarchitecture.application.dto.response.FacultadResponse;
import com.university.cleanarchitecture.application.mapper.FacultadMapper;
import com.university.cleanarchitecture.application.port.in.facultad.UpdateFacultadUseCase;
import com.university.cleanarchitecture.application.port.out.CarreraRepositoryPort;
import com.university.cleanarchitecture.application.port.out.FacultadRepositoryPort;
import com.university.cleanarchitecture.domain.exception.FacultadNotFoundException;
import com.university.cleanarchitecture.domain.model.Facultad;
import com.university.cleanarchitecture.domain.model.valueobjects.FacultadId;
import com.university.cleanarchitecture.domain.model.valueobjects.NombreAcademico;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementación del caso de uso: Actualizar Facultad.
 *
 * Responsabilidades:
 * - Orquestar la actualización de una facultad existente
 * - Validar que no exista duplicidad de nombres
 * - Aplicar cambios y persistir
 */
@Service
@Transactional
public class UpdateFacultadService implements UpdateFacultadUseCase {

    private final FacultadRepositoryPort facultadRepository;
    private final CarreraRepositoryPort carreraRepository;

    public UpdateFacultadService(
            FacultadRepositoryPort facultadRepository,
            CarreraRepositoryPort carreraRepository) {
        this.facultadRepository = facultadRepository;
        this.carreraRepository = carreraRepository;
    }

    @Override
    public FacultadResponse update(UpdateFacultadCommand command) {
        // 1. Validar comando
        if (command == null) {
            throw new IllegalArgumentException("El comando no puede ser nulo");
        }

        // 2. Buscar la facultad existente
        FacultadId facultadId = FacultadId.of(command.facultadId());
        Facultad facultad = facultadRepository.findById(facultadId)
                .orElseThrow(() -> new FacultadNotFoundException(command.facultadId()));

        // 3. Validar que el nuevo nombre no exista en otra facultad
        NombreAcademico nuevoNombre = NombreAcademico.of(command.nombre());
        if (facultadRepository.existsByNombreAndIdNot(nuevoNombre, facultadId)) {
            throw new IllegalArgumentException(
                    "Ya existe otra facultad con el nombre: " + nuevoNombre.getValue()
            );
        }

        // 4. Aplicar cambios usando el Mapper
        // El mapper delega al método de dominio que contiene las validaciones
        FacultadMapper.applyUpdateCommand(command, facultad);

        // 5. Persistir los cambios
        Facultad updatedFacultad = facultadRepository.save(facultad);

        // 6. Contar carreras asociadas
        long carrerasCount = carreraRepository.countActiveByFacultadId(updatedFacultad.getId());

        // 7. Convertir a Response
        return FacultadMapper.toResponse(updatedFacultad, (int) carrerasCount);
    }
}

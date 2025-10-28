package com.university.cleanarchitecture.application.services;

import com.university.cleanarchitecture.application.dto.response.OperationResponse;
import com.university.cleanarchitecture.application.port.in.facultad.ManageFacultadStatusUseCase;
import com.university.cleanarchitecture.application.port.out.FacultadRepositoryPort;
import com.university.cleanarchitecture.domain.exception.FacultadNotFoundException;
import com.university.cleanarchitecture.domain.model.Facultad;
import com.university.cleanarchitecture.domain.model.valueobjects.FacultadId;
import com.university.cleanarchitecture.domain.service.CarreraDomainService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementación del caso de uso: Gestionar Estado de Facultad.
 *
 * Responsabilidades:
 * - Activar/Desactivar facultades
 * - Eliminar facultades
 * - Validar reglas de negocio con Domain Service
 */
@Service
@Transactional
public class ManageFacultadStatusService implements ManageFacultadStatusUseCase {

    private final FacultadRepositoryPort facultadRepository;
    private final CarreraDomainService carreraDomainService;

    public ManageFacultadStatusService(
            FacultadRepositoryPort facultadRepository,
            CarreraDomainService carreraDomainService) {
        this.facultadRepository = facultadRepository;
        this.carreraDomainService = carreraDomainService;
    }

    @Override
    public OperationResponse deactivate(Long facultadId) {
        // 1. Buscar facultad
        FacultadId id = FacultadId.of(facultadId);
        Facultad facultad = facultadRepository.findById(id)
                .orElseThrow(() -> new FacultadNotFoundException(facultadId));

        // 2. Validar que pueda ser desactivada (usar Domain Service)
        if (!carreraDomainService.puedeDesactivarFacultad(id)) {
            throw new IllegalStateException(
                    "No se puede desactivar la facultad porque tiene carreras activas"
            );
        }

        // 3. Desactivar (método de dominio con validaciones)
        facultad.desactivar();

        // 4. Persistir cambios
        facultadRepository.save(facultad);

        // 5. Retornar respuesta
        return OperationResponse.success(
                "Facultad desactivada exitosamente: " + facultad.getNombre().getValue(),
                facultadId
        );
    }

    @Override
    public OperationResponse activate(Long facultadId) {
        // 1. Buscar facultad
        FacultadId id = FacultadId.of(facultadId);
        Facultad facultad = facultadRepository.findById(id)
                .orElseThrow(() -> new FacultadNotFoundException(facultadId));

        // 2. Activar (método de dominio con validaciones)
        facultad.activar();

        // 3. Persistir cambios
        facultadRepository.save(facultad);

        // 4. Retornar respuesta
        return OperationResponse.success(
                "Facultad activada exitosamente: " + facultad.getNombre().getValue(),
                facultadId
        );
    }

    @Override
    public OperationResponse delete(Long facultadId) {
        // 1. Buscar facultad
        FacultadId id = FacultadId.of(facultadId);
        Facultad facultad = facultadRepository.findById(id)
                .orElseThrow(() -> new FacultadNotFoundException(facultadId));

        // 2. Validar que pueda ser eliminada (usar Domain Service)
        if (!carreraDomainService.puedeEliminarFacultad(id)) {
            throw new IllegalStateException(
                    "No se puede eliminar la facultad porque tiene carreras asociadas"
            );
        }

        // 3. Validar que esté inactiva
        if (facultad.isActivo()) {
            throw new IllegalStateException(
                    "No se puede eliminar una facultad activa. Primero desactívela."
            );
        }

        // 4. Eliminar del repositorio
        String nombreFacultad = facultad.getNombre().getValue();
        facultadRepository.deleteById(id);

        // 5. Retornar respuesta
        return OperationResponse.success(
                "Facultad eliminada exitosamente: " + nombreFacultad,
                facultadId
        );
    }
}

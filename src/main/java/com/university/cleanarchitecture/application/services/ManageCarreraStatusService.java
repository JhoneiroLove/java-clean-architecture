package com.university.cleanarchitecture.application.services;

import com.university.cleanarchitecture.application.dto.response.OperationResponse;
import com.university.cleanarchitecture.application.port.in.carrera.ManageCarreraStatusUseCase;
import com.university.cleanarchitecture.application.port.out.CarreraRepositoryPort;
import com.university.cleanarchitecture.domain.exception.CarreraNotFoundException;
import com.university.cleanarchitecture.domain.model.Carrera;
import com.university.cleanarchitecture.domain.model.valueobjects.CarreraId;
import com.university.cleanarchitecture.domain.service.CarreraDomainService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementación del caso de uso: Gestionar Estado de Carrera.
 *
 * Responsabilidades:
 * - Activar/Desactivar carreras
 * - Eliminar carreras
 * - Validar reglas de negocio con Domain Service
 */
@Service
@Transactional
public class ManageCarreraStatusService implements ManageCarreraStatusUseCase {

    private final CarreraRepositoryPort carreraRepository;
    private final CarreraDomainService domainService;

    public ManageCarreraStatusService(
            CarreraRepositoryPort carreraRepository,
            CarreraDomainService domainService) {
        this.carreraRepository = carreraRepository;
        this.domainService = domainService;
    }

    @Override
    public OperationResponse deactivate(Long carreraId) {
        // 1. Buscar carrera
        CarreraId id = CarreraId.of(carreraId);
        Carrera carrera = carreraRepository.findById(id)
                .orElseThrow(() -> new CarreraNotFoundException(carreraId));

        // 2. Desactivar (método de dominio con validaciones)
        carrera.desactivar();

        // 3. Persistir cambios
        carreraRepository.save(carrera);

        // 4. Retornar respuesta
        return OperationResponse.success(
                "Carrera desactivada exitosamente: " + carrera.getNombre().getValue(),
                carreraId
        );
    }

    @Override
    public OperationResponse activate(Long carreraId) {
        // 1. Buscar carrera
        CarreraId id = CarreraId.of(carreraId);
        Carrera carrera = carreraRepository.findById(id)
                .orElseThrow(() -> new CarreraNotFoundException(carreraId));

        // 2. Activar (método de dominio con validaciones)
        carrera.activar();

        // 3. Persistir cambios
        carreraRepository.save(carrera);

        // 4. Retornar respuesta
        return OperationResponse.success(
                "Carrera activada exitosamente: " + carrera.getNombre().getValue(),
                carreraId
        );
    }

    @Override
    public OperationResponse delete(Long carreraId) {
        // 1. Buscar carrera
        CarreraId id = CarreraId.of(carreraId);
        Carrera carrera = carreraRepository.findById(id)
                .orElseThrow(() -> new CarreraNotFoundException(carreraId));

        // 2. Validar que pueda ser eliminada (usar Domain Service)
        if (!domainService.puedeEliminarCarrera(carrera)) {
            throw new IllegalStateException(
                    "No se puede eliminar una carrera activa. Primero desactívela."
            );
        }

        // 3. Eliminar del repositorio
        String nombreCarrera = carrera.getNombre().getValue();
        carreraRepository.deleteById(id);

        // 4. Retornar respuesta
        return OperationResponse.success(
                "Carrera eliminada exitosamente: " + nombreCarrera,
                carreraId
        );
    }
}

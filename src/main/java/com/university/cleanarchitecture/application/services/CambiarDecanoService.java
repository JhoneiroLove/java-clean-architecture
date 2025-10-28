package com.university.cleanarchitecture.application.services;

import com.university.cleanarchitecture.application.dto.command.CambiarDecanoCommand;
import com.university.cleanarchitecture.application.dto.response.OperationResponse;
import com.university.cleanarchitecture.application.port.in.facultad.CambiarDecanoUseCase;
import com.university.cleanarchitecture.application.port.out.FacultadRepositoryPort;
import com.university.cleanarchitecture.domain.exception.FacultadNotFoundException;
import com.university.cleanarchitecture.domain.model.Facultad;
import com.university.cleanarchitecture.domain.model.valueobjects.FacultadId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementación del caso de uso: Cambiar Decano de Facultad.
 *
 * Operación específica que solo modifica el decano sin afectar otros datos.
 */
@Service
@Transactional
public class CambiarDecanoService implements CambiarDecanoUseCase {

    private final FacultadRepositoryPort facultadRepository;

    public CambiarDecanoService(FacultadRepositoryPort facultadRepository) {
        this.facultadRepository = facultadRepository;
    }

    @Override
    public OperationResponse cambiarDecano(CambiarDecanoCommand command) {
        // 1. Validar comando
        if (command == null) {
            throw new IllegalArgumentException("El comando no puede ser nulo");
        }

        // 2. Buscar la facultad
        FacultadId facultadId = FacultadId.of(command.facultadId());
        Facultad facultad = facultadRepository.findById(facultadId)
                .orElseThrow(() -> new FacultadNotFoundException(command.facultadId()));

        // 3. Obtener el decano anterior (para el mensaje de respuesta)
        String decanoAnterior = facultad.getDecano();

        // 4. Cambiar decano (método de dominio con validaciones)
        facultad.cambiarDecano(command.nuevoDecano());

        // 5. Persistir los cambios
        facultadRepository.save(facultad);

        // 6. Retornar respuesta de operación exitosa
        String mensaje = String.format(
                "Decano cambiado exitosamente en '%s'. Anterior: '%s', Nuevo: '%s'",
                facultad.getNombre().getValue(),
                decanoAnterior,
                command.nuevoDecano()
        );

        return OperationResponse.success(mensaje, facultadId.getValue());
    }
}

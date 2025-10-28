package com.university.cleanarchitecture.application.port.in.facultad;

import com.university.cleanarchitecture.application.dto.command.CambiarDecanoCommand;
import com.university.cleanarchitecture.application.dto.response.OperationResponse;

/**
 * Puerto de entrada (Input Port) para cambiar el decano de una Facultad.
 *
 * Operación específica que solo modifica el decano sin afectar otros datos.
 */
public interface CambiarDecanoUseCase {

    /**
     * Cambia el decano de una facultad.
     *
     * @param command datos del cambio (facultadId y nuevoDecano)
     * @return OperationResponse indicando el éxito de la operación
     * @throws com.university.cleanarchitecture.domain.exception.FacultadNotFoundException
     *         si no existe la facultad
     */
    OperationResponse cambiarDecano(CambiarDecanoCommand command);
}

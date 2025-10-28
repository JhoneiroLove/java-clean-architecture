package com.university.cleanarchitecture.application.port.in.carrera;

import com.university.cleanarchitecture.application.dto.command.UpdateCarreraCommand;
import com.university.cleanarchitecture.application.dto.response.CarreraResponse;

/**
 * Puerto de entrada (Input Port) para actualizar una Carrera existente.
 *
 * Define el contrato para el caso de uso de actualización de carreras.
 */
public interface UpdateCarreraUseCase {

    /**
     * Actualiza los datos de una carrera existente.
     *
     * Proceso:
     * 1. Busca la carrera por ID
     * 2. Valida que no exista otra carrera con el mismo nombre
     * 3. Aplica los cambios a la entidad
     * 4. Persiste los cambios
     * 5. Retorna la respuesta actualizada
     *
     * @param command datos de actualización
     * @return CarreraResponse con la información actualizada
     * @throws com.university.cleanarchitecture.domain.exception.CarreraNotFoundException
     *         si no existe la carrera
     * @throws IllegalArgumentException si el nombre ya existe en otra carrera
     */
    CarreraResponse update(UpdateCarreraCommand command);
}

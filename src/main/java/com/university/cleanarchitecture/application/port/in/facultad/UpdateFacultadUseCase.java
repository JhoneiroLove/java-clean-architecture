package com.university.cleanarchitecture.application.port.in.facultad;

import com.university.cleanarchitecture.application.dto.command.UpdateFacultadCommand;
import com.university.cleanarchitecture.application.dto.response.FacultadResponse;

/**
 * Puerto de entrada (Input Port) para actualizar una Facultad existente.
 *
 * Define el contrato para el caso de uso de actualización de facultades.
 */
public interface UpdateFacultadUseCase {

    /**
     * Actualiza los datos de una facultad existente.
     *
     * Proceso:
     * 1. Busca la facultad por ID
     * 2. Valida que no exista otra facultad con el mismo nombre
     * 3. Aplica los cambios a la entidad
     * 4. Persiste los cambios
     * 5. Retorna la respuesta actualizada
     *
     * @param command datos de actualización
     * @return FacultadResponse con la información actualizada
     * @throws com.university.cleanarchitecture.domain.exception.FacultadNotFoundException
     *         si no existe la facultad
     * @throws IllegalArgumentException si el nombre ya existe en otra facultad
     */
    FacultadResponse update(UpdateFacultadCommand command);
}

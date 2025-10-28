package com.university.cleanarchitecture.application.port.in.facultad;

import com.university.cleanarchitecture.application.dto.response.OperationResponse;

/**
 * Puerto de entrada (Input Port) para gestionar el estado de Facultades.
 *
 * Operaciones de activación/desactivación de facultades.
 */
public interface ManageFacultadStatusUseCase {

    /**
     * Desactiva una facultad.
     *
     * Regla de negocio: Solo se puede desactivar si no tiene carreras activas.
     *
     * @param facultadId ID de la facultad a desactivar
     * @return OperationResponse indicando el resultado
     * @throws com.university.cleanarchitecture.domain.exception.FacultadNotFoundException
     *         si no existe la facultad
     * @throws IllegalStateException si tiene carreras activas
     */
    OperationResponse deactivate(Long facultadId);

    /**
     * Activa una facultad previamente desactivada.
     *
     * @param facultadId ID de la facultad a activar
     * @return OperationResponse indicando el resultado
     * @throws com.university.cleanarchitecture.domain.exception.FacultadNotFoundException
     *         si no existe la facultad
     */
    OperationResponse activate(Long facultadId);

    /**
     * Elimina una facultad del sistema.
     *
     * Regla de negocio: Solo se puede eliminar si está inactiva y sin carreras.
     *
     * @param facultadId ID de la facultad a eliminar
     * @return OperationResponse indicando el resultado
     * @throws com.university.cleanarchitecture.domain.exception.FacultadNotFoundException
     *         si no existe la facultad
     * @throws IllegalStateException si está activa o tiene carreras
     */
    OperationResponse delete(Long facultadId);
}

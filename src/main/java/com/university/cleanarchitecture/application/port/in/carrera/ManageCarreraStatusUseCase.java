package com.university.cleanarchitecture.application.port.in.carrera;

import com.university.cleanarchitecture.application.dto.response.OperationResponse;

/**
 * Puerto de entrada (Input Port) para gestionar el estado de Carreras.
 *
 * Operaciones de activación/desactivación/eliminación de carreras.
 */
public interface ManageCarreraStatusUseCase {

    /**
     * Desactiva una carrera.
     *
     * Una carrera desactivada no acepta nuevos estudiantes.
     *
     * @param carreraId ID de la carrera a desactivar
     * @return OperationResponse indicando el resultado
     * @throws com.university.cleanarchitecture.domain.exception.CarreraNotFoundException
     *         si no existe la carrera
     */
    OperationResponse deactivate(Long carreraId);

    /**
     * Activa una carrera previamente desactivada.
     *
     * @param carreraId ID de la carrera a activar
     * @return OperationResponse indicando el resultado
     * @throws com.university.cleanarchitecture.domain.exception.CarreraNotFoundException
     *         si no existe la carrera
     */
    OperationResponse activate(Long carreraId);

    /**
     * Elimina una carrera del sistema.
     *
     * Regla de negocio: Solo se puede eliminar si está inactiva.
     *
     * @param carreraId ID de la carrera a eliminar
     * @return OperationResponse indicando el resultado
     * @throws com.university.cleanarchitecture.domain.exception.CarreraNotFoundException
     *         si no existe la carrera
     * @throws IllegalStateException si está activa
     */
    OperationResponse delete(Long carreraId);
}
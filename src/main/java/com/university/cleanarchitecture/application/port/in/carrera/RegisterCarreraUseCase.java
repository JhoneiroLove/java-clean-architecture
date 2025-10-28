package com.university.cleanarchitecture.application.port.in.carrera;

import com.university.cleanarchitecture.application.dto.command.RegisterCarreraCommand;
import com.university.cleanarchitecture.application.dto.response.CarreraResponse;

/**
 * Puerto de entrada (Input Port) para registrar una nueva Carrera.
 *
 * Define el contrato para el caso de uso de creación de carreras.
 */
public interface RegisterCarreraUseCase {

    /**
     * Registra una nueva carrera en el sistema.
     *
     * Proceso:
     * 1. Valida que la facultad exista y esté activa
     * 2. Valida que no exista una carrera con el mismo nombre
     * 3. Crea la entidad de dominio
     * 4. Persiste en el repositorio
     * 5. Retorna la respuesta con los datos de la carrera creada
     *
     * @param command datos para crear la carrera
     * @return CarreraResponse con la información de la carrera registrada
     * @throws com.university.cleanarchitecture.domain.exception.FacultadNotFoundException
     *         si no existe la facultad
     * @throws IllegalStateException si la facultad no está activa
     * @throws IllegalArgumentException si ya existe una carrera con el mismo nombre
     */
    CarreraResponse register(RegisterCarreraCommand command);
}

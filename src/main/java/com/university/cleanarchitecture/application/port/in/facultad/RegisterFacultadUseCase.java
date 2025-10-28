package com.university.cleanarchitecture.application.port.in.facultad;

import com.university.cleanarchitecture.application.dto.command.RegisterFacultadCommand;
import com.university.cleanarchitecture.application.dto.response.FacultadResponse;

/**
 * Puerto de entrada (Input Port) para registrar una nueva Facultad.
 *
 * Define el contrato para el caso de uso de creación de facultades.
 * Los adaptadores de entrada (Controllers, CLI, etc.) utilizan esta interfaz.
 *
 * Patrón: Hexagonal Architecture / Ports & Adapters
 */
public interface RegisterFacultadUseCase {

    /**
     * Registra una nueva facultad en el sistema.
     *
     * Proceso:
     * 1. Valida que no exista una facultad con el mismo nombre
     * 2. Crea la entidad de dominio
     * 3. Persiste en el repositorio
     * 4. Retorna la respuesta con los datos de la facultad creada
     *
     * @param command datos para crear la facultad
     * @return FacultadResponse con la información de la facultad registrada
     * @throws IllegalArgumentException si el comando tiene datos inválidos
     * @throws com.university.cleanarchitecture.domain.exception.DomainException
     *         si ya existe una facultad con el mismo nombre
     */
    FacultadResponse register(RegisterFacultadCommand command);
}

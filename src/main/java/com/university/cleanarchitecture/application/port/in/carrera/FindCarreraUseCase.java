package com.university.cleanarchitecture.application.port.in.carrera;

import com.university.cleanarchitecture.application.dto.query.FindCarreraByIdQuery;
import com.university.cleanarchitecture.application.dto.query.FindCarrerasByDuracionQuery;
import com.university.cleanarchitecture.application.dto.query.FindCarrerasByFacultadQuery;
import com.university.cleanarchitecture.application.dto.response.CarreraResponse;
import com.university.cleanarchitecture.application.dto.response.CarreraSummaryResponse;

import java.util.List;

/**
 * Puerto de entrada (Input Port) para consultas de Carreras.
 *
 * Define el contrato para todos los casos de uso de lectura de carreras.
 * Implementa el patrón CQRS (Command Query Responsibility Segregation).
 */
public interface FindCarreraUseCase {

    /**
     * Busca una carrera por su ID.
     *
     * @param query query con el ID de la carrera
     * @return CarreraResponse con la información completa
     * @throws com.university.cleanarchitecture.domain.exception.CarreraNotFoundException
     *         si no existe la carrera
     */
    CarreraResponse findById(FindCarreraByIdQuery query);

    /**
     * Obtiene todas las carreras de una facultad específica.
     *
     * @param query query con el ID de la facultad y filtro de activas
     * @return lista de CarreraSummaryResponse
     */
    List<CarreraSummaryResponse> findByFacultad(FindCarrerasByFacultadQuery query);

    /**
     * Busca carreras por duración en semestres.
     *
     * Permite búsqueda por:
     * - Duración exacta
     * - Rango de duración (mínimo y/o máximo)
     *
     * @param query query con criterios de duración
     * @return lista de CarreraSummaryResponse
     */
    List<CarreraSummaryResponse> findByDuracion(FindCarrerasByDuracionQuery query);
}

package com.university.cleanarchitecture.application.port.in.facultad;

import com.university.cleanarchitecture.application.dto.query.FindAllFacultadesQuery;
import com.university.cleanarchitecture.application.dto.query.FindFacultadByIdQuery;
import com.university.cleanarchitecture.application.dto.query.FindFacultadByNombreQuery;
import com.university.cleanarchitecture.application.dto.response.FacultadResponse;
import com.university.cleanarchitecture.application.dto.response.FacultadSummaryResponse;

import java.util.List;

/**
 * Puerto de entrada (Input Port) para consultas de Facultades.
 *
 * Define el contrato para todos los casos de uso de lectura de facultades.
 * Implementa el patrón CQRS (Command Query Responsibility Segregation).
 */
public interface FindFacultadUseCase {

    /**
     * Busca una facultad por su ID.
     *
     * @param query query con el ID de la facultad
     * @return FacultadResponse con la información completa
     * @throws com.university.cleanarchitecture.domain.exception.FacultadNotFoundException
     *         si no existe la facultad
     */
    FacultadResponse findById(FindFacultadByIdQuery query);

    /**
     * Busca una facultad por su nombre.
     *
     * @param query query con el nombre de la facultad
     * @return FacultadResponse con la información completa
     * @throws com.university.cleanarchitecture.domain.exception.FacultadNotFoundException
     *         si no existe la facultad
     */
    FacultadResponse findByNombre(FindFacultadByNombreQuery query);

    /**
     * Obtiene todas las facultades del sistema.
     *
     * @param query query con filtros (incluir inactivas o no)
     * @return lista de FacultadSummaryResponse
     */
    List<FacultadSummaryResponse> findAll(FindAllFacultadesQuery query);
}

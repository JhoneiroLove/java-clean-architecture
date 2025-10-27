package com.university.cleanarchitecture.application.dto.query;

/**
 * Query inmutable para obtener todas las Facultades activas.
 *
 * Este query no requiere parámetros ya que simplemente solicita
 * todas las facultades que están activas en el sistema.
 *
 * @param incluirInactivas Si true, incluye también facultades inactivas
 */
public record FindAllFacultadesQuery(

        boolean incluirInactivas
) {

    /**
     * Constructor por defecto que solo busca facultades activas.
     */
    public FindAllFacultadesQuery() {
        this(false);
    }
}

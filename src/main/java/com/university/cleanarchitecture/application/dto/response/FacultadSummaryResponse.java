package com.university.cleanarchitecture.application.dto.response;

/**
 * Response inmutable simplificado para listar Facultades.
 * Contiene solo la información esencial para mostrar en listas.
 *
 * @param id ID único de la facultad
 * @param nombre Nombre de la facultad
 * @param ubicacion Ubicación física
 * @param decano Nombre del decano
 * @param activo Estado de activación
 * @param cantidadCarreras Número de carreras asociadas
 */
public record FacultadSummaryResponse(

        Long id,
        String nombre,
        String ubicacion,
        String decano,
        boolean activo,
        int cantidadCarreras
) {

    /**
     * Determina si la facultad puede ser eliminada.
     * Una facultad solo puede eliminarse si no está activa y no tiene carreras.
     */
    public boolean puedeSerEliminada() {
        return !activo && cantidadCarreras == 0;
    }

    /**
     * Determina si la facultad puede ser desactivada.
     * Una facultad solo puede desactivarse si no tiene carreras activas.
     */
    public boolean puedeSerDesactivada() {
        return activo && cantidadCarreras == 0;
    }
}




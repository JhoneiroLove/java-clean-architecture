package com.university.cleanarchitecture.application.dto.response;

/**
 * Response inmutable simplificado para listar Carreras.
 * Contiene solo la información esencial para mostrar en listas.
 *
 * @param id ID único de la carrera
 * @param nombre Nombre de la carrera
 * @param duracionSemestres Duración en semestres
 * @param tituloOtorgado Título profesional que otorga
 * @param activo Estado de activación
 * @param facultadId ID de la facultad
 * @param facultadNombre Nombre de la facultad
 */
public record CarreraSummaryResponse(

        Long id,
        String nombre,
        int duracionSemestres,
        String tituloOtorgado,
        boolean activo,
        Long facultadId,
        String facultadNombre
) {

    /**
     * Calcula la duración en años.
     */
    public int getDuracionAnios() {
        return (int) Math.ceil(duracionSemestres / 2.0);
    }

    /**
     * Determina si es una carrera de duración estándar.
     */
    public boolean isEstandar() {
        return duracionSemestres == 10;
    }

    /**
     * Determina si es una carrera corta.
     */
    public boolean isCorta() {
        return duracionSemestres < 10;
    }

    /**
     * Determina si es una carrera larga.
     */
    public boolean isLarga() {
        return duracionSemestres > 10;
    }
}

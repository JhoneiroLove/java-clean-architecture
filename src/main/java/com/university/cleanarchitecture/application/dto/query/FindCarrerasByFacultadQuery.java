package com.university.cleanarchitecture.application.dto.query;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * Query inmutable para buscar todas las Carreras de una Facultad.
 *
 * @param facultadId ID de la facultad (requerido)
 * @param soloActivas Si true, solo retorna carreras activas
 */
public record FindCarrerasByFacultadQuery(

        @NotNull(message = "El ID de la facultad es obligatorio")
        @Positive(message = "El ID debe ser un número positivo")
        Long facultadId,

        boolean soloActivas
) {

    /**
     * Constructor que por defecto solo busca carreras activas.
     */
    public FindCarrerasByFacultadQuery(Long facultadId) {
        this(facultadId, true);
    }
}
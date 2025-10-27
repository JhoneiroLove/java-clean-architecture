package com.university.cleanarchitecture.application.dto.query;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * Query inmutable para buscar una Facultad por su ID.
 *
 * @param facultadId ID de la facultad a buscar (requerido)
 */
public record FindFacultadByIdQuery(

        @NotNull(message = "El ID de la facultad es obligatorio")
        @Positive(message = "El ID debe ser un número positivo")
        Long facultadId
) {
}
package com.university.cleanarchitecture.application.dto.query;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * Query inmutable para buscar una Carrera por su ID.
 *
 * @param carreraId ID de la carrera a buscar (requerido)
 */
public record FindCarreraByIdQuery(

        @NotNull(message = "El ID de la carrera es obligatorio")
        @Positive(message = "El ID debe ser un número positivo")
        Long carreraId
) {
}

package com.university.cleanarchitecture.application.dto.command;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

/**
 * Command inmutable para cambiar el decano de una Facultad.
 * Operación específica que modifica solo el decano.
 *
 * @param facultadId ID de la facultad (requerido)
 * @param nuevoDecano Nombre del nuevo decano (requerido)
 */
public record CambiarDecanoCommand(

        @NotNull(message = "El ID de la facultad es obligatorio")
        @Positive(message = "El ID debe ser un número positivo")
        Long facultadId,

        @NotBlank(message = "El nombre del nuevo decano es obligatorio")
        @Size(min = 3, max = 100, message = "El nombre del decano debe tener entre 3 y 100 caracteres")
        String nuevoDecano
) {

    public CambiarDecanoCommand {
        if (nuevoDecano != null) {
            nuevoDecano = nuevoDecano.trim();
        }
    }
}




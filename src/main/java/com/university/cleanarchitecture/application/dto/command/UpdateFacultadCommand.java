package com.university.cleanarchitecture.application.dto.command;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

/**
 * Command inmutable para actualizar una Facultad existente.
 *
 * @param facultadId ID de la facultad a actualizar (requerido)
 * @param nombre Nuevo nombre de la facultad (requerido)
 * @param descripcion Nueva descripción (opcional)
 * @param ubicacion Nueva ubicación (opcional)
 */
public record UpdateFacultadCommand(

        @NotNull(message = "El ID de la facultad es obligatorio")
        @Positive(message = "El ID debe ser un número positivo")
        Long facultadId,

        @NotBlank(message = "El nombre de la facultad es obligatorio")
        @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
        String nombre,

        @Size(max = 500, message = "La descripción no puede exceder 500 caracteres")
        String descripcion,

        @Size(max = 100, message = "La ubicación no puede exceder 100 caracteres")
        String ubicacion
) {

    public UpdateFacultadCommand {
        if (nombre != null) {
            nombre = nombre.trim();
        }
        if (descripcion != null) {
            descripcion = descripcion.trim();
        }
        if (ubicacion != null) {
            ubicacion = ubicacion.trim();
        }
    }
}




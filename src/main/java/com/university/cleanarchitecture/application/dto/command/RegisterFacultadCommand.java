package com.university.cleanarchitecture.application.dto.command;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Command inmutable para registrar una nueva Facultad.
 * Usa Record de Java para garantizar inmutabilidad.
 *
 * @param nombre Nombre de la facultad (requerido, 3-100 caracteres)
 * @param descripcion Descripción de la facultad (opcional)
 * @param ubicacion Ubicación física de la facultad (opcional)
 * @param decano Nombre del decano (requerido)
 */
public record RegisterFacultadCommand(

        @NotBlank(message = "El nombre de la facultad es obligatorio")
        @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
        String nombre,

        @Size(max = 500, message = "La descripción no puede exceder 500 caracteres")
        String descripcion,

        @Size(max = 100, message = "La ubicación no puede exceder 100 caracteres")
        String ubicacion,

        @NotBlank(message = "El nombre del decano es obligatorio")
        @Size(max = 100, message = "El nombre del decano no puede exceder 100 caracteres")
        String decano
) {

    /**
     * Constructor compacto para validaciones adicionales.
     * Se ejecuta después de las validaciones de Bean Validation.
     */
    public RegisterFacultadCommand {
        // Trimming automático de strings
        if (nombre != null) {
            nombre = nombre.trim();
        }
        if (descripcion != null) {
            descripcion = descripcion.trim();
        }
        if (ubicacion != null) {
            ubicacion = ubicacion.trim();
        }
        if (decano != null) {
            decano = decano.trim();
        }
    }
}




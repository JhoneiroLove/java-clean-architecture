package com.university.cleanarchitecture.application.dto.command;

import jakarta.validation.constraints.*;

/**
 * Command inmutable para actualizar una Carrera existente.
 *
 * @param carreraId ID de la carrera a actualizar (requerido)
 * @param nombre Nuevo nombre de la carrera (requerido)
 * @param descripcion Nueva descripción (opcional)
 * @param duracionSemestres Nueva duración en semestres (requerido, 6-14)
 * @param tituloOtorgado Nuevo título profesional (requerido)
 */
public record UpdateCarreraCommand(

        @NotNull(message = "El ID de la carrera es obligatorio")
        @Positive(message = "El ID debe ser un número positivo")
        Long carreraId,

        @NotBlank(message = "El nombre de la carrera es obligatorio")
        @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
        String nombre,

        @Size(max = 500, message = "La descripción no puede exceder 500 caracteres")
        String descripcion,

        @NotNull(message = "La duración en semestres es obligatoria")
        @Min(value = 6, message = "La duración mínima es de 6 semestres")
        @Max(value = 14, message = "La duración máxima es de 14 semestres")
        Integer duracionSemestres,

        @NotBlank(message = "El título otorgado es obligatorio")
        @Size(min = 5, max = 100, message = "El título otorgado debe tener entre 5 y 100 caracteres")
        String tituloOtorgado
) {

    public UpdateCarreraCommand {
        if (nombre != null) {
            nombre = nombre.trim();
        }
        if (descripcion != null) {
            descripcion = descripcion.trim();
        }
        if (tituloOtorgado != null) {
            tituloOtorgado = tituloOtorgado.trim();
        }
    }
}

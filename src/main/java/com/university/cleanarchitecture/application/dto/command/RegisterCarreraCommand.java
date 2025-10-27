package com.university.cleanarchitecture.application.dto.command;

import jakarta.validation.constraints.*;

/**
 * Command inmutable para registrar una nueva Carrera.
 *
 * @param facultadId ID de la facultad a la que pertenece (requerido)
 * @param nombre Nombre de la carrera (requerido, 3-100 caracteres)
 * @param descripcion Descripción de la carrera (opcional)
 * @param duracionSemestres Duración en semestres (requerido, 6-14)
 * @param tituloOtorgado Título profesional que otorga (requerido)
 */
public record RegisterCarreraCommand(

        @NotNull(message = "El ID de la facultad es obligatorio")
        @Positive(message = "El ID de la facultad debe ser un número positivo")
        Long facultadId,

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

    public RegisterCarreraCommand {
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

    /**
     * Método auxiliar para verificar si es una carrera de duración estándar.
     * Útil para lógica de negocio en la capa de aplicación.
     */
    public boolean isDuracionEstandar() {
        return duracionSemestres != null && duracionSemestres == 10;
    }

    /**
     * Calcula la duración en años.
     */
    public int getDuracionEnAnios() {
        return duracionSemestres != null ? (int) Math.ceil(duracionSemestres / 2.0) : 0;
    }
}

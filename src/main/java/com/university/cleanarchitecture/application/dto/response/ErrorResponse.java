package com.university.cleanarchitecture.application.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * Response inmutable para errores de validación.
 * Usado por el GlobalExceptionHandler para retornar errores de forma consistente.
 *
 * @param code Código del error
 * @param message Mensaje general del error
 * @param timestamp Momento en que ocurrió el error
 * @param validationErrors Mapa de errores de validación por campo (opcional)
 */
public record ErrorResponse(

        String code,
        String message,

        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime timestamp,

        Map<String, String> validationErrors
) {

    /**
     * Constructor para error simple sin detalles de validación.
     */
    public ErrorResponse(String code, String message) {
        this(code, message, LocalDateTime.now(), null);
    }

    /**
     * Constructor para errores de validación con detalles por campo.
     */
    public ErrorResponse(String code, String message, Map<String, String> validationErrors) {
        this(code, message, LocalDateTime.now(), validationErrors);
    }

    /**
     * Verifica si hay errores de validación.
     */
    public boolean hasValidationErrors() {
        return validationErrors != null && !validationErrors.isEmpty();
    }
}




package com.university.cleanarchitecture.application.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

/**
 * Response inmutable genérico para operaciones exitosas.
 * Usado para respuestas de comandos que no retornan datos complejos.
 *
 * @param success Indica si la operación fue exitosa
 * @param message Mensaje descriptivo de la operación
 * @param timestamp Momento en que se completó la operación
 * @param resourceId ID del recurso afectado (opcional)
 */
public record OperationResponse(

        boolean success,
        String message,

        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime timestamp,

        Long resourceId
) {

    /**
     * Constructor para operación exitosa con ID de recurso.
     */
    public static OperationResponse success(String message, Long resourceId) {
        return new OperationResponse(true, message, LocalDateTime.now(), resourceId);
    }

    /**
     * Constructor para operación exitosa sin ID de recurso.
     */
    public static OperationResponse success(String message) {
        return new OperationResponse(true, message, LocalDateTime.now(), null);
    }

    /**
     * Constructor para operación fallida.
     */
    public static OperationResponse failure(String message) {
        return new OperationResponse(false, message, LocalDateTime.now(), null);
    }
}

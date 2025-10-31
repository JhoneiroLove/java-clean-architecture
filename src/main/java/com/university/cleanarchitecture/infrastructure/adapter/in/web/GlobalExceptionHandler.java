package com.university.cleanarchitecture.infrastructure.adapter.in.web;

import com.university.cleanarchitecture.application.dto.response.ErrorResponse;
import com.university.cleanarchitecture.domain.exception.CarreraNotFoundException;
import com.university.cleanarchitecture.domain.exception.DomainException;
import com.university.cleanarchitecture.domain.exception.FacultadNotFoundException;
import com.university.cleanarchitecture.domain.exception.InvalidDurationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(FacultadNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleFacultadNotFoundException(FacultadNotFoundException ex) {
        ErrorResponse error = createErrorResponse("FACULTAD_NOT_FOUND", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(CarreraNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleCarreraNotFoundException(CarreraNotFoundException ex) {
        ErrorResponse error = createErrorResponse("CARRERA_NOT_FOUND", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(InvalidDurationException.class)
    public ResponseEntity<ErrorResponse> handleInvalidDurationException(InvalidDurationException ex) {
        ErrorResponse error = createErrorResponse("INVALID_DURATION", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        ErrorResponse error = createErrorResponse(
                "VALIDATION_ERROR",
                "Errores de validación en los datos de entrada"
        );
        error.setValidationErrors(errors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ErrorResponse> handleDomainException(DomainException ex) {
        ErrorResponse error = createErrorResponse("DOMAIN_ERROR", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        ErrorResponse error = createErrorResponse("INVALID_ARGUMENT", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponse> handleIllegalStateException(IllegalStateException ex) {
        ErrorResponse error = createErrorResponse("INVALID_STATE", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception ex) {
        ErrorResponse error = createErrorResponse(
                "INTERNAL_ERROR",
                "Error interno del servidor"
        );
        // Log the exception for debugging
        ex.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    private ErrorResponse createErrorResponse(String code, String message) {
        ErrorResponse error = new ErrorResponse();
        error.setCode(code);
        error.setMessage(message);
        error.setTimestamp(LocalDateTime.now());
        return error;
    }
}


package com.university.cleanarchitecture.domain.exception;

public class InvalidDurationException extends DomainException {

    public InvalidDurationException(String message) {
        super(message);
    }

    public InvalidDurationException(int semestres) {
        super("Duración inválida: " + semestres + " semestres. Debe ser entre 6 y 14 semestres.");
    }
}
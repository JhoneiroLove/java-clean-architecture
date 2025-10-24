package com.university.cleanarchitecture.domain.exception;

public class FacultadNotFoundException extends DomainException {

    public FacultadNotFoundException(String message) {
        super(message);
    }

    public FacultadNotFoundException(Long facultadId) {
        super("Facultad no encontrada con ID: " + facultadId);
    }
}
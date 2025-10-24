package com.university.cleanarchitecture.domain.exception;

public class CarreraNotFoundException extends DomainException {

    public CarreraNotFoundException(String message) {
        super(message);
    }

    public CarreraNotFoundException(Long carreraId) {
        super("Carrera no encontrada con ID: " + carreraId);
    }
}
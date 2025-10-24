package com.university.cleanarchitecture.domain.model.valueobjects;

import com.university.cleanarchitecture.domain.exception.InvalidDurationException;

import java.util.Objects;

public class Duracion {

    private static final int MIN_SEMESTRES = 6;
    private static final int MAX_SEMESTRES = 14;

    private final int semestres;

    public Duracion(int semestres) {
        if (semestres < MIN_SEMESTRES || semestres > MAX_SEMESTRES) {
            throw new InvalidDurationException(semestres);
        }
        this.semestres = semestres;
    }

    public static Duracion of(int semestres) {
        return new Duracion(semestres);
    }

    public int getSemestres() {
        return semestres;
    }

    /**
     * Calcula la duración en años (redondeando hacia arriba)
     */
    public int getAnios() {
        return (int) Math.ceil(semestres / 2.0);
    }

    /**
     * Verifica si es una carrera de duración estándar (10 semestres = 5 años)
     */
    public boolean isEstandar() {
        return semestres == 10;
    }

    /**
     * Verifica si es una carrera corta (menos de 10 semestres)
     */
    public boolean isCorta() {
        return semestres < 10;
    }

    /**
     * Verifica si es una carrera larga (más de 10 semestres)
     */
    public boolean isLarga() {
        return semestres > 10;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Duracion)) return false;
        Duracion duracion = (Duracion) o;
        return semestres == duracion.semestres;
    }

    @Override
    public int hashCode() {
        return Objects.hash(semestres);
    }

    @Override
    public String toString() {
        return semestres + " semestres (" + getAnios() + " años)";
    }
}



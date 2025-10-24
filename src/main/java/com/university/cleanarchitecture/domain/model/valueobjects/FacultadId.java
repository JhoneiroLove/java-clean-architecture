package com.university.cleanarchitecture.domain.model.valueobjects;

import java.util.Objects;

public class FacultadId {

    private final Long value;

    public FacultadId(Long value) {
        if (value == null || value <= 0) {
            throw new IllegalArgumentException("FacultadId debe ser un valor positivo");
        }
        this.value = value;
    }

    public static FacultadId of(Long value) {
        return new FacultadId(value);
    }

    public Long getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FacultadId)) return false;
        FacultadId that = (FacultadId) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
package com.university.cleanarchitecture.domain.model.valueobjects;

import java.util.Objects;

public class CarreraId {

    private final Long value;

    public CarreraId(Long value) {
        if (value == null || value <= 0) {
            throw new IllegalArgumentException("CarreraId debe ser un valor positivo");
        }
        this.value = value;
    }

    public static CarreraId of(Long value) {
        return new CarreraId(value);
    }

    public Long getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CarreraId)) return false;
        CarreraId that = (CarreraId) o;
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
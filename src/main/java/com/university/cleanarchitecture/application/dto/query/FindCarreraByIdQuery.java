package com.university.cleanarchitecture.application.dto.query;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class FindCarreraByIdQuery {

    @NotNull(message = "El ID de la carrera es obligatorio")
    @Positive(message = "El ID debe ser un número positivo")
    private Long carreraId;

    public FindCarreraByIdQuery() {}

    public FindCarreraByIdQuery(Long carreraId) {
        this.carreraId = carreraId;
    }

    public Long getCarreraId() { return carreraId; }
    public void setCarreraId(Long carreraId) { this.carreraId = carreraId; }
}

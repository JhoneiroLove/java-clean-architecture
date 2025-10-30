package com.university.cleanarchitecture.application.dto.query;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class FindFacultadByIdQuery {

    @NotNull(message = "El ID de la facultad es obligatorio")
    @Positive(message = "El ID debe ser un número positivo")
    private Long facultadId;

    public FindFacultadByIdQuery() {}

    public FindFacultadByIdQuery(Long facultadId) {
        this.facultadId = facultadId;
    }

    public Long getFacultadId() { return facultadId; }
    public void setFacultadId(Long facultadId) { this.facultadId = facultadId; }
}
package com.university.cleanarchitecture.application.dto.query;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class FindCarrerasByFacultadQuery {

    @NotNull(message = "El ID de la facultad es obligatorio")
    @Positive(message = "El ID debe ser un número positivo")
    private Long facultadId;

    private boolean soloActivas;

    public FindCarrerasByFacultadQuery() {}

    public FindCarrerasByFacultadQuery(Long facultadId, boolean soloActivas) {
        this.facultadId = facultadId;
        this.soloActivas = soloActivas;
    }

    public Long getFacultadId() { return facultadId; }
    public void setFacultadId(Long facultadId) { this.facultadId = facultadId; }

    public boolean isSoloActivas() { return soloActivas; }
    public void setSoloActivas(boolean soloActivas) { this.soloActivas = soloActivas; }
}
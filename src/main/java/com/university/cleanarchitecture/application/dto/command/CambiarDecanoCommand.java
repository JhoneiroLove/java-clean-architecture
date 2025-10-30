package com.university.cleanarchitecture.application.dto.command;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class CambiarDecanoCommand {

    @NotNull(message = "El ID de la facultad es obligatorio")
    @Positive(message = "El ID debe ser un número positivo")
    private Long facultadId;

    @NotBlank(message = "El nombre del nuevo decano es obligatorio")
    @Size(min = 3, max = 100, message = "El nombre del decano debe tener entre 3 y 100 caracteres")
    private String nuevoDecano;

    // Constructor por defecto
    public CambiarDecanoCommand() {}

    // Constructor completo
    public CambiarDecanoCommand(Long facultadId, String nuevoDecano) {
        this.facultadId = facultadId;
        this.nuevoDecano = nuevoDecano;
    }

    // Getters y Setters
    public Long getFacultadId() { return facultadId; }
    public void setFacultadId(Long facultadId) { this.facultadId = facultadId; }

    public String getNuevoDecano() { return nuevoDecano; }
    public void setNuevoDecano(String nuevoDecano) { this.nuevoDecano = nuevoDecano; }
}


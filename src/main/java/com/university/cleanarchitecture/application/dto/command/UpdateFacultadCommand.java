package com.university.cleanarchitecture.application.dto.command;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class UpdateFacultadCommand {

    @NotNull(message = "El ID de la facultad es obligatorio")
    @Positive(message = "El ID debe ser un número positivo")
    private Long facultadId;

    @NotBlank(message = "El nombre de la facultad es obligatorio")
    @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
    private String nombre;

    @Size(max = 500, message = "La descripción no puede exceder 500 caracteres")
    private String descripcion;

    @Size(max = 100, message = "La ubicación no puede exceder 100 caracteres")
    private String ubicacion;

    // Constructor por defecto
    public UpdateFacultadCommand() {}

    // Constructor completo
    public UpdateFacultadCommand(Long facultadId, String nombre, String descripcion, String ubicacion) {
        this.facultadId = facultadId;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.ubicacion = ubicacion;
    }

    // Getters y Setters
    public Long getFacultadId() { return facultadId; }
    public void setFacultadId(Long facultadId) { this.facultadId = facultadId; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getUbicacion() { return ubicacion; }
    public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }
}


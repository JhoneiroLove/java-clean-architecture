package com.university.cleanarchitecture.application.dto.command;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RegisterFacultadCommand {

    @NotBlank(message = "El nombre de la facultad es obligatorio")
    @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
    private String nombre;

    @Size(max = 500, message = "La descripción no puede exceder 500 caracteres")
    private String descripcion;

    @Size(max = 100, message = "La ubicación no puede exceder 100 caracteres")
    private String ubicacion;

    @NotBlank(message = "El nombre del decano es obligatorio")
    @Size(max = 100, message = "El nombre del decano no puede exceder 100 caracteres")
    private String decano;

    // Constructor por defecto
    public RegisterFacultadCommand() {}

    // Constructor completo
    public RegisterFacultadCommand(String nombre, String descripcion, String ubicacion, String decano) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.ubicacion = ubicacion;
        this.decano = decano;
    }

    // Getters y Setters
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getUbicacion() { return ubicacion; }
    public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }

    public String getDecano() { return decano; }
    public void setDecano(String decano) { this.decano = decano; }
}


package com.university.cleanarchitecture.application.dto.query;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class FindFacultadByNombreQuery {

    @NotBlank(message = "El nombre de búsqueda es obligatorio")
    @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
    private String nombre;

    public FindFacultadByNombreQuery() {}

    public FindFacultadByNombreQuery(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
}


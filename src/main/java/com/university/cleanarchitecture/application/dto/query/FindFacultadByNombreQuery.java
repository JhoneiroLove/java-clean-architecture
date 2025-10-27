package com.university.cleanarchitecture.application.dto.query;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Query inmutable para buscar una Facultad por su nombre.
 *
 * @param nombre Nombre de la facultad a buscar (requerido)
 */
public record FindFacultadByNombreQuery(

        @NotBlank(message = "El nombre de búsqueda es obligatorio")
        @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
        String nombre
) {

    public FindFacultadByNombreQuery {
        if (nombre != null) {
            nombre = nombre.trim();
        }
    }
}



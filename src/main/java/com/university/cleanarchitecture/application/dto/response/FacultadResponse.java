package com.university.cleanarchitecture.application.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

/**
 * Response inmutable que representa una Facultad.
 * Usado para retornar datos al cliente.
 *
 * @param id ID único de la facultad
 * @param nombre Nombre de la facultad (ya normalizado)
 * @param descripcion Descripción de la facultad
 * @param ubicacion Ubicación física
 * @param decano Nombre del decano actual
 * @param fechaRegistro Fecha de registro en el sistema
 * @param activo Estado de activación
 * @param cantidadCarreras Número de carreras asociadas
 */
public record FacultadResponse(

        Long id,
        String nombre,
        String descripcion,
        String ubicacion,
        String decano,

        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime fechaRegistro,

        boolean activo,
        int cantidadCarreras
) {

    /**
     * Constructor simplificado sin cantidad de carreras.
     * Útil cuando no se necesita la información agregada.
     */
    public FacultadResponse(Long id, String nombre, String descripcion,
                            String ubicacion, String decano,
                            LocalDateTime fechaRegistro, boolean activo) {
        this(id, nombre, descripcion, ubicacion, decano, fechaRegistro, activo, 0);
    }

    /**
     * Retorna las iniciales del nombre de la facultad.
     * Útil para códigos o abreviaturas.
     */
    public String getIniciales() {
        if (nombre == null || nombre.isEmpty()) {
            return "";
        }

        StringBuilder iniciales = new StringBuilder();
        String[] palabras = nombre.split("\\s+");

        for (String palabra : palabras) {
            if (!palabra.isEmpty() && !esPalabraMinuscula(palabra.toLowerCase())) {
                iniciales.append(palabra.charAt(0));
            }
        }

        return iniciales.toString().toUpperCase();
    }

    private boolean esPalabraMinuscula(String palabra) {
        return palabra.equals("de") || palabra.equals("del") || palabra.equals("la") ||
                palabra.equals("las") || palabra.equals("los") || palabra.equals("el") ||
                palabra.equals("y") || palabra.equals("e");
    }
}
package com.university.cleanarchitecture.domain.model.valueobjects;

import java.util.Objects;

public class NombreAcademico {

    private static final int MIN_LENGTH = 3;
    private static final int MAX_LENGTH = 100;

    private final String value;

    public NombreAcademico(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre académico no puede estar vacío");
        }

        String trimmed = value.trim();

        if (trimmed.length() < MIN_LENGTH) {
            throw new IllegalArgumentException(
                    "El nombre académico debe tener al menos " + MIN_LENGTH + " caracteres"
            );
        }

        if (trimmed.length() > MAX_LENGTH) {
            throw new IllegalArgumentException(
                    "El nombre académico no puede exceder " + MAX_LENGTH + " caracteres"
            );
        }

        this.value = normalizar(trimmed);
    }

    public static NombreAcademico of(String value) {
        return new NombreAcademico(value);
    }

    /**
     * Normaliza el nombre: Primera letra de cada palabra en mayúscula
     */
    private String normalizar(String nombre) {
        String[] palabras = nombre.split("\\s+");
        StringBuilder resultado = new StringBuilder();

        for (int i = 0; i < palabras.length; i++) {
            if (i > 0) {
                resultado.append(" ");
            }

            String palabra = palabras[i];
            if (!palabra.isEmpty()) {
                // Excepciones: preposiciones y artículos en minúscula
                if (esPalabraMinuscula(palabra.toLowerCase())) {
                    resultado.append(palabra.toLowerCase());
                } else {
                    resultado.append(palabra.substring(0, 1).toUpperCase());
                    if (palabra.length() > 1) {
                        resultado.append(palabra.substring(1).toLowerCase());
                    }
                }
            }
        }

        return resultado.toString();
    }

    /**
     * Verifica si una palabra debe estar en minúscula (artículos, preposiciones)
     */
    private boolean esPalabraMinuscula(String palabra) {
        return palabra.equals("de") || palabra.equals("del") || palabra.equals("la") ||
                palabra.equals("las") || palabra.equals("los") || palabra.equals("el") ||
                palabra.equals("y") || palabra.equals("e");
    }

    public String getValue() {
        return value;
    }

    /**
     * Obtiene las iniciales del nombre (útil para códigos o abreviaturas)
     */
    public String getIniciales() {
        String[] palabras = value.split("\\s+");
        StringBuilder iniciales = new StringBuilder();

        for (String palabra : palabras) {
            if (!palabra.isEmpty() && !esPalabraMinuscula(palabra.toLowerCase())) {
                iniciales.append(palabra.charAt(0));
            }
        }

        return iniciales.toString().toUpperCase();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NombreAcademico)) return false;
        NombreAcademico that = (NombreAcademico) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
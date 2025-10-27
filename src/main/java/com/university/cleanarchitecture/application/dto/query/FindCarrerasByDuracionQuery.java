package com.university.cleanarchitecture.application.dto.query;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

/**
 * Query inmutable para buscar Carreras por su duración en semestres.
 * Permite filtrar carreras cortas, estándar o largas.
 *
 * @param duracionSemestres Duración exacta en semestres (opcional)
 * @param duracionMinima Duración mínima en semestres (opcional)
 * @param duracionMaxima Duración máxima en semestres (opcional)
 * @param soloActivas Si true, solo retorna carreras activas
 */
public record FindCarrerasByDuracionQuery(

        @Min(value = 6, message = "La duración mínima es de 6 semestres")
        @Max(value = 14, message = "La duración máxima es de 14 semestres")
        Integer duracionSemestres,

        @Min(value = 6, message = "La duración mínima es de 6 semestres")
        Integer duracionMinima,

        @Max(value = 14, message = "La duración máxima es de 14 semestres")
        Integer duracionMaxima,

        boolean soloActivas
) {

    /**
     * Constructor para buscar por duración exacta (solo carreras activas).
     */
    public FindCarrerasByDuracionQuery(Integer duracionSemestres) {
        this(duracionSemestres, null, null, true);
    }

    /**
     * Constructor para buscar por rango de duración (solo carreras activas).
     */
    public FindCarrerasByDuracionQuery(Integer duracionMinima, Integer duracionMaxima) {
        this(null, duracionMinima, duracionMaxima, true);
    }

    /**
     * Método helper para determinar si busca por duración exacta.
     */
    public boolean isBusquedaExacta() {
        return duracionSemestres != null;
    }

    /**
     * Método helper para determinar si busca por rango.
     */
    public boolean isBusquedaPorRango() {
        return duracionMinima != null || duracionMaxima != null;
    }
}




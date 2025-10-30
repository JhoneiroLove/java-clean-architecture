package com.university.cleanarchitecture.application.dto.query;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public class FindCarrerasByDuracionQuery {

    @Min(value = 6, message = "La duración mínima es de 6 semestres")
    @Max(value = 14, message = "La duración máxima es de 14 semestres")
    private Integer duracionSemestres;

    @Min(value = 6, message = "La duración mínima es de 6 semestres")
    private Integer duracionMinima;

    @Max(value = 14, message = "La duración máxima es de 14 semestres")
    private Integer duracionMaxima;

    private boolean soloActivas;

    public FindCarrerasByDuracionQuery() {}

    public FindCarrerasByDuracionQuery(Integer duracionSemestres, Integer duracionMinima,
                                       Integer duracionMaxima, boolean soloActivas) {
        this.duracionSemestres = duracionSemestres;
        this.duracionMinima = duracionMinima;
        this.duracionMaxima = duracionMaxima;
        this.soloActivas = soloActivas;
    }

    public Integer getDuracionSemestres() { return duracionSemestres; }
    public void setDuracionSemestres(Integer duracionSemestres) { this.duracionSemestres = duracionSemestres; }

    public Integer getDuracionMinima() { return duracionMinima; }
    public void setDuracionMinima(Integer duracionMinima) { this.duracionMinima = duracionMinima; }

    public Integer getDuracionMaxima() { return duracionMaxima; }
    public void setDuracionMaxima(Integer duracionMaxima) { this.duracionMaxima = duracionMaxima; }

    public boolean isSoloActivas() { return soloActivas; }
    public void setSoloActivas(boolean soloActivas) { this.soloActivas = soloActivas; }
}



package com.university.cleanarchitecture.application.dto.query;

public class FindAllFacultadesQuery {

    private boolean incluirInactivas;

    public FindAllFacultadesQuery() {}

    public FindAllFacultadesQuery(boolean incluirInactivas) {
        this.incluirInactivas = incluirInactivas;
    }

    public boolean isIncluirInactivas() { return incluirInactivas; }
    public void setIncluirInactivas(boolean incluirInactivas) { this.incluirInactivas = incluirInactivas; }
}

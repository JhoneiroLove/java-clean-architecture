package com.university.cleanarchitecture.application.dto.response;

public class CarreraSummaryResponse {

    private Long id;
    private String nombre;
    private int duracionSemestres;
    private String tituloOtorgado;
    private boolean activo;
    private Long facultadId;
    private String facultadNombre;

    // Constructor por defecto
    public CarreraSummaryResponse() {}

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public int getDuracionSemestres() { return duracionSemestres; }
    public void setDuracionSemestres(int duracionSemestres) { this.duracionSemestres = duracionSemestres; }

    public String getTituloOtorgado() { return tituloOtorgado; }
    public void setTituloOtorgado(String tituloOtorgado) { this.tituloOtorgado = tituloOtorgado; }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }

    public Long getFacultadId() { return facultadId; }
    public void setFacultadId(Long facultadId) { this.facultadId = facultadId; }

    public String getFacultadNombre() { return facultadNombre; }
    public void setFacultadNombre(String facultadNombre) { this.facultadNombre = facultadNombre; }
}

package com.university.cleanarchitecture.application.dto.response;

public class FacultadSummaryResponse {

    private Long id;
    private String nombre;
    private String ubicacion;
    private String decano;
    private boolean activo;
    private int cantidadCarreras;

    // Constructor por defecto
    public FacultadSummaryResponse() {}

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getUbicacion() { return ubicacion; }
    public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }

    public String getDecano() { return decano; }
    public void setDecano(String decano) { this.decano = decano; }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }

    public int getCantidadCarreras() { return cantidadCarreras; }
    public void setCantidadCarreras(int cantidadCarreras) { this.cantidadCarreras = cantidadCarreras; }
}


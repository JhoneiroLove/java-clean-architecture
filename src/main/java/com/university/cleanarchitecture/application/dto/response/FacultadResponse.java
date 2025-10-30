package com.university.cleanarchitecture.application.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

public class FacultadResponse {

    private Long id;
    private String nombre;
    private String descripcion;
    private String ubicacion;
    private String decano;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime fechaRegistro;

    private boolean activo;
    private int cantidadCarreras;

    // Constructor por defecto
    public FacultadResponse() {}

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getUbicacion() { return ubicacion; }
    public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }

    public String getDecano() { return decano; }
    public void setDecano(String decano) { this.decano = decano; }

    public LocalDateTime getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(LocalDateTime fechaRegistro) { this.fechaRegistro = fechaRegistro; }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }

    public int getCantidadCarreras() { return cantidadCarreras; }
    public void setCantidadCarreras(int cantidadCarreras) { this.cantidadCarreras = cantidadCarreras; }
}
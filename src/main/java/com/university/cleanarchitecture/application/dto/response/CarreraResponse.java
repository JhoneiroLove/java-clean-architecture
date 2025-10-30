package com.university.cleanarchitecture.application.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

public class CarreraResponse {

    private Long id;
    private String nombre;
    private String descripcion;
    private int duracionSemestres;
    private int duracionAnios;
    private String tituloOtorgado;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime fechaRegistro;

    private boolean activo;
    private Long facultadId;
    private String facultadNombre;
    private String clasificacion;

    // Constructor por defecto
    public CarreraResponse() {}

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public int getDuracionSemestres() { return duracionSemestres; }
    public void setDuracionSemestres(int duracionSemestres) { this.duracionSemestres = duracionSemestres; }

    public int getDuracionAnios() { return duracionAnios; }
    public void setDuracionAnios(int duracionAnios) { this.duracionAnios = duracionAnios; }

    public String getTituloOtorgado() { return tituloOtorgado; }
    public void setTituloOtorgado(String tituloOtorgado) { this.tituloOtorgado = tituloOtorgado; }

    public LocalDateTime getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(LocalDateTime fechaRegistro) { this.fechaRegistro = fechaRegistro; }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }

    public Long getFacultadId() { return facultadId; }
    public void setFacultadId(Long facultadId) { this.facultadId = facultadId; }

    public String getFacultadNombre() { return facultadNombre; }
    public void setFacultadNombre(String facultadNombre) { this.facultadNombre = facultadNombre; }

    public String getClasificacion() { return clasificacion; }
    public void setClasificacion(String clasificacion) { this.clasificacion = clasificacion; }
}

package com.university.cleanarchitecture.application.dto.command;

import jakarta.validation.constraints.*;

public class RegisterCarreraCommand {

    @NotNull(message = "El ID de la facultad es obligatorio")
    @Positive(message = "El ID de la facultad debe ser un número positivo")
    private Long facultadId;

    @NotBlank(message = "El nombre de la carrera es obligatorio")
    @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
    private String nombre;

    @Size(max = 500, message = "La descripción no puede exceder 500 caracteres")
    private String descripcion;

    @NotNull(message = "La duración en semestres es obligatoria")
    @Min(value = 6, message = "La duración mínima es de 6 semestres")
    @Max(value = 14, message = "La duración máxima es de 14 semestres")
    private Integer duracionSemestres;

    @NotBlank(message = "El título otorgado es obligatorio")
    @Size(min = 5, max = 100, message = "El título otorgado debe tener entre 5 y 100 caracteres")
    private String tituloOtorgado;

    // Constructor por defecto
    public RegisterCarreraCommand() {}

    // Constructor completo
    public RegisterCarreraCommand(Long facultadId, String nombre, String descripcion,
                                  Integer duracionSemestres, String tituloOtorgado) {
        this.facultadId = facultadId;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.duracionSemestres = duracionSemestres;
        this.tituloOtorgado = tituloOtorgado;
    }

    // Getters y Setters
    public Long getFacultadId() { return facultadId; }
    public void setFacultadId(Long facultadId) { this.facultadId = facultadId; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public Integer getDuracionSemestres() { return duracionSemestres; }
    public void setDuracionSemestres(Integer duracionSemestres) { this.duracionSemestres = duracionSemestres; }

    public String getTituloOtorgado() { return tituloOtorgado; }
    public void setTituloOtorgado(String tituloOtorgado) { this.tituloOtorgado = tituloOtorgado; }
}

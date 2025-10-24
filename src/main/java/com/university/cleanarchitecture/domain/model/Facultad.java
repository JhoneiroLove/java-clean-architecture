package com.university.cleanarchitecture.domain.model;

import com.university.cleanarchitecture.domain.model.valueobjects.FacultadId;
import com.university.cleanarchitecture.domain.model.valueobjects.NombreAcademico;

import java.time.LocalDateTime;
import java.util.Objects;

public class Facultad {

    private final FacultadId id;
    private NombreAcademico nombre;
    private String descripcion;
    private String ubicacion;
    private String decano;
    private final LocalDateTime fechaRegistro;
    private boolean activo;

    // Constructor completo (para reconstrucción desde persistencia)
    public Facultad(FacultadId id, NombreAcademico nombre, String descripcion,
                    String ubicacion, String decano, LocalDateTime fechaRegistro,
                    boolean activo) {
        this.id = Objects.requireNonNull(id, "FacultadId no puede ser nulo");
        this.setNombre(nombre);
        this.descripcion = descripcion;
        this.ubicacion = ubicacion;
        this.decano = decano;
        this.fechaRegistro = Objects.requireNonNull(fechaRegistro, "Fecha de registro no puede ser nula");
        this.activo = activo;
    }

    // Constructor para crear nueva facultad
    public Facultad(FacultadId id, NombreAcademico nombre, String descripcion,
                    String ubicacion, String decano) {
        this(id, nombre, descripcion, ubicacion, decano, LocalDateTime.now(), true);
    }

    /**
     * Factory method para crear una nueva facultad
     */
    public static Facultad crear(FacultadId id, NombreAcademico nombre, String descripcion,
                                 String ubicacion, String decano) {
        return new Facultad(id, nombre, descripcion, ubicacion, decano);
    }

    // Métodos de negocio

    /**
     * Actualiza la información básica de la facultad
     */
    public void actualizarInformacion(NombreAcademico nombre, String descripcion, String ubicacion) {
        this.setNombre(nombre);
        this.descripcion = descripcion;
        this.ubicacion = ubicacion;
    }

    /**
     * Cambia el decano de la facultad
     */
    public void cambiarDecano(String nuevoDecano) {
        if (nuevoDecano == null || nuevoDecano.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del decano no puede estar vacío");
        }
        this.decano = nuevoDecano.trim();
    }

    /**
     * Desactiva la facultad (no se puede eliminar físicamente si tiene carreras)
     */
    public void desactivar() {
        if (!this.activo) {
            throw new IllegalStateException("La facultad ya está desactivada");
        }
        this.activo = false;
    }

    /**
     * Reactiva la facultad
     */
    public void activar() {
        if (this.activo) {
            throw new IllegalStateException("La facultad ya está activa");
        }
        this.activo = true;
    }

    /**
     * Verifica si la facultad puede ser eliminada
     * (Esta lógica puede requerir verificar que no tenga carreras asociadas)
     */
    public boolean puedeSerEliminada() {
        // En una implementación completa, esto verificaría si tiene carreras
        return !this.activo;
    }

    // Getters

    public FacultadId getId() {
        return id;
    }

    public NombreAcademico getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public String getDecano() {
        return decano;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public boolean isActivo() {
        return activo;
    }

    // Setters privados con validación

    private void setNombre(NombreAcademico nombre) {
        this.nombre = Objects.requireNonNull(nombre, "El nombre de la facultad no puede ser nulo");
    }

    // Equals y HashCode basados en la identidad

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Facultad)) return false;
        Facultad facultad = (Facultad) o;
        return Objects.equals(id, facultad.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Facultad{" +
                "id=" + id +
                ", nombre=" + nombre +
                ", decano='" + decano + '\'' +
                ", activo=" + activo +
                '}';
    }
}
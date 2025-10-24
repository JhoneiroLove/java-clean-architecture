package com.university.cleanarchitecture.domain.model;

import com.university.cleanarchitecture.domain.model.valueobjects.CarreraId;
import com.university.cleanarchitecture.domain.model.valueobjects.Duracion;
import com.university.cleanarchitecture.domain.model.valueobjects.FacultadId;
import com.university.cleanarchitecture.domain.model.valueobjects.NombreAcademico;

import java.time.LocalDateTime;
import java.util.Objects;

public class Carrera {

    private final CarreraId id;
    private final FacultadId facultadId;
    private NombreAcademico nombre;
    private String descripcion;
    private Duracion duracion;
    private String tituloOtorgado;
    private final LocalDateTime fechaRegistro;
    private boolean activo;

    // Constructor completo (para reconstrucción desde persistencia)
    public Carrera(CarreraId id, FacultadId facultadId, NombreAcademico nombre,
                   String descripcion, Duracion duracion, String tituloOtorgado,
                   LocalDateTime fechaRegistro, boolean activo) {
        this.id = Objects.requireNonNull(id, "CarreraId no puede ser nulo");
        this.facultadId = Objects.requireNonNull(facultadId, "FacultadId no puede ser nulo");
        this.setNombre(nombre);
        this.descripcion = descripcion;
        this.setDuracion(duracion);
        this.setTituloOtorgado(tituloOtorgado);
        this.fechaRegistro = Objects.requireNonNull(fechaRegistro, "Fecha de registro no puede ser nula");
        this.activo = activo;
    }

    // Constructor para crear nueva carrera
    public Carrera(CarreraId id, FacultadId facultadId, NombreAcademico nombre,
                   String descripcion, Duracion duracion, String tituloOtorgado) {
        this(id, facultadId, nombre, descripcion, duracion, tituloOtorgado,
                LocalDateTime.now(), true);
    }

    /**
     * Factory method para crear una nueva carrera
     */
    public static Carrera crear(CarreraId id, FacultadId facultadId, NombreAcademico nombre,
                                String descripcion, Duracion duracion, String tituloOtorgado) {
        return new Carrera(id, facultadId, nombre, descripcion, duracion, tituloOtorgado);
    }

    // Métodos de negocio

    /**
     * Actualiza la información académica de la carrera
     */
    public void actualizarInformacionAcademica(NombreAcademico nombre, String descripcion,
                                               Duracion duracion, String tituloOtorgado) {
        this.setNombre(nombre);
        this.descripcion = descripcion;
        this.setDuracion(duracion);
        this.setTituloOtorgado(tituloOtorgado);
    }

    /**
     * Cambia la duración de la carrera
     * (esto puede requerir aprobación especial en un caso real)
     */
    public void cambiarDuracion(Duracion nuevaDuracion) {
        if (nuevaDuracion.equals(this.duracion)) {
            throw new IllegalArgumentException("La nueva duración es igual a la actual");
        }
        this.setDuracion(nuevaDuracion);
    }

    /**
     * Actualiza el título que otorga la carrera
     */
    public void actualizarTituloOtorgado(String nuevoTitulo) {
        this.setTituloOtorgado(nuevoTitulo);
    }

    /**
     * Desactiva la carrera (ya no acepta nuevos estudiantes)
     */
    public void desactivar() {
        if (!this.activo) {
            throw new IllegalStateException("La carrera ya está desactivada");
        }
        this.activo = false;
    }

    /**
     * Reactiva la carrera
     */
    public void activar() {
        if (this.activo) {
            throw new IllegalStateException("La carrera ya está activa");
        }
        this.activo = true;
    }

    /**
     * Verifica si la carrera es de duración estándar
     */
    public boolean esDuracionEstandar() {
        return duracion.isEstandar();
    }

    /**
     * Verifica si la carrera es corta
     */
    public boolean esCarreraCorta() {
        return duracion.isCorta();
    }

    /**
     * Verifica si la carrera es larga
     */
    public boolean esCarreraLarga() {
        return duracion.isLarga();
    }

    /**
     * Obtiene la duración en años
     */
    public int getDuracionEnAnios() {
        return duracion.getAnios();
    }

    /**
     * Verifica si la carrera puede ser eliminada
     * (Esta lógica puede requerir verificar que no tenga estudiantes matriculados)
     */
    public boolean puedeSerEliminada() {
        // En una implementación completa, esto verificaría si tiene estudiantes
        return !this.activo;
    }

    /**
     * Verifica si pertenece a una facultad específica
     */
    public boolean perteneceAFacultad(FacultadId facultadId) {
        return this.facultadId.equals(facultadId);
    }

    // Getters

    public CarreraId getId() {
        return id;
    }

    public FacultadId getFacultadId() {
        return facultadId;
    }

    public NombreAcademico getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public Duracion getDuracion() {
        return duracion;
    }

    public String getTituloOtorgado() {
        return tituloOtorgado;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public boolean isActivo() {
        return activo;
    }

    // Setters privados con validación

    private void setNombre(NombreAcademico nombre) {
        this.nombre = Objects.requireNonNull(nombre, "El nombre de la carrera no puede ser nulo");
    }

    private void setDuracion(Duracion duracion) {
        this.duracion = Objects.requireNonNull(duracion, "La duración no puede ser nula");
    }

    private void setTituloOtorgado(String tituloOtorgado) {
        if (tituloOtorgado == null || tituloOtorgado.trim().isEmpty()) {
            throw new IllegalArgumentException("El título otorgado no puede estar vacío");
        }
        if (tituloOtorgado.length() > 100) {
            throw new IllegalArgumentException("El título otorgado no puede exceder 100 caracteres");
        }
        this.tituloOtorgado = tituloOtorgado.trim();
    }

    // Equals y HashCode basados en la identidad

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Carrera)) return false;
        Carrera carrera = (Carrera) o;
        return Objects.equals(id, carrera.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Carrera{" +
                "id=" + id +
                ", nombre=" + nombre +
                ", duracion=" + duracion +
                ", tituloOtorgado='" + tituloOtorgado + '\'' +
                ", activo=" + activo +
                '}';
    }
}
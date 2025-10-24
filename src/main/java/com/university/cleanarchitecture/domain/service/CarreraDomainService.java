package com.university.cleanarchitecture.domain.service;

import com.university.cleanarchitecture.domain.exception.FacultadNotFoundException;
import com.university.cleanarchitecture.domain.model.Carrera;
import com.university.cleanarchitecture.domain.model.Facultad;
import com.university.cleanarchitecture.domain.model.valueobjects.FacultadId;
import com.university.cleanarchitecture.domain.model.valueobjects.NombreAcademico;
import com.university.cleanarchitecture.domain.repository.CarreraRepository;
import com.university.cleanarchitecture.domain.repository.FacultadRepository;

public class CarreraDomainService {

    private final CarreraRepository carreraRepository;
    private final FacultadRepository facultadRepository;

    public CarreraDomainService(CarreraRepository carreraRepository,
                                FacultadRepository facultadRepository) {
        this.carreraRepository = carreraRepository;
        this.facultadRepository = facultadRepository;
    }

    /**
     * Valida que una carrera puede ser creada en una facultad
     * Reglas:
     * - La facultad debe existir
     * - La facultad debe estar activa
     * - No debe existir otra carrera con el mismo nombre
     *
     * @param facultadId el ID de la facultad
     * @param nombreCarrera el nombre de la carrera
     * @throws FacultadNotFoundException si la facultad no existe
     * @throws IllegalStateException si la facultad no está activa
     * @throws IllegalArgumentException si ya existe una carrera con ese nombre
     */
    public void validarCreacionCarrera(FacultadId facultadId, NombreAcademico nombreCarrera) {
        // Verificar que la facultad existe
        Facultad facultad = facultadRepository.findById(facultadId)
                .orElseThrow(() -> new FacultadNotFoundException(facultadId.getValue()));

        // Verificar que la facultad está activa
        if (!facultad.isActivo()) {
            throw new IllegalStateException(
                    "No se puede crear una carrera en una facultad inactiva: " + facultad.getNombre()
            );
        }

        // Verificar que no existe otra carrera con el mismo nombre
        if (carreraRepository.existsByNombre(nombreCarrera)) {
            throw new IllegalArgumentException(
                    "Ya existe una carrera con el nombre: " + nombreCarrera.getValue()
            );
        }
    }

    /**
     * Valida que una facultad puede ser desactivada
     * Regla: Una facultad no puede ser desactivada si tiene carreras activas
     *
     * @param facultadId el ID de la facultad
     * @return true si puede ser desactivada
     */
    public boolean puedeDesactivarFacultad(FacultadId facultadId) {
        long carrerasActivas = carreraRepository.countActiveByFacultadId(facultadId);
        return carrerasActivas == 0;
    }

    /**
     * Verifica si una facultad puede ser eliminada
     * Regla: Una facultad solo puede ser eliminada si no tiene ninguna carrera asociada
     *
     * @param facultadId el ID de la facultad
     * @return true si puede ser eliminada
     */
    public boolean puedeEliminarFacultad(FacultadId facultadId) {
        long carrerasAsociadas = facultadRepository.countCarrerasByFacultadId(facultadId);
        return carrerasAsociadas == 0;
    }

    /**
     * Obtiene el número de carreras activas en una facultad
     *
     * @param facultadId el ID de la facultad
     * @return número de carreras activas
     */
    public long contarCarrerasActivasPorFacultad(FacultadId facultadId) {
        return carreraRepository.countActiveByFacultadId(facultadId);
    }

    /**
     * Valida si una carrera puede cambiar de facultad
     * (Esta operación podría no estar permitida dependiendo de las reglas de negocio)
     *
     * @param carrera la carrera
     * @param nuevaFacultadId el ID de la nueva facultad
     * @return true si puede cambiar de facultad
     */
    public boolean puedeCambiarFacultad(Carrera carrera, FacultadId nuevaFacultadId) {
        // Verificar que la nueva facultad existe y está activa
        Facultad nuevaFacultad = facultadRepository.findById(nuevaFacultadId)
                .orElseThrow(() -> new FacultadNotFoundException(nuevaFacultadId.getValue()));

        if (!nuevaFacultad.isActivo()) {
            return false;
        }

        // La carrera solo puede cambiar si está inactiva
        // (para evitar problemas con estudiantes matriculados)
        return !carrera.isActivo();
    }

    /**
     * Valida que una carrera puede ser eliminada
     * Regla: Una carrera solo puede ser eliminada si está inactiva
     * (en un sistema real, también verificaríamos que no tenga estudiantes)
     *
     * @param carrera la carrera a validar
     * @return true si puede ser eliminada
     */
    public boolean puedeEliminarCarrera(Carrera carrera) {
        return !carrera.isActivo();
    }
}



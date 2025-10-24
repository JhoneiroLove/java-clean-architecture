package com.university.cleanarchitecture.domain.repository;

import com.university.cleanarchitecture.domain.model.Facultad;
import com.university.cleanarchitecture.domain.model.valueobjects.FacultadId;
import com.university.cleanarchitecture.domain.model.valueobjects.NombreAcademico;

import java.util.List;
import java.util.Optional;

public interface FacultadRepository {

    /**
     * Guarda una facultad (crear o actualizar)
     *
     * @param facultad la facultad a guardar
     * @return la facultad guardada
     */
    Facultad save(Facultad facultad);

    /**
     * Busca una facultad por su ID
     *
     * @param id el identificador de la facultad
     * @return Optional con la facultad si existe
     */
    Optional<Facultad> findById(FacultadId id);

    /**
     * Busca una facultad por su nombre
     *
     * @param nombre el nombre de la facultad
     * @return Optional con la facultad si existe
     */
    Optional<Facultad> findByNombre(NombreAcademico nombre);

    /**
     * Obtiene todas las facultades activas
     *
     * @return lista de facultades activas
     */
    List<Facultad> findAllActive();

    /**
     * Obtiene todas las facultades (activas e inactivas)
     *
     * @return lista de todas las facultades
     */
    List<Facultad> findAll();

    /**
     * Busca facultades por ubicación
     *
     * @param ubicacion la ubicación a buscar
     * @return lista de facultades en esa ubicación
     */
    List<Facultad> findByUbicacion(String ubicacion);

    /**
     * Verifica si existe una facultad con el nombre dado
     *
     * @param nombre el nombre a verificar
     * @return true si existe una facultad con ese nombre
     */
    boolean existsByNombre(NombreAcademico nombre);

    /**
     * Verifica si existe una facultad con el nombre dado, excluyendo un ID específico
     * (útil para actualizaciones)
     *
     * @param nombre el nombre a verificar
     * @param excludeId el ID a excluir de la búsqueda
     * @return true si existe otra facultad con ese nombre
     */
    boolean existsByNombreAndIdNot(NombreAcademico nombre, FacultadId excludeId);

    /**
     * Cuenta el número de carreras asociadas a una facultad
     *
     * @param facultadId el ID de la facultad
     * @return número de carreras asociadas
     */
    long countCarrerasByFacultadId(FacultadId facultadId);

    /**
     * Elimina una facultad por su ID
     * (solo si no tiene carreras asociadas)
     *
     * @param id el ID de la facultad a eliminar
     */
    void deleteById(FacultadId id);
}



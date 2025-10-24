package com.university.cleanarchitecture.domain.repository;

import com.university.cleanarchitecture.domain.model.Carrera;
import com.university.cleanarchitecture.domain.model.valueobjects.CarreraId;
import com.university.cleanarchitecture.domain.model.valueobjects.Duracion;
import com.university.cleanarchitecture.domain.model.valueobjects.FacultadId;
import com.university.cleanarchitecture.domain.model.valueobjects.NombreAcademico;

import java.util.List;
import java.util.Optional;

public interface CarreraRepository {

    /**
     * Guarda una carrera (crear o actualizar)
     *
     * @param carrera la carrera a guardar
     * @return la carrera guardada
     */
    Carrera save(Carrera carrera);

    /**
     * Busca una carrera por su ID
     *
     * @param id el identificador de la carrera
     * @return Optional con la carrera si existe
     */
    Optional<Carrera> findById(CarreraId id);

    /**
     * Busca una carrera por su nombre
     *
     * @param nombre el nombre de la carrera
     * @return Optional con la carrera si existe
     */
    Optional<Carrera> findByNombre(NombreAcademico nombre);

    /**
     * Obtiene todas las carreras de una facultad
     *
     * @param facultadId el ID de la facultad
     * @return lista de carreras de la facultad
     */
    List<Carrera> findByFacultadId(FacultadId facultadId);

    /**
     * Obtiene todas las carreras activas de una facultad
     *
     * @param facultadId el ID de la facultad
     * @return lista de carreras activas de la facultad
     */
    List<Carrera> findActiveByFacultadId(FacultadId facultadId);

    /**
     * Obtiene todas las carreras activas
     *
     * @return lista de carreras activas
     */
    List<Carrera> findAllActive();

    /**
     * Obtiene todas las carreras (activas e inactivas)
     *
     * @return lista de todas las carreras
     */
    List<Carrera> findAll();

    /**
     * Busca carreras por duración
     *
     * @param duracion la duración en semestres
     * @return lista de carreras con esa duración
     */
    List<Carrera> findByDuracion(Duracion duracion);

    /**
     * Busca carreras cortas (menos de 10 semestres)
     *
     * @return lista de carreras cortas
     */
    List<Carrera> findCarrerasCortas();

    /**
     * Busca carreras largas (más de 10 semestres)
     *
     * @return lista de carreras largas
     */
    List<Carrera> findCarrerasLargas();

    /**
     * Busca carreras por título otorgado
     *
     * @param tituloOtorgado el título a buscar (parcial o completo)
     * @return lista de carreras que otorgan ese título
     */
    List<Carrera> findByTituloOtorgadoContaining(String tituloOtorgado);

    /**
     * Verifica si existe una carrera con el nombre dado
     *
     * @param nombre el nombre a verificar
     * @return true si existe una carrera con ese nombre
     */
    boolean existsByNombre(NombreAcademico nombre);

    /**
     * Verifica si existe una carrera con el nombre dado, excluyendo un ID específico
     * (útil para actualizaciones)
     *
     * @param nombre el nombre a verificar
     * @param excludeId el ID a excluir de la búsqueda
     * @return true si existe otra carrera con ese nombre
     */
    boolean existsByNombreAndIdNot(NombreAcademico nombre, CarreraId excludeId);

    /**
     * Cuenta el número de carreras activas en una facultad
     *
     * @param facultadId el ID de la facultad
     * @return número de carreras activas
     */
    long countActiveByFacultadId(FacultadId facultadId);

    /**
     * Elimina una carrera por su ID
     * (solo si no tiene estudiantes matriculados)
     *
     * @param id el ID de la carrera a eliminar
     */
    void deleteById(CarreraId id);
}



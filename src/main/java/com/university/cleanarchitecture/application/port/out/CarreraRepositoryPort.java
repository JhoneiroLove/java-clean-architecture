package com.university.cleanarchitecture.application.port.out;

import com.university.cleanarchitecture.domain.model.Carrera;
import com.university.cleanarchitecture.domain.model.valueobjects.CarreraId;
import com.university.cleanarchitecture.domain.model.valueobjects.Duracion;
import com.university.cleanarchitecture.domain.model.valueobjects.FacultadId;
import com.university.cleanarchitecture.domain.model.valueobjects.NombreAcademico;
import com.university.cleanarchitecture.domain.repository.CarreraRepository;

import java.util.List;
import java.util.Optional;

/**
 * Puerto de salida (Output Port) para persistencia de Carreras.
 *
 * Define el contrato que los adaptadores de persistencia deben implementar
 * para que la Application Layer pueda persistir carreras.
 *
 * EXTIENDE la interfaz del dominio (CarreraRepository) para mantener
 * compatibilidad con el Domain Service que usa la interfaz del dominio.
 *
 * Patrón: Hexagonal Architecture / Ports & Adapters
 */
public interface CarreraRepositoryPort extends CarreraRepository {
    // Hereda todos los métodos de CarreraRepository del dominio:
    // - save(Carrera)
    // - findById(CarreraId)
    // - findByNombre(NombreAcademico)
    // - findByFacultadId(FacultadId)
    // - findActiveByFacultadId(FacultadId)
    // - findAllActive()
    // - findAll()
    // - findByDuracion(Duracion)
    // - existsByNombre(NombreAcademico)
    // - existsByNombreAndIdNot(NombreAcademico, CarreraId)
    // - countActiveByFacultadId(FacultadId)
    // - deleteById(CarreraId)

    /**
     * Método adicional específico de la aplicación:
     * Busca carreras dentro de un rango de duración.
     *
     * @param minSemestres duración mínima (inclusive)
     * @param maxSemestres duración máxima (inclusive)
     * @return lista de carreras dentro del rango
     */
    List<Carrera> findByDuracionBetween(int minSemestres, int maxSemestres);
}

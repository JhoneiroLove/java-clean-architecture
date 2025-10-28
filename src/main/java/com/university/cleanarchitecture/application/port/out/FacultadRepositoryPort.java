package com.university.cleanarchitecture.application.port.out;

import com.university.cleanarchitecture.domain.repository.FacultadRepository;

/**
 * Puerto de salida (Output Port) para persistencia de Facultades.
 *
 * Define el contrato que los adaptadores de persistencia (JPA, MongoDB, etc.)
 * deben implementar para que la Application Layer pueda persistir facultades.
 *
 * EXTIENDE la interfaz del dominio (FacultadRepository) para mantener
 * compatibilidad con el Domain Service que usa la interfaz del dominio.
 *
 * Esto respeta Clean Architecture: el dominio define el contrato,
 * la aplicación puede extenderlo si necesita métodos adicionales.
 *
 * Patrón: Hexagonal Architecture / Ports & Adapters
 */
public interface FacultadRepositoryPort extends FacultadRepository {
    // Hereda todos los métodos de FacultadRepository del dominio:
    // - save(Facultad)
    // - findById(FacultadId)
    // - findByNombre(NombreAcademico)
    // - findAllActive()
    // - findAll()
    // - existsByNombre(NombreAcademico)
    // - existsByNombreAndIdNot(NombreAcademico, FacultadId)
    // - countCarrerasByFacultadId(FacultadId)
    // - deleteById(FacultadId)

    // Aquí se pueden agregar métodos adicionales específicos de la aplicación si se necesitan.
    // Por ejemplo:
    // List<Facultad> findByUbicacion(String ubicacion);
}

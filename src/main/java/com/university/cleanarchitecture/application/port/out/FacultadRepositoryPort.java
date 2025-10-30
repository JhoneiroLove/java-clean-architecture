package com.university.cleanarchitecture.application.port.out;

import com.university.cleanarchitecture.domain.model.Facultad;
import com.university.cleanarchitecture.domain.model.valueobjects.FacultadId;
import com.university.cleanarchitecture.domain.model.valueobjects.NombreAcademico;
import java.util.List;
import java.util.Optional;

public interface FacultadRepositoryPort {
    Facultad save(Facultad facultad);
    Optional<Facultad> findById(FacultadId id);
    Optional<Facultad> findByNombre(NombreAcademico nombre);
    List<Facultad> findAllActive();
    List<Facultad> findAll();
    boolean existsByNombre(NombreAcademico nombre);
    boolean existsByNombreAndIdNot(NombreAcademico nombre, FacultadId id);
    int countCarrerasByFacultadId(FacultadId facultadId);
    void deleteById(FacultadId id);
}


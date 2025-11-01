package com.university.cleanarchitecture.application.port.out;

import com.university.cleanarchitecture.domain.model.Carrera;
import com.university.cleanarchitecture.domain.model.valueobjects.CarreraId;
import com.university.cleanarchitecture.domain.model.valueobjects.FacultadId;
import com.university.cleanarchitecture.domain.model.valueobjects.NombreAcademico;
import java.util.List;
import java.util.Optional;

public interface CarreraRepositoryPort {
    Carrera save(Carrera carrera);
    Optional<Carrera> findById(CarreraId id);
    Optional<Carrera> findByNombre(NombreAcademico nombre);
    List<Carrera> findByFacultadId(FacultadId facultadId);
    List<Carrera> findActiveByFacultadId(FacultadId facultadId);
    List<Carrera> findByDuracionSemestres(int semestres);
    List<Carrera> findByDuracionBetween(int minSemestres, int maxSemestres);
    List<Carrera> findAllActive();
    List<Carrera> findAll();
    boolean existsByNombre(NombreAcademico nombre);
    boolean existsByNombreAndIdNot(NombreAcademico nombre, CarreraId id);
    int countActiveByFacultadId(FacultadId facultadId);
    void deleteById(CarreraId id);
}




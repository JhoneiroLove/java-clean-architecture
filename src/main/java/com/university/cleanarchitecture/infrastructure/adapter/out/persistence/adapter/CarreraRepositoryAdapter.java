package com.university.cleanarchitecture.infrastructure.adapter.out.persistence.adapter;

import com.university.cleanarchitecture.domain.model.Carrera;
import com.university.cleanarchitecture.domain.model.valueobjects.CarreraId;
import com.university.cleanarchitecture.domain.model.valueobjects.Duracion;
import com.university.cleanarchitecture.domain.model.valueobjects.FacultadId;
import com.university.cleanarchitecture.domain.model.valueobjects.NombreAcademico;
import com.university.cleanarchitecture.domain.repository.CarreraRepository;
import com.university.cleanarchitecture.infrastructure.adapter.out.persistence.jpa.entity.CarreraJpaEntity;
import com.university.cleanarchitecture.infrastructure.adapter.out.persistence.jpa.repository.CarreraJpaRepository;
import com.university.cleanarchitecture.infrastructure.adapter.out.persistence.mapper.CarreraJpaMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class CarreraRepositoryAdapter implements CarreraRepository {

    private final CarreraJpaRepository jpaRepository;
    private final CarreraJpaMapper mapper;

    public CarreraRepositoryAdapter(CarreraJpaRepository jpaRepository, CarreraJpaMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Carrera save(Carrera carrera) {
        CarreraJpaEntity entity = mapper.toJpaEntity(carrera);
        CarreraJpaEntity savedEntity = jpaRepository.save(entity);
        return mapper.toDomainModel(savedEntity);
    }

    @Override
    public Optional<Carrera> findById(CarreraId id) {
        return jpaRepository.findById(id.getValue())
                .map(mapper::toDomainModel);
    }

    @Override
    public Optional<Carrera> findByNombre(NombreAcademico nombre) {
        return jpaRepository.findByNombre(nombre.getValue())
                .map(mapper::toDomainModel);
    }

    @Override
    public List<Carrera> findByFacultadId(FacultadId facultadId) {
        return jpaRepository.findByFacultadId(facultadId.getValue())
                .stream()
                .map(mapper::toDomainModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<Carrera> findActiveByFacultadId(FacultadId facultadId) {
        return jpaRepository.findActiveByFacultadId(facultadId.getValue())
                .stream()
                .map(mapper::toDomainModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<Carrera> findAllActive() {
        return jpaRepository.findByActivoTrue()
                .stream()
                .map(mapper::toDomainModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<Carrera> findAll() {
        return jpaRepository.findAll()
                .stream()
                .map(mapper::toDomainModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<Carrera> findByDuracion(Duracion duracion) {
        return jpaRepository.findByDuracionSemestres(duracion.getSemestres())
                .stream()
                .map(mapper::toDomainModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<Carrera> findCarrerasCortas() {
        return jpaRepository.findCarrerasCortas()
                .stream()
                .map(mapper::toDomainModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<Carrera> findCarrerasLargas() {
        return jpaRepository.findCarrerasLargas()
                .stream()
                .map(mapper::toDomainModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<Carrera> findByTituloOtorgadoContaining(String tituloOtorgado) {
        return jpaRepository.findByTituloOtorgadoContaining(tituloOtorgado)
                .stream()
                .map(mapper::toDomainModel)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsByNombre(NombreAcademico nombre) {
        return jpaRepository.existsByNombre(nombre.getValue());
    }

    @Override
    public boolean existsByNombreAndIdNot(NombreAcademico nombre, CarreraId excludeId) {
        return jpaRepository.existsByNombreAndIdNot(nombre.getValue(), excludeId.getValue());
    }

    @Override
    public long countActiveByFacultadId(FacultadId facultadId) {
        return jpaRepository.countActiveByFacultadId(facultadId.getValue());
    }

    @Override
    public void deleteById(CarreraId id) {
        jpaRepository.deleteById(id.getValue());
    }
}


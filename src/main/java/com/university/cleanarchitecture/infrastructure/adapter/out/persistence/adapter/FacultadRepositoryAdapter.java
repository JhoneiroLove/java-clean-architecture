package com.university.cleanarchitecture.infrastructure.adapter.out.persistence.adapter;

import com.university.cleanarchitecture.domain.model.Facultad;
import com.university.cleanarchitecture.domain.model.valueobjects.FacultadId;
import com.university.cleanarchitecture.domain.model.valueobjects.NombreAcademico;
import com.university.cleanarchitecture.domain.repository.FacultadRepository;
import com.university.cleanarchitecture.infrastructure.adapter.out.persistence.jpa.entity.FacultadJpaEntity;
import com.university.cleanarchitecture.infrastructure.adapter.out.persistence.jpa.repository.FacultadJpaRepository;
import com.university.cleanarchitecture.infrastructure.adapter.out.persistence.mapper.FacultadJpaMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class FacultadRepositoryAdapter implements FacultadRepository {

    private final FacultadJpaRepository jpaRepository;
    private final FacultadJpaMapper mapper;

    public FacultadRepositoryAdapter(FacultadJpaRepository jpaRepository,
                                     FacultadJpaMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Facultad save(Facultad facultad) {
        FacultadJpaEntity entity = mapper.toJpaEntity(facultad);
        FacultadJpaEntity savedEntity = jpaRepository.save(entity);
        return mapper.toDomainModel(savedEntity);
    }

    @Override
    public Optional<Facultad> findById(FacultadId id) {
        return jpaRepository.findById(id.getValue())
                .map(mapper::toDomainModel);
    }

    @Override
    public Optional<Facultad> findByNombre(NombreAcademico nombre) {
        return jpaRepository.findByNombre(nombre.getValue())
                .map(mapper::toDomainModel);
    }

    @Override
    public List<Facultad> findAllActive() {
        return jpaRepository.findByActivoTrue()
                .stream()
                .map(mapper::toDomainModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<Facultad> findAll() {
        return jpaRepository.findAll()
                .stream()
                .map(mapper::toDomainModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<Facultad> findByUbicacion(String ubicacion) {
        return jpaRepository.findByUbicacion(ubicacion)
                .stream()
                .map(mapper::toDomainModel)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsByNombre(NombreAcademico nombre) {
        return jpaRepository.existsByNombre(nombre.getValue());
    }

    @Override
    public boolean existsByNombreAndIdNot(NombreAcademico nombre, FacultadId excludeId) {
        return jpaRepository.existsByNombreAndIdNot(nombre.getValue(), excludeId.getValue());
    }

    @Override
    public long countCarrerasByFacultadId(FacultadId facultadId) {
        return jpaRepository.countCarrerasByFacultadId(facultadId.getValue());
    }

    @Override
    public void deleteById(FacultadId id) {
        jpaRepository.deleteById(id.getValue());
    }
}


package com.university.cleanarchitecture.infrastructure.adapter.out.persistence.mapper;

import com.university.cleanarchitecture.domain.model.Facultad;
import com.university.cleanarchitecture.domain.model.valueobjects.FacultadId;
import com.university.cleanarchitecture.domain.model.valueobjects.NombreAcademico;
import com.university.cleanarchitecture.infrastructure.adapter.out.persistence.jpa.entity.FacultadJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class FacultadJpaMapper {

    public FacultadJpaEntity toJpaEntity(Facultad facultad) {
        FacultadJpaEntity entity = new FacultadJpaEntity();
        entity.setId(facultad.getId().getValue());
        entity.setNombre(facultad.getNombre().getValue());
        entity.setDescripcion(facultad.getDescripcion());
        entity.setUbicacion(facultad.getUbicacion());
        entity.setDecano(facultad.getDecano());
        entity.setFechaRegistro(facultad.getFechaRegistro());
        entity.setActivo(facultad.isActivo());
        return entity;
    }

    public Facultad toDomainModel(FacultadJpaEntity entity) {
        return new Facultad(
                new FacultadId(entity.getId()),
                new NombreAcademico(entity.getNombre()),
                entity.getDescripcion(),
                entity.getUbicacion(),
                entity.getDecano(),
                entity.getFechaRegistro(),
                entity.getActivo()
        );
    }
}


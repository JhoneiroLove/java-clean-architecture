package com.university.cleanarchitecture.infrastructure.adapter.out.persistence.mapper;

import com.university.cleanarchitecture.domain.model.Carrera;
import com.university.cleanarchitecture.domain.model.valueobjects.CarreraId;
import com.university.cleanarchitecture.domain.model.valueobjects.Duracion;
import com.university.cleanarchitecture.domain.model.valueobjects.FacultadId;
import com.university.cleanarchitecture.domain.model.valueobjects.NombreAcademico;
import com.university.cleanarchitecture.infrastructure.adapter.out.persistence.jpa.entity.CarreraJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class CarreraJpaMapper {

    public CarreraJpaEntity toJpaEntity(Carrera carrera) {
        CarreraJpaEntity entity = new CarreraJpaEntity();
        entity.setId(carrera.getId().getValue());
        entity.setFacultadId(carrera.getFacultadId().getValue());
        entity.setNombre(carrera.getNombre().getValue());
        entity.setDescripcion(carrera.getDescripcion());
        entity.setDuracionSemestres(carrera.getDuracion().getSemestres());
        entity.setTituloOtorgado(carrera.getTituloOtorgado());
        entity.setFechaRegistro(carrera.getFechaRegistro());
        entity.setActivo(carrera.isActivo());

        return entity;
    }

    public Carrera toDomainModel(CarreraJpaEntity entity) {
        return new Carrera(
                new CarreraId(entity.getId()),
                new FacultadId(entity.getFacultadId()),
                new NombreAcademico(entity.getNombre()),
                entity.getDescripcion(),
                new Duracion(entity.getDuracionSemestres()),
                entity.getTituloOtorgado(),
                entity.getFechaRegistro(),
                entity.getActivo()
        );
    }
}

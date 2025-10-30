package com.university.cleanarchitecture.application.mapper;

import com.university.cleanarchitecture.application.dto.response.CarreraResponse;
import com.university.cleanarchitecture.application.dto.response.CarreraSummaryResponse;
import com.university.cleanarchitecture.domain.model.Carrera;
import org.springframework.stereotype.Component;

@Component
public class CarreraMapper {

    public CarreraResponse toResponse(Carrera carrera, String facultadNombre) {
        CarreraResponse response = new CarreraResponse();
        response.setId(carrera.getId().getValue());
        response.setNombre(carrera.getNombre().getValue());
        response.setDescripcion(carrera.getDescripcion());
        response.setDuracionSemestres(carrera.getDuracion().getSemestres());
        response.setDuracionAnios(carrera.getDuracion().getAnios());
        response.setTituloOtorgado(carrera.getTituloOtorgado());
        response.setFechaRegistro(carrera.getFechaRegistro());
        response.setActivo(carrera.isActivo());
        response.setFacultadId(carrera.getFacultadId().getValue());
        response.setFacultadNombre(facultadNombre);
        response.setClasificacion(determinarClasificacion(carrera.getDuracion().getSemestres()));
        return response;
    }

    public CarreraSummaryResponse toSummaryResponse(Carrera carrera, String facultadNombre) {
        CarreraSummaryResponse response = new CarreraSummaryResponse();
        response.setId(carrera.getId().getValue());
        response.setNombre(carrera.getNombre().getValue());
        response.setDuracionSemestres(carrera.getDuracion().getSemestres());
        response.setTituloOtorgado(carrera.getTituloOtorgado());
        response.setActivo(carrera.isActivo());
        response.setFacultadId(carrera.getFacultadId().getValue());
        response.setFacultadNombre(facultadNombre);
        return response;
    }

    private String determinarClasificacion(int semestres) {
        if (semestres < 10) return "CORTA";
        if (semestres == 10) return "ESTANDAR";
        return "LARGA";
    }
}

package com.university.cleanarchitecture.application.mapper;

import com.university.cleanarchitecture.application.dto.response.FacultadResponse;
import com.university.cleanarchitecture.application.dto.response.FacultadSummaryResponse;
import com.university.cleanarchitecture.domain.model.Facultad;
import org.springframework.stereotype.Component;

@Component
public class FacultadMapper {

    public FacultadResponse toResponse(Facultad facultad, int cantidadCarreras) {
        FacultadResponse response = new FacultadResponse();
        response.setId(facultad.getId().getValue());
        response.setNombre(facultad.getNombre().getValue());
        response.setDescripcion(facultad.getDescripcion());
        response.setUbicacion(facultad.getUbicacion());
        response.setDecano(facultad.getDecano());
        response.setFechaRegistro(facultad.getFechaRegistro());
        response.setActivo(facultad.isActivo());
        response.setCantidadCarreras(cantidadCarreras);
        return response;
    }

    public FacultadSummaryResponse toSummaryResponse(Facultad facultad, int cantidadCarreras) {
        FacultadSummaryResponse response = new FacultadSummaryResponse();
        response.setId(facultad.getId().getValue());
        response.setNombre(facultad.getNombre().getValue());
        response.setUbicacion(facultad.getUbicacion());
        response.setDecano(facultad.getDecano());
        response.setActivo(facultad.isActivo());
        response.setCantidadCarreras(cantidadCarreras);
        return response;
    }
}

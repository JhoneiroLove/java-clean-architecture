package com.university.cleanarchitecture.application.port.in;

import com.university.cleanarchitecture.application.dto.query.FindFacultadByNombreQuery;
import com.university.cleanarchitecture.application.dto.response.FacultadResponse;

public interface FindFacultadByNombreUseCase {
    FacultadResponse findByNombre(FindFacultadByNombreQuery query);
}


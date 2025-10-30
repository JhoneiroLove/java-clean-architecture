package com.university.cleanarchitecture.application.port.in;

import com.university.cleanarchitecture.application.dto.query.FindFacultadByIdQuery;
import com.university.cleanarchitecture.application.dto.response.FacultadResponse;

public interface FindFacultadByIdUseCase {
    FacultadResponse findById(FindFacultadByIdQuery query);
}


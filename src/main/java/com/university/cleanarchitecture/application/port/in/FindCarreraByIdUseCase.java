package com.university.cleanarchitecture.application.port.in;

import com.university.cleanarchitecture.application.dto.query.FindCarreraByIdQuery;
import com.university.cleanarchitecture.application.dto.response.CarreraResponse;

public interface FindCarreraByIdUseCase {
    CarreraResponse findById(FindCarreraByIdQuery query);
}


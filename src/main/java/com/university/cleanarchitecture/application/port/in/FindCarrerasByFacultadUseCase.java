package com.university.cleanarchitecture.application.port.in;

import com.university.cleanarchitecture.application.dto.query.FindCarrerasByFacultadQuery;
import com.university.cleanarchitecture.application.dto.response.CarreraSummaryResponse;
import java.util.List;

public interface FindCarrerasByFacultadUseCase {
    List<CarreraSummaryResponse> findByFacultad(FindCarrerasByFacultadQuery query);
}


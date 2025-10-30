package com.university.cleanarchitecture.application.port.in;

import com.university.cleanarchitecture.application.dto.query.FindCarrerasByDuracionQuery;
import com.university.cleanarchitecture.application.dto.response.CarreraSummaryResponse;
import java.util.List;

public interface FindCarrerasByDuracionUseCase {
    List<CarreraSummaryResponse> findByDuracion(FindCarrerasByDuracionQuery query);
}


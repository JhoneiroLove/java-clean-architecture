package com.university.cleanarchitecture.application.port.in;

import com.university.cleanarchitecture.application.dto.query.FindAllFacultadesQuery;
import com.university.cleanarchitecture.application.dto.response.FacultadSummaryResponse;
import java.util.List;

public interface FindAllFacultadesUseCase {
    List<FacultadSummaryResponse> findAll(FindAllFacultadesQuery query);
}


package com.university.cleanarchitecture.application.port.in;

import com.university.cleanarchitecture.application.dto.command.UpdateFacultadCommand;
import com.university.cleanarchitecture.application.dto.response.FacultadResponse;

public interface UpdateFacultadUseCase {
    FacultadResponse update(UpdateFacultadCommand command);
}


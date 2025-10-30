package com.university.cleanarchitecture.application.port.in;

import com.university.cleanarchitecture.application.dto.command.RegisterFacultadCommand;
import com.university.cleanarchitecture.application.dto.response.FacultadResponse;

public interface RegisterFacultadUseCase {
    FacultadResponse register(RegisterFacultadCommand command);
}


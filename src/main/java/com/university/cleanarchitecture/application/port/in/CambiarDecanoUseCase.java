package com.university.cleanarchitecture.application.port.in;

import com.university.cleanarchitecture.application.dto.command.CambiarDecanoCommand;
import com.university.cleanarchitecture.application.dto.response.FacultadResponse;

public interface CambiarDecanoUseCase {
    FacultadResponse cambiarDecano(CambiarDecanoCommand command);
}


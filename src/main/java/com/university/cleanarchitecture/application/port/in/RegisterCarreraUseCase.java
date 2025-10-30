package com.university.cleanarchitecture.application.port.in;

import com.university.cleanarchitecture.application.dto.command.RegisterCarreraCommand;
import com.university.cleanarchitecture.application.dto.response.CarreraResponse;

public interface RegisterCarreraUseCase {
    CarreraResponse register(RegisterCarreraCommand command);
}


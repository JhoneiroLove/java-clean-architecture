package com.university.cleanarchitecture.application.port.in;

import com.university.cleanarchitecture.application.dto.command.UpdateCarreraCommand;
import com.university.cleanarchitecture.application.dto.response.CarreraResponse;

public interface UpdateCarreraUseCase {
    CarreraResponse update(UpdateCarreraCommand command);
}


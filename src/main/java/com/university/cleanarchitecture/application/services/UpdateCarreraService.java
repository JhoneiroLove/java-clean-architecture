package com.university.cleanarchitecture.application.services;

import com.university.cleanarchitecture.application.dto.command.UpdateCarreraCommand;
import com.university.cleanarchitecture.application.dto.response.CarreraResponse;
import com.university.cleanarchitecture.application.mapper.CarreraMapper;
import com.university.cleanarchitecture.application.port.in.carrera.UpdateCarreraUseCase;
import com.university.cleanarchitecture.application.port.out.CarreraRepositoryPort;
import com.university.cleanarchitecture.application.port.out.FacultadRepositoryPort;
import com.university.cleanarchitecture.domain.exception.CarreraNotFoundException;
import com.university.cleanarchitecture.domain.model.Carrera;
import com.university.cleanarchitecture.domain.model.Facultad;
import com.university.cleanarchitecture.domain.model.valueobjects.CarreraId;
import com.university.cleanarchitecture.domain.model.valueobjects.NombreAcademico;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementación del caso de uso: Actualizar Carrera.
 *
 * Responsabilidades:
 * - Orquestar la actualización de una carrera existente
 * - Validar que no exista duplicidad de nombres
 * - Aplicar cambios y persistir
 */
@Service
@Transactional
public class UpdateCarreraService implements UpdateCarreraUseCase {

    private final CarreraRepositoryPort carreraRepository;
    private final FacultadRepositoryPort facultadRepository;

    public UpdateCarreraService(
            CarreraRepositoryPort carreraRepository,
            FacultadRepositoryPort facultadRepository) {
        this.carreraRepository = carreraRepository;
        this.facultadRepository = facultadRepository;
    }

    @Override
    public CarreraResponse update(UpdateCarreraCommand command) {
        // 1. Validar comando
        if (command == null) {
            throw new IllegalArgumentException("El comando no puede ser nulo");
        }

        // 2. Buscar la carrera existente
        CarreraId carreraId = CarreraId.of(command.carreraId());
        Carrera carrera = carreraRepository.findById(carreraId)
                .orElseThrow(() -> new CarreraNotFoundException(command.carreraId()));

        // 3. Validar que el nuevo nombre no exista en otra carrera
        NombreAcademico nuevoNombre = NombreAcademico.of(command.nombre());
        if (carreraRepository.existsByNombreAndIdNot(nuevoNombre, carreraId)) {
            throw new IllegalArgumentException(
                    "Ya existe otra carrera con el nombre: " + nuevoNombre.getValue()
            );
        }

        // 4. Aplicar cambios usando el Mapper
        // El mapper delega al método de dominio que contiene las validaciones
        CarreraMapper.applyUpdateCommand(command, carrera);

        // 5. Persistir los cambios
        Carrera updatedCarrera = carreraRepository.save(carrera);

        // 6. Obtener nombre de la facultad
        Facultad facultad = facultadRepository.findById(updatedCarrera.getFacultadId())
                .orElseThrow(); // No debería fallar
        String facultadNombre = facultad.getNombre().getValue();

        // 7. Convertir a Response
        return CarreraMapper.toResponse(updatedCarrera, facultadNombre);
    }
}

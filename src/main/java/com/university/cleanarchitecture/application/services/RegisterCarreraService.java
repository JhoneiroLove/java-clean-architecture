package com.university.cleanarchitecture.application.services;

import com.university.cleanarchitecture.application.dto.command.RegisterCarreraCommand;
import com.university.cleanarchitecture.application.dto.response.CarreraResponse;
import com.university.cleanarchitecture.application.mapper.CarreraMapper;
import com.university.cleanarchitecture.application.port.in.carrera.RegisterCarreraUseCase;
import com.university.cleanarchitecture.application.port.out.CarreraRepositoryPort;
import com.university.cleanarchitecture.application.port.out.FacultadRepositoryPort;
import com.university.cleanarchitecture.domain.model.Carrera;
import com.university.cleanarchitecture.domain.model.Facultad;
import com.university.cleanarchitecture.domain.model.valueobjects.CarreraId;
import com.university.cleanarchitecture.domain.model.valueobjects.FacultadId;
import com.university.cleanarchitecture.domain.model.valueobjects.NombreAcademico;
import com.university.cleanarchitecture.domain.service.CarreraDomainService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Implementación del caso de uso: Registrar Carrera.
 *
 * Responsabilidades:
 * - Orquestar la creación de una carrera
 * - Validar reglas de negocio con Domain Service
 * - Coordinar entre mappers, repositorios y dominio
 */
@Service
@Transactional
public class RegisterCarreraService implements RegisterCarreraUseCase {

    private final CarreraRepositoryPort carreraRepository;
    private final FacultadRepositoryPort facultadRepository;
    private final CarreraDomainService domainService;

    // Simulador de generación de IDs
    private static final AtomicLong ID_GENERATOR = new AtomicLong(1);

    public RegisterCarreraService(
            CarreraRepositoryPort carreraRepository,
            FacultadRepositoryPort facultadRepository,
            CarreraDomainService domainService) {
        this.carreraRepository = carreraRepository;
        this.facultadRepository = facultadRepository;
        this.domainService = domainService;
    }

    @Override
    public CarreraResponse register(RegisterCarreraCommand command) {
        // 1. Validar comando
        if (command == null) {
            throw new IllegalArgumentException("El comando no puede ser nulo");
        }

        // 2. Convertir IDs a Value Objects
        FacultadId facultadId = FacultadId.of(command.facultadId());
        NombreAcademico nombre = NombreAcademico.of(command.nombre());

        // 3. Validar reglas de negocio usando Domain Service
        // - Verifica que la facultad exista y esté activa
        // - Verifica que no exista otra carrera con el mismo nombre
        domainService.validarCreacionCarrera(facultadId, nombre);

        // 4. Generar nuevo ID
        CarreraId carreraId = CarreraId.of(ID_GENERATOR.getAndIncrement());

        // 5. Convertir Command → Domain Entity
        Carrera carrera = CarreraMapper.toDomain(command, carreraId);

        // 6. Persistir en el repositorio
        Carrera savedCarrera = carreraRepository.save(carrera);

        // 7. Obtener nombre de la facultad para el response
        Facultad facultad = facultadRepository.findById(facultadId)
                .orElseThrow(); // No debería fallar porque ya se validó antes
        String facultadNombre = facultad.getNombre().getValue();

        // 8. Convertir Domain → Response
        return CarreraMapper.toResponse(savedCarrera, facultadNombre);
    }
}

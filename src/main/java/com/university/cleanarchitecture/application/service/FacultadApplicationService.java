package com.university.cleanarchitecture.application.service;

import com.university.cleanarchitecture.application.port.in.*;
import com.university.cleanarchitecture.application.port.out.CarreraRepositoryPort;
import com.university.cleanarchitecture.application.port.out.FacultadRepositoryPort;
import com.university.cleanarchitecture.application.dto.command.CambiarDecanoCommand;
import com.university.cleanarchitecture.application.dto.command.RegisterFacultadCommand;
import com.university.cleanarchitecture.application.dto.command.UpdateFacultadCommand;
import com.university.cleanarchitecture.application.dto.query.*;
import com.university.cleanarchitecture.application.dto.response.FacultadResponse;
import com.university.cleanarchitecture.application.dto.response.FacultadSummaryResponse;
import com.university.cleanarchitecture.application.mapper.FacultadMapper;
import com.university.cleanarchitecture.domain.model.Facultad;
import com.university.cleanarchitecture.domain.model.valueobjects.FacultadId;
import com.university.cleanarchitecture.domain.model.valueobjects.NombreAcademico;
import com.university.cleanarchitecture.domain.exception.FacultadNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
@Transactional
public class FacultadApplicationService implements
        RegisterFacultadUseCase,
        UpdateFacultadUseCase,
        CambiarDecanoUseCase,
        FindFacultadByIdUseCase,
        FindFacultadByNombreUseCase,
        FindAllFacultadesUseCase,
        ActivateFacultadUseCase,
        DeactivateFacultadUseCase {

    private final FacultadRepositoryPort facultadRepository;
    private final CarreraRepositoryPort carreraRepository;
    private final FacultadMapper facultadMapper;
    private static final AtomicLong ID_GENERATOR = new AtomicLong(1);

    public FacultadApplicationService(FacultadRepositoryPort facultadRepository,
                                      CarreraRepositoryPort carreraRepository,
                                      FacultadMapper facultadMapper) {
        this.facultadRepository = facultadRepository;
        this.carreraRepository = carreraRepository;
        this.facultadMapper = facultadMapper;
    }

    @Override
    public FacultadResponse register(RegisterFacultadCommand command) {
        NombreAcademico nombre = NombreAcademico.of(command.getNombre());

        if (facultadRepository.existsByNombre(nombre)) {
            throw new RuntimeException("Ya existe una facultad con el nombre: " + command.getNombre());
        }

        FacultadId facultadId = FacultadId.of(ID_GENERATOR.getAndIncrement());

        Facultad facultad = Facultad.crear(
                facultadId,
                nombre,
                command.getDescripcion(),
                command.getUbicacion(),
                command.getDecano()
        );

        Facultad savedFacultad = facultadRepository.save(facultad);

        int cantidadCarreras = carreraRepository.countActiveByFacultadId(facultadId);

        return facultadMapper.toResponse(savedFacultad, cantidadCarreras);
    }

    @Override
    public FacultadResponse update(UpdateFacultadCommand command) {
        FacultadId facultadId = FacultadId.of(command.getFacultadId());

        Facultad facultad = facultadRepository.findById(facultadId)
                .orElseThrow(() -> new FacultadNotFoundException(command.getFacultadId()));

        NombreAcademico nuevoNombre = NombreAcademico.of(command.getNombre());

        if (facultadRepository.existsByNombreAndIdNot(nuevoNombre, facultadId)) {
            throw new RuntimeException("Ya existe otra facultad con el nombre: " + command.getNombre());
        }

        facultad.actualizarInformacion(
                nuevoNombre,
                command.getDescripcion(),
                command.getUbicacion()
        );

        Facultad updatedFacultad = facultadRepository.save(facultad);

        int cantidadCarreras = carreraRepository.countActiveByFacultadId(facultadId);

        return facultadMapper.toResponse(updatedFacultad, cantidadCarreras);
    }

    @Override
    public FacultadResponse cambiarDecano(CambiarDecanoCommand command) {
        FacultadId facultadId = FacultadId.of(command.getFacultadId());

        Facultad facultad = facultadRepository.findById(facultadId)
                .orElseThrow(() -> new FacultadNotFoundException(command.getFacultadId()));

        facultad.cambiarDecano(command.getNuevoDecano());

        Facultad updatedFacultad = facultadRepository.save(facultad);

        int cantidadCarreras = carreraRepository.countActiveByFacultadId(facultadId);

        return facultadMapper.toResponse(updatedFacultad, cantidadCarreras);
    }

    @Override
    @Transactional(readOnly = true)
    public FacultadResponse findById(FindFacultadByIdQuery query) {
        FacultadId facultadId = FacultadId.of(query.getFacultadId());

        Facultad facultad = facultadRepository.findById(facultadId)
                .orElseThrow(() -> new FacultadNotFoundException(query.getFacultadId()));

        int cantidadCarreras = carreraRepository.countActiveByFacultadId(facultadId);

        return facultadMapper.toResponse(facultad, cantidadCarreras);
    }

    @Override
    @Transactional(readOnly = true)
    public FacultadResponse findByNombre(FindFacultadByNombreQuery query) {
        NombreAcademico nombre = NombreAcademico.of(query.getNombre());

        Facultad facultad = facultadRepository.findByNombre(nombre)
                .orElseThrow(() -> new RuntimeException("Facultad no encontrada con nombre: " + query.getNombre()));

        int cantidadCarreras = carreraRepository.countActiveByFacultadId(facultad.getId());

        return facultadMapper.toResponse(facultad, cantidadCarreras);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FacultadSummaryResponse> findAll(FindAllFacultadesQuery query) {
        List<Facultad> facultades = query.isIncluirInactivas()
                ? facultadRepository.findAll()
                : facultadRepository.findAllActive();

        return facultades.stream()
                .map(this::enrichFacultadSummary)
                .collect(Collectors.toList());
    }

    @Override
    public void activate(Long facultadId) {
        Facultad facultad = facultadRepository.findById(FacultadId.of(facultadId))
                .orElseThrow(() -> new FacultadNotFoundException(facultadId));

        facultad.activar();
        facultadRepository.save(facultad);
    }

    @Override
    public void deactivate(Long facultadId) {
        FacultadId id = FacultadId.of(facultadId);

        Facultad facultad = facultadRepository.findById(id)
                .orElseThrow(() -> new FacultadNotFoundException(facultadId));

        int cantidadCarrerasActivas = carreraRepository.countActiveByFacultadId(id);

        if (cantidadCarrerasActivas > 0) {
            throw new RuntimeException("No se puede desactivar la facultad porque tiene " + cantidadCarrerasActivas + " carreras activas");
        }

        facultad.desactivar();
        facultadRepository.save(facultad);
    }

    private FacultadSummaryResponse enrichFacultadSummary(Facultad facultad) {
        int cantidadCarreras = carreraRepository.countActiveByFacultadId(facultad.getId());
        return facultadMapper.toSummaryResponse(facultad, cantidadCarreras);
    }
}

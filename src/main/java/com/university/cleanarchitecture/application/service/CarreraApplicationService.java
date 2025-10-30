package com.university.cleanarchitecture.application.service;

import com.university.cleanarchitecture.application.port.in.*;
import com.university.cleanarchitecture.application.port.out.CarreraRepositoryPort;
import com.university.cleanarchitecture.application.port.out.FacultadRepositoryPort;
import com.university.cleanarchitecture.application.dto.command.RegisterCarreraCommand;
import com.university.cleanarchitecture.application.dto.command.UpdateCarreraCommand;
import com.university.cleanarchitecture.application.dto.query.*;
import com.university.cleanarchitecture.application.dto.response.CarreraResponse;
import com.university.cleanarchitecture.application.dto.response.CarreraSummaryResponse;
import com.university.cleanarchitecture.application.mapper.CarreraMapper;
import com.university.cleanarchitecture.domain.model.Carrera;
import com.university.cleanarchitecture.domain.model.Facultad;
import com.university.cleanarchitecture.domain.model.valueobjects.*;
import com.university.cleanarchitecture.domain.exception.CarreraNotFoundException;
import com.university.cleanarchitecture.domain.exception.FacultadNotFoundException;
import com.university.cleanarchitecture.domain.service.CarreraDomainService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
@Transactional
public class CarreraApplicationService implements
        RegisterCarreraUseCase,
        UpdateCarreraUseCase,
        FindCarreraByIdUseCase,
        FindCarrerasByFacultadUseCase,
        FindCarrerasByDuracionUseCase,
        ActivateCarreraUseCase,
        DeactivateCarreraUseCase {

    private final CarreraRepositoryPort carreraRepository;
    private final FacultadRepositoryPort facultadRepository;
    private final CarreraDomainService carreraDomainService;
    private final CarreraMapper carreraMapper;
    private static final AtomicLong ID_GENERATOR = new AtomicLong(1);

    public CarreraApplicationService(CarreraRepositoryPort carreraRepository,
                                     FacultadRepositoryPort facultadRepository,
                                     CarreraDomainService carreraDomainService,
                                     CarreraMapper carreraMapper) {
        this.carreraRepository = carreraRepository;
        this.facultadRepository = facultadRepository;
        this.carreraDomainService = carreraDomainService;
        this.carreraMapper = carreraMapper;
    }

    @Override
    public CarreraResponse register(RegisterCarreraCommand command) {
        FacultadId facultadId = FacultadId.of(command.getFacultadId());
        NombreAcademico nombre = NombreAcademico.of(command.getNombre());

        carreraDomainService.validarCreacionCarrera(facultadId, nombre);

        CarreraId carreraId = CarreraId.of(ID_GENERATOR.getAndIncrement());

        Carrera carrera = Carrera.crear(
                carreraId,
                facultadId,
                nombre,
                command.getDescripcion(),
                Duracion.of(command.getDuracionSemestres()),
                command.getTituloOtorgado()
        );

        Carrera savedCarrera = carreraRepository.save(carrera);

        Facultad facultad = facultadRepository.findById(facultadId)
                .orElseThrow(() -> new FacultadNotFoundException(facultadId.getValue()));

        return carreraMapper.toResponse(savedCarrera, facultad.getNombre().getValue());
    }

    @Override
    public CarreraResponse update(UpdateCarreraCommand command) {
        CarreraId carreraId = CarreraId.of(command.getCarreraId());

        Carrera carrera = carreraRepository.findById(carreraId)
                .orElseThrow(() -> new CarreraNotFoundException(command.getCarreraId()));

        carrera.actualizarInformacionAcademica(
                NombreAcademico.of(command.getNombre()),
                command.getDescripcion(),
                Duracion.of(command.getDuracionSemestres()),
                command.getTituloOtorgado()
        );

        Carrera updatedCarrera = carreraRepository.save(carrera);

        Facultad facultad = facultadRepository.findById(carrera.getFacultadId())
                .orElseThrow(() -> new FacultadNotFoundException(carrera.getFacultadId().getValue()));

        return carreraMapper.toResponse(updatedCarrera, facultad.getNombre().getValue());
    }

    @Override
    @Transactional(readOnly = true)
    public CarreraResponse findById(FindCarreraByIdQuery query) {
        CarreraId carreraId = CarreraId.of(query.getCarreraId());

        Carrera carrera = carreraRepository.findById(carreraId)
                .orElseThrow(() -> new CarreraNotFoundException(query.getCarreraId()));

        Facultad facultad = facultadRepository.findById(carrera.getFacultadId())
                .orElseThrow(() -> new FacultadNotFoundException(carrera.getFacultadId().getValue()));

        return carreraMapper.toResponse(carrera, facultad.getNombre().getValue());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CarreraSummaryResponse> findByFacultad(FindCarrerasByFacultadQuery query) {
        FacultadId facultadId = FacultadId.of(query.getFacultadId());

        List<Carrera> carreras = query.isSoloActivas()
                ? carreraRepository.findActiveByFacultadId(facultadId)
                : carreraRepository.findByFacultadId(facultadId);

        Facultad facultad = facultadRepository.findById(facultadId)
                .orElseThrow(() -> new FacultadNotFoundException(facultadId.getValue()));

        return carreras.stream()
                .map(carrera -> carreraMapper.toSummaryResponse(carrera, facultad.getNombre().getValue()))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CarreraSummaryResponse> findByDuracion(FindCarrerasByDuracionQuery query) {
        List<Carrera> carreras;

        if (query.getDuracionSemestres() != null) {
            carreras = carreraRepository.findByDuracionSemestres(query.getDuracionSemestres());
        } else if (query.getDuracionMinima() != null || query.getDuracionMaxima() != null) {
            int min = query.getDuracionMinima() != null ? query.getDuracionMinima() : 6;
            int max = query.getDuracionMaxima() != null ? query.getDuracionMaxima() : 14;
            carreras = carreraRepository.findByDuracionBetween(min, max);
        } else {
            carreras = carreraRepository.findAllActive();
        }

        if (query.isSoloActivas()) {
            carreras = carreras.stream()
                    .filter(Carrera::isActivo)
                    .collect(Collectors.toList());
        }

        return carreras.stream()
                .map(this::enrichCarreraSummary)
                .collect(Collectors.toList());
    }

    @Override
    public void activate(Long carreraId) {
        Carrera carrera = carreraRepository.findById(CarreraId.of(carreraId))
                .orElseThrow(() -> new CarreraNotFoundException(carreraId));

        carrera.activar();
        carreraRepository.save(carrera);
    }

    @Override
    public void deactivate(Long carreraId) {
        Carrera carrera = carreraRepository.findById(CarreraId.of(carreraId))
                .orElseThrow(() -> new CarreraNotFoundException(carreraId));

        carrera.desactivar();
        carreraRepository.save(carrera);
    }

    private CarreraSummaryResponse enrichCarreraSummary(Carrera carrera) {
        Facultad facultad = facultadRepository.findById(carrera.getFacultadId())
                .orElseThrow(() -> new FacultadNotFoundException(carrera.getFacultadId().getValue()));

        return carreraMapper.toSummaryResponse(carrera, facultad.getNombre().getValue());
    }
}

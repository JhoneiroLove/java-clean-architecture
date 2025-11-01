package com.university.cleanarchitecture.infrastructure.config;

import com.university.cleanarchitecture.application.port.out.CarreraRepositoryPort;
import com.university.cleanarchitecture.application.port.out.FacultadRepositoryPort;
import com.university.cleanarchitecture.domain.repository.CarreraRepository;
import com.university.cleanarchitecture.domain.repository.FacultadRepository;
import com.university.cleanarchitecture.domain.service.CarreraDomainService;
import com.university.cleanarchitecture.infrastructure.adapter.out.persistence.adapter.CarreraRepositoryAdapter;
import com.university.cleanarchitecture.infrastructure.adapter.out.persistence.adapter.FacultadRepositoryAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DomainConfiguration {

    /**
     * Bean para CarreraRepository (interfaz de dominio)
     * Este bean delega al adapter de infraestructura
     */
    @Bean
    public CarreraRepository carreraRepository(CarreraRepositoryPort carreraRepositoryPort) {
        return new CarreraRepository() {
            @Override
            public com.university.cleanarchitecture.domain.model.Carrera save(
                    com.university.cleanarchitecture.domain.model.Carrera carrera) {
                return carreraRepositoryPort.save(carrera);
            }

            @Override
            public java.util.Optional<com.university.cleanarchitecture.domain.model.Carrera> findById(
                    com.university.cleanarchitecture.domain.model.valueobjects.CarreraId id) {
                return carreraRepositoryPort.findById(id);
            }

            @Override
            public java.util.Optional<com.university.cleanarchitecture.domain.model.Carrera> findByNombre(
                    com.university.cleanarchitecture.domain.model.valueobjects.NombreAcademico nombre) {
                return carreraRepositoryPort.findByNombre(nombre);
            }

            @Override
            public java.util.List<com.university.cleanarchitecture.domain.model.Carrera> findByFacultadId(
                    com.university.cleanarchitecture.domain.model.valueobjects.FacultadId facultadId) {
                return carreraRepositoryPort.findByFacultadId(facultadId);
            }

            @Override
            public java.util.List<com.university.cleanarchitecture.domain.model.Carrera> findActiveByFacultadId(
                    com.university.cleanarchitecture.domain.model.valueobjects.FacultadId facultadId) {
                return carreraRepositoryPort.findActiveByFacultadId(facultadId);
            }

            @Override
            public java.util.List<com.university.cleanarchitecture.domain.model.Carrera> findAllActive() {
                return carreraRepositoryPort.findAllActive();
            }

            @Override
            public java.util.List<com.university.cleanarchitecture.domain.model.Carrera> findAll() {
                return carreraRepositoryPort.findAll();
            }

            @Override
            public java.util.List<com.university.cleanarchitecture.domain.model.Carrera> findByDuracion(
                    com.university.cleanarchitecture.domain.model.valueobjects.Duracion duracion) {
                return carreraRepositoryPort.findByDuracionSemestres(duracion.getSemestres());
            }

            @Override
            public java.util.List<com.university.cleanarchitecture.domain.model.Carrera> findCarrerasCortas() {
                return carreraRepositoryPort.findByDuracionBetween(6, 9);
            }

            @Override
            public java.util.List<com.university.cleanarchitecture.domain.model.Carrera> findCarrerasLargas() {
                return carreraRepositoryPort.findByDuracionBetween(11, 14);
            }

            @Override
            public java.util.List<com.university.cleanarchitecture.domain.model.Carrera> findByTituloOtorgadoContaining(
                    String tituloOtorgado) {
                // Esta funcionalidad no está en el port, retornamos lista vacía
                return java.util.Collections.emptyList();
            }

            @Override
            public boolean existsByNombre(
                    com.university.cleanarchitecture.domain.model.valueobjects.NombreAcademico nombre) {
                return carreraRepositoryPort.existsByNombre(nombre);
            }

            @Override
            public boolean existsByNombreAndIdNot(
                    com.university.cleanarchitecture.domain.model.valueobjects.NombreAcademico nombre,
                    com.university.cleanarchitecture.domain.model.valueobjects.CarreraId excludeId) {
                return carreraRepositoryPort.existsByNombreAndIdNot(nombre, excludeId);
            }

            @Override
            public long countActiveByFacultadId(
                    com.university.cleanarchitecture.domain.model.valueobjects.FacultadId facultadId) {
                return carreraRepositoryPort.countActiveByFacultadId(facultadId);
            }

            @Override
            public void deleteById(
                    com.university.cleanarchitecture.domain.model.valueobjects.CarreraId id) {
                carreraRepositoryPort.deleteById(id);
            }
        };
    }

    /**
     * Bean para FacultadRepository (interfaz de dominio)
     */
    @Bean
    public FacultadRepository facultadRepository(FacultadRepositoryPort facultadRepositoryPort) {
        return new FacultadRepository() {
            @Override
            public com.university.cleanarchitecture.domain.model.Facultad save(
                    com.university.cleanarchitecture.domain.model.Facultad facultad) {
                return facultadRepositoryPort.save(facultad);
            }

            @Override
            public java.util.Optional<com.university.cleanarchitecture.domain.model.Facultad> findById(
                    com.university.cleanarchitecture.domain.model.valueobjects.FacultadId id) {
                return facultadRepositoryPort.findById(id);
            }

            @Override
            public java.util.Optional<com.university.cleanarchitecture.domain.model.Facultad> findByNombre(
                    com.university.cleanarchitecture.domain.model.valueobjects.NombreAcademico nombre) {
                return facultadRepositoryPort.findByNombre(nombre);
            }

            @Override
            public java.util.List<com.university.cleanarchitecture.domain.model.Facultad> findAllActive() {
                return facultadRepositoryPort.findAllActive();
            }

            @Override
            public java.util.List<com.university.cleanarchitecture.domain.model.Facultad> findAll() {
                return facultadRepositoryPort.findAll();
            }

            @Override
            public java.util.List<com.university.cleanarchitecture.domain.model.Facultad> findByUbicacion(
                    String ubicacion) {
                // Esta funcionalidad no está en el port, retornamos lista vacía
                return java.util.Collections.emptyList();
            }

            @Override
            public boolean existsByNombre(
                    com.university.cleanarchitecture.domain.model.valueobjects.NombreAcademico nombre) {
                return facultadRepositoryPort.existsByNombre(nombre);
            }

            @Override
            public boolean existsByNombreAndIdNot(
                    com.university.cleanarchitecture.domain.model.valueobjects.NombreAcademico nombre,
                    com.university.cleanarchitecture.domain.model.valueobjects.FacultadId excludeId) {
                return facultadRepositoryPort.existsByNombreAndIdNot(nombre, excludeId);
            }

            @Override
            public long countCarrerasByFacultadId(
                    com.university.cleanarchitecture.domain.model.valueobjects.FacultadId facultadId) {
                return facultadRepositoryPort.countCarrerasByFacultadId(facultadId);
            }

            @Override
            public void deleteById(
                    com.university.cleanarchitecture.domain.model.valueobjects.FacultadId id) {
                facultadRepositoryPort.deleteById(id);
            }
        };
    }

    /**
     * Bean para el servicio de dominio de Carrera
     */
    @Bean
    public CarreraDomainService carreraDomainService(
            CarreraRepository carreraRepository,
            FacultadRepository facultadRepository) {
        return new CarreraDomainService(carreraRepository, facultadRepository);
    }
}

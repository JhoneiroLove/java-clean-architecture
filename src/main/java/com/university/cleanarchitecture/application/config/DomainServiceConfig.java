package com.university.cleanarchitecture.application.config;

import com.university.cleanarchitecture.application.port.out.CarreraRepositoryPort;
import com.university.cleanarchitecture.application.port.out.FacultadRepositoryPort;
import com.university.cleanarchitecture.domain.service.CarreraDomainService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración de Spring para los servicios de dominio.
 *
 * Los Domain Services son parte de la capa de dominio pero necesitan
 * ser instanciados como Beans de Spring para poder ser inyectados
 * en los Application Services.
 *
 * Esta clase actúa como un "bridge" entre Spring y el dominio puro.
 */
@Configuration
public class DomainServiceConfig {

    /**
     * Crea una instancia de CarreraDomainService como Bean de Spring.
     *
     * El Domain Service necesita acceso a los repositorios para validar
     * reglas de negocio complejas que involucran múltiples agregados.
     *
     * IMPORTANTE: Los repositorios que se inyectan aquí son los PORTS
     * (interfaces de Application), no las implementaciones de Infrastructure.
     *
     * @param carreraRepository puerto de salida para carreras
     * @param facultadRepository puerto de salida para facultades
     * @return instancia configurada de CarreraDomainService
     */
    @Bean
    public CarreraDomainService carreraDomainService(
            CarreraRepositoryPort carreraRepository,
            FacultadRepositoryPort facultadRepository) {

        return new CarreraDomainService(carreraRepository, facultadRepository);
    }
}



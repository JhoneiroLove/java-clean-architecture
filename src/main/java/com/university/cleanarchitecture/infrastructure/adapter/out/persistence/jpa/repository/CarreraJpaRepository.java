package com.university.cleanarchitecture.infrastructure.adapter.out.persistence.jpa.repository;

import com.university.cleanarchitecture.infrastructure.adapter.out.persistence.jpa.entity.CarreraJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CarreraJpaRepository extends JpaRepository<CarreraJpaEntity, Long> {

    Optional<CarreraJpaEntity> findByNombre(String nombre);

    List<CarreraJpaEntity> findByFacultadId(Long facultadId);

    @Query("SELECT c FROM CarreraJpaEntity c WHERE c.facultadId = :facultadId AND c.activo = true")
    List<CarreraJpaEntity> findActiveByFacultadId(@Param("facultadId") Long facultadId);

    List<CarreraJpaEntity> findByActivoTrue();

    List<CarreraJpaEntity> findByDuracionSemestres(Integer duracionSemestres);

    @Query("SELECT c FROM CarreraJpaEntity c WHERE c.duracionSemestres < 10")
    List<CarreraJpaEntity> findCarrerasCortas();

    @Query("SELECT c FROM CarreraJpaEntity c WHERE c.duracionSemestres > 10")
    List<CarreraJpaEntity> findCarrerasLargas();

    List<CarreraJpaEntity> findByTituloOtorgadoContaining(String tituloOtorgado);

    boolean existsByNombre(String nombre);

    boolean existsByNombreAndIdNot(String nombre, Long id);

    @Query("SELECT COUNT(c) FROM CarreraJpaEntity c WHERE c.facultadId = :facultadId AND c.activo = true")
    long countActiveByFacultadId(@Param("facultadId") Long facultadId);
}


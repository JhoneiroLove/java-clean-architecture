package com.university.cleanarchitecture.infrastructure.adapter.out.persistence.jpa.repository;

import com.university.cleanarchitecture.infrastructure.adapter.out.persistence.jpa.entity.FacultadJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FacultadJpaRepository extends JpaRepository<FacultadJpaEntity, Long> {

    Optional<FacultadJpaEntity> findByNombre(String nombre);

    List<FacultadJpaEntity> findByActivoTrue();

    List<FacultadJpaEntity> findByUbicacion(String ubicacion);

    boolean existsByNombre(String nombre);

    boolean existsByNombreAndIdNot(String nombre, Long id);

    @Query("SELECT COUNT(c) FROM CarreraJpaEntity c WHERE c.facultadId = :facultadId")
    long countCarrerasByFacultadId(@Param("facultadId") Long facultadId);
}


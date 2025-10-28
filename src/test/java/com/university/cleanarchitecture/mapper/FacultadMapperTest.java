package com.university.cleanarchitecture.mapper;

import com.university.cleanarchitecture.application.dto.command.RegisterFacultadCommand;
import com.university.cleanarchitecture.application.dto.command.UpdateFacultadCommand;
import com.university.cleanarchitecture.application.dto.response.FacultadResponse;
import com.university.cleanarchitecture.application.dto.response.FacultadSummaryResponse;
import com.university.cleanarchitecture.application.mapper.FacultadMapper;
import com.university.cleanarchitecture.domain.model.Facultad;
import com.university.cleanarchitecture.domain.model.valueobjects.FacultadId;
import com.university.cleanarchitecture.domain.model.valueobjects.NombreAcademico;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitarios para FacultadMapper.
 *
 * Verifica:
 * - Conversiones correctas entre Command → Domain
 * - Conversiones correctas entre Domain → Response
 * - Manejo de casos nulos
 * - Normalización de datos
 */
class FacultadMapperTest {

    @Test
    @DisplayName("Debe convertir RegisterFacultadCommand a Facultad correctamente")
    void testToDomain_RegisterCommand_Success() {
        // Given
        RegisterFacultadCommand command = new RegisterFacultadCommand(
                "facultad de ingeniería",  // Minúsculas (se normalizará)
                "Facultad de las ingenierías",
                "Edificio A",
                "Dr. Juan Pérez"
        );
        FacultadId facultadId = FacultadId.of(1L);

        // When
        Facultad facultad = FacultadMapper.toDomain(command, facultadId);

        // Then
        assertNotNull(facultad);
        assertEquals(facultadId, facultad.getId());
        assertEquals("Facultad de Ingeniería", facultad.getNombre().getValue()); // Normalizado
        assertEquals("Facultad de las ingenierías", facultad.getDescripcion());
        assertEquals("Edificio A", facultad.getUbicacion());
        assertEquals("Dr. Juan Pérez", facultad.getDecano());
        assertTrue(facultad.isActivo());
        assertNotNull(facultad.getFechaRegistro());
    }

    @Test
    @DisplayName("Debe lanzar excepción si RegisterFacultadCommand es nulo")
    void testToDomain_NullCommand_ThrowsException() {
        // Given
        FacultadId facultadId = FacultadId.of(1L);

        // When & Then
        assertThrows(IllegalArgumentException.class,
                () -> FacultadMapper.toDomain(null, facultadId));
    }

    @Test
    @DisplayName("Debe lanzar excepción si FacultadId es nulo")
    void testToDomain_NullFacultadId_ThrowsException() {
        // Given
        RegisterFacultadCommand command = new RegisterFacultadCommand(
                "Facultad de Ingeniería",
                "Descripción",
                "Ubicación",
                "Dr. Decano"
        );

        // When & Then
        assertThrows(IllegalArgumentException.class,
                () -> FacultadMapper.toDomain(command, null));
    }

    @Test
    @DisplayName("Debe aplicar UpdateFacultadCommand a Facultad existente")
    void testApplyUpdateCommand_Success() {
        // Given
        Facultad facultad = Facultad.crear(
                FacultadId.of(1L),
                NombreAcademico.of("Nombre Original"),
                "Descripción original",
                "Ubicación original",
                "Dr. Original"
        );

        UpdateFacultadCommand command = new UpdateFacultadCommand(
                1L,
                "nombre actualizado",  // Minúsculas (se normalizará)
                "Nueva descripción",
                "Nueva ubicación"
        );

        // When
        FacultadMapper.applyUpdateCommand(command, facultad);

        // Then
        assertEquals("Nombre Actualizado", facultad.getNombre().getValue()); // Normalizado
        assertEquals("Nueva descripción", facultad.getDescripcion());
        assertEquals("Nueva ubicación", facultad.getUbicacion());
        assertEquals("Dr. Original", facultad.getDecano()); // No cambia en update
    }

    @Test
    @DisplayName("Debe convertir Facultad a FacultadResponse correctamente")
    void testToResponse_Success() {
        // Given
        Facultad facultad = Facultad.crear(
                FacultadId.of(1L),
                NombreAcademico.of("Facultad de Ingeniería"),
                "Facultad de las ingenierías",
                "Edificio A",
                "Dr. Juan Pérez"
        );
        int cantidadCarreras = 5;

        // When
        FacultadResponse response = FacultadMapper.toResponse(facultad, cantidadCarreras);

        // Then
        assertNotNull(response);
        assertEquals(1L, response.id());
        assertEquals("Facultad de Ingeniería", response.nombre());
        assertEquals("Facultad de las ingenierías", response.descripcion());
        assertEquals("Edificio A", response.ubicacion());
        assertEquals("Dr. Juan Pérez", response.decano());
        assertTrue(response.activo());
        assertEquals(5, response.cantidadCarreras());
        assertNotNull(response.fechaRegistro());
    }

    @Test
    @DisplayName("Debe convertir Facultad a FacultadResponse sin cantidad de carreras")
    void testToResponse_WithoutCarrerasCount_Success() {
        // Given
        Facultad facultad = Facultad.crear(
                FacultadId.of(1L),
                NombreAcademico.of("Facultad de Medicina"),
                "Facultad de ciencias médicas",
                "Edificio B",
                "Dra. María García"
        );

        // When
        FacultadResponse response = FacultadMapper.toResponse(facultad);

        // Then
        assertNotNull(response);
        assertEquals(0, response.cantidadCarreras()); // Default
    }

    @Test
    @DisplayName("Debe convertir Facultad a FacultadSummaryResponse correctamente")
    void testToSummaryResponse_Success() {
        // Given
        Facultad facultad = Facultad.crear(
                FacultadId.of(2L),
                NombreAcademico.of("Facultad de Derecho"),
                "Facultad de ciencias jurídicas",
                "Edificio C",
                "Dr. Carlos López"
        );
        int cantidadCarreras = 3;

        // When
        FacultadSummaryResponse response = FacultadMapper.toSummaryResponse(facultad, cantidadCarreras);

        // Then
        assertNotNull(response);
        assertEquals(2L, response.id());
        assertEquals("Facultad de Derecho", response.nombre());
        assertEquals("Edificio C", response.ubicacion());
        assertEquals("Dr. Carlos López", response.decano());
        assertTrue(response.activo());
        assertEquals(3, response.cantidadCarreras());
    }

    @Test
    @DisplayName("Debe convertir Long a FacultadId correctamente")
    void testToFacultadId_Success() {
        // Given
        Long id = 42L;

        // When
        FacultadId facultadId = FacultadMapper.toFacultadId(id);

        // Then
        assertNotNull(facultadId);
        assertEquals(42L, facultadId.getValue());
    }

    @Test
    @DisplayName("Debe lanzar excepción si el ID es nulo en toFacultadId")
    void testToFacultadId_NullId_ThrowsException() {
        // When & Then
        assertThrows(IllegalArgumentException.class,
                () -> FacultadMapper.toFacultadId(null));
    }

    @Test
    @DisplayName("Debe convertir String a NombreAcademico correctamente")
    void testToNombreAcademico_Success() {
        // Given
        String nombre = "  facultad de CIENCIAS  ";

        // When
        NombreAcademico nombreAcademico = FacultadMapper.toNombreAcademico(nombre);

        // Then
        assertNotNull(nombreAcademico);
        assertEquals("Facultad de Ciencias", nombreAcademico.getValue()); // Normalizado y trimmed
    }

    @Test
    @DisplayName("Debe lanzar excepción si el nombre está vacío")
    void testToNombreAcademico_EmptyString_ThrowsException() {
        // When & Then
        assertThrows(IllegalArgumentException.class,
                () -> FacultadMapper.toNombreAcademico("   "));
    }

    @Test
    @DisplayName("Debe obtener iniciales correctamente de FacultadResponse")
    void testGetIniciales_FromResponse() {
        // Given
        Facultad facultad = Facultad.crear(
                FacultadId.of(1L),
                NombreAcademico.of("Facultad de Ingeniería y Arquitectura"),
                "Descripción",
                "Ubicación",
                "Dr. Decano"
        );

        FacultadResponse response = FacultadMapper.toResponse(facultad);

        // When
        String iniciales = response.getIniciales();

        // Then
        assertEquals("FIA", iniciales); // "de" y "y" se ignoran
    }
}

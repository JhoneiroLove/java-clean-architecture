package com.university.cleanarchitecture.mapper;

import com.university.cleanarchitecture.application.dto.command.RegisterCarreraCommand;
import com.university.cleanarchitecture.application.dto.command.UpdateCarreraCommand;
import com.university.cleanarchitecture.application.dto.response.CarreraResponse;
import com.university.cleanarchitecture.application.dto.response.CarreraSummaryResponse;
import com.university.cleanarchitecture.application.mapper.CarreraMapper;
import com.university.cleanarchitecture.domain.model.Carrera;
import com.university.cleanarchitecture.domain.model.valueobjects.CarreraId;
import com.university.cleanarchitecture.domain.model.valueobjects.Duracion;
import com.university.cleanarchitecture.domain.model.valueobjects.FacultadId;
import com.university.cleanarchitecture.domain.model.valueobjects.NombreAcademico;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitarios para CarreraMapper.
 *
 * Verifica:
 * - Conversiones correctas entre Command → Domain
 * - Conversiones correctas entre Domain → Response
 * - Manejo de Value Objects (Duracion, NombreAcademico)
 * - Clasificación automática de carreras
 */
class CarreraMapperTest {

    @Test
    @DisplayName("Debe convertir RegisterCarreraCommand a Carrera correctamente")
    void testToDomain_RegisterCommand_Success() {
        // Given
        RegisterCarreraCommand command = new RegisterCarreraCommand(
                1L,
                "ingeniería de sistemas",  // Minúsculas (se normalizará)
                "Carrera de ingeniería en sistemas computacionales",
                10,
                "Ingeniero de Sistemas"
        );
        CarreraId carreraId = CarreraId.of(1L);

        // When
        Carrera carrera = CarreraMapper.toDomain(command, carreraId);

        // Then
        assertNotNull(carrera);
        assertEquals(carreraId, carrera.getId());
        assertEquals(FacultadId.of(1L), carrera.getFacultadId());
        assertEquals("Ingeniería de Sistemas", carrera.getNombre().getValue()); // Normalizado
        assertEquals("Carrera de ingeniería en sistemas computacionales", carrera.getDescripcion());
        assertEquals(10, carrera.getDuracion().getSemestres());
        assertEquals("Ingeniero de Sistemas", carrera.getTituloOtorgado());
        assertTrue(carrera.isActivo());
        assertNotNull(carrera.getFechaRegistro());
    }

    @Test
    @DisplayName("Debe lanzar excepción si RegisterCarreraCommand es nulo")
    void testToDomain_NullCommand_ThrowsException() {
        // Given
        CarreraId carreraId = CarreraId.of(1L);

        // When & Then
        assertThrows(IllegalArgumentException.class,
                () -> CarreraMapper.toDomain(null, carreraId));
    }

    @Test
    @DisplayName("Debe lanzar excepción si CarreraId es nulo")
    void testToDomain_NullCarreraId_ThrowsException() {
        // Given
        RegisterCarreraCommand command = new RegisterCarreraCommand(
                1L,
                "Ingeniería de Sistemas",
                "Descripción",
                10,
                "Ingeniero"
        );

        // When & Then
        assertThrows(IllegalArgumentException.class,
                () -> CarreraMapper.toDomain(command, null));
    }

    @Test
    @DisplayName("Debe aplicar UpdateCarreraCommand a Carrera existente")
    void testApplyUpdateCommand_Success() {
        // Given
        Carrera carrera = Carrera.crear(
                CarreraId.of(1L),
                FacultadId.of(1L),
                NombreAcademico.of("Nombre Original"),
                "Descripción original",
                Duracion.of(10),
                "Título Original"
        );

        UpdateCarreraCommand command = new UpdateCarreraCommand(
                1L,
                "nombre actualizado",
                "Nueva descripción",
                12,
                "Nuevo Título"
        );

        // When
        CarreraMapper.applyUpdateCommand(command, carrera);

        // Then
        assertEquals("Nombre Actualizado", carrera.getNombre().getValue());
        assertEquals("Nueva descripción", carrera.getDescripcion());
        assertEquals(12, carrera.getDuracion().getSemestres());
        assertEquals("Nuevo Título", carrera.getTituloOtorgado());
    }

    @Test
    @DisplayName("Debe convertir Carrera a CarreraResponse correctamente - Carrera Estándar")
    void testToResponse_CarreraEstandar_Success() {
        // Given
        Carrera carrera = Carrera.crear(
                CarreraId.of(1L),
                FacultadId.of(1L),
                NombreAcademico.of("Ingeniería de Sistemas"),
                "Carrera de sistemas",
                Duracion.of(10),  // Estándar: 10 semestres
                "Ingeniero de Sistemas"
        );
        String facultadNombre = "Facultad de Ingeniería";

        // When
        CarreraResponse response = CarreraMapper.toResponse(carrera, facultadNombre);

        // Then
        assertNotNull(response);
        assertEquals(1L, response.id());
        assertEquals("Ingeniería de Sistemas", response.nombre());
        assertEquals("Carrera de sistemas", response.descripcion());
        assertEquals(10, response.duracionSemestres());
        assertEquals(5, response.duracionAnios()); // 10 semestres = 5 años
        assertEquals("Ingeniero de Sistemas", response.tituloOtorgado());
        assertTrue(response.activo());
        assertEquals(1L, response.facultadId());
        assertEquals("Facultad de Ingeniería", response.facultadNombre());
        assertEquals(CarreraResponse.ClasificacionCarrera.ESTANDAR, response.clasificacion());
    }

    @Test
    @DisplayName("Debe convertir Carrera a CarreraResponse correctamente - Carrera Corta")
    void testToResponse_CarreraCorta_Success() {
        // Given
        Carrera carrera = Carrera.crear(
                CarreraId.of(2L),
                FacultadId.of(1L),
                NombreAcademico.of("Técnico en Sistemas"),
                "Carrera técnica",
                Duracion.of(8),  // Corta: menos de 10 semestres
                "Técnico Profesional"
        );
        String facultadNombre = "Facultad de Ingeniería";

        // When
        CarreraResponse response = CarreraMapper.toResponse(carrera, facultadNombre);

        // Then
        assertEquals(8, response.duracionSemestres());
        assertEquals(4, response.duracionAnios()); // 8 semestres = 4 años
        assertEquals(CarreraResponse.ClasificacionCarrera.CORTA, response.clasificacion());
    }

    @Test
    @DisplayName("Debe convertir Carrera a CarreraResponse correctamente - Carrera Larga")
    void testToResponse_CarreraLarga_Success() {
        // Given
        Carrera carrera = Carrera.crear(
                CarreraId.of(3L),
                FacultadId.of(2L),
                NombreAcademico.of("Medicina"),
                "Carrera de medicina humana",
                Duracion.of(14),  // Larga: más de 10 semestres
                "Médico Cirujano"
        );
        String facultadNombre = "Facultad de Medicina";

        // When
        CarreraResponse response = CarreraMapper.toResponse(carrera, facultadNombre);

        // Then
        assertEquals(14, response.duracionSemestres());
        assertEquals(7, response.duracionAnios()); // 14 semestres = 7 años
        assertEquals(CarreraResponse.ClasificacionCarrera.LARGA, response.clasificacion());
    }

    @Test
    @DisplayName("Debe lanzar excepción si facultadNombre es nulo en toResponse")
    void testToResponse_NullFacultadNombre_ThrowsException() {
        // Given
        Carrera carrera = Carrera.crear(
                CarreraId.of(1L),
                FacultadId.of(1L),
                NombreAcademico.of("Carrera"),
                "Descripción",
                Duracion.of(10),
                "Título"
        );

        // When & Then
        assertThrows(IllegalArgumentException.class,
                () -> CarreraMapper.toResponse(carrera, null));
    }

    @Test
    @DisplayName("Debe convertir Carrera a CarreraSummaryResponse correctamente")
    void testToSummaryResponse_Success() {
        // Given
        Carrera carrera = Carrera.crear(
                CarreraId.of(1L),
                FacultadId.of(1L),
                NombreAcademico.of("Ingeniería Industrial"),
                "Carrera de ingeniería industrial",
                Duracion.of(10),
                "Ingeniero Industrial"
        );
        String facultadNombre = "Facultad de Ingeniería";

        // When
        CarreraSummaryResponse response = CarreraMapper.toSummaryResponse(carrera, facultadNombre);

        // Then
        assertNotNull(response);
        assertEquals(1L, response.id());
        assertEquals("Ingeniería Industrial", response.nombre());
        assertEquals(10, response.duracionSemestres());
        assertEquals("Ingeniero Industrial", response.tituloOtorgado());
        assertTrue(response.activo());
        assertEquals(1L, response.facultadId());
        assertEquals("Facultad de Ingeniería", response.facultadNombre());

        // Verificar métodos helper
        assertEquals(5, response.getDuracionAnios());
        assertTrue(response.isEstandar());
        assertFalse(response.isCorta());
        assertFalse(response.isLarga());
    }

    @Test
    @DisplayName("Debe convertir Long a CarreraId correctamente")
    void testToCarreraId_Success() {
        // Given
        Long id = 42L;

        // When
        CarreraId carreraId = CarreraMapper.toCarreraId(id);

        // Then
        assertNotNull(carreraId);
        assertEquals(42L, carreraId.getValue());
    }

    @Test
    @DisplayName("Debe convertir Long a FacultadId correctamente")
    void testToFacultadId_Success() {
        // Given
        Long id = 10L;

        // When
        FacultadId facultadId = CarreraMapper.toFacultadId(id);

        // Then
        assertNotNull(facultadId);
        assertEquals(10L, facultadId.getValue());
    }

    @Test
    @DisplayName("Debe convertir Integer a Duracion correctamente")
    void testToDuracion_Success() {
        // Given
        Integer semestres = 12;

        // When
        Duracion duracion = CarreraMapper.toDuracion(semestres);

        // Then
        assertNotNull(duracion);
        assertEquals(12, duracion.getSemestres());
        assertEquals(6, duracion.getAnios());
        assertTrue(duracion.isLarga());
    }

    @Test
    @DisplayName("Debe lanzar excepción si la duración es nula")
    void testToDuracion_NullSemestres_ThrowsException() {
        // When & Then
        assertThrows(IllegalArgumentException.class,
                () -> CarreraMapper.toDuracion(null));
    }

    @Test
    @DisplayName("Debe obtener clasificación como String correctamente")
    void testGetClasificacionString_AllTypes() {
        // Given
        Duracion corta = Duracion.of(8);
        Duracion estandar = Duracion.of(10);
        Duracion larga = Duracion.of(12);

        // When & Then
        assertEquals("CORTA", CarreraMapper.getClasificacionString(corta));
        assertEquals("ESTANDAR", CarreraMapper.getClasificacionString(estandar));
        assertEquals("LARGA", CarreraMapper.getClasificacionString(larga));
    }

    @Test
    @DisplayName("Debe calcular duración descriptiva correctamente en response")
    void testGetDuracionDescriptiva_FromResponse() {
        // Given
        Carrera carrera = Carrera.crear(
                CarreraId.of(1L),
                FacultadId.of(1L),
                NombreAcademico.of("Ingeniería Civil"),
                "Descripción",
                Duracion.of(11),
                "Ingeniero Civil"
        );

        CarreraResponse response = CarreraMapper.toResponse(carrera, "Facultad X");

        // When
        String descripcion = response.getDuracionDescriptiva();

        // Then
        assertEquals("11 semestres (6 años)", descripcion);
    }
}

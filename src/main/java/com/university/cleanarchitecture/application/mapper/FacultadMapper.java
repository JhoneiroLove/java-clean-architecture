package com.university.cleanarchitecture.application.mapper;

import com.university.cleanarchitecture.application.dto.command.RegisterFacultadCommand;
import com.university.cleanarchitecture.application.dto.command.UpdateFacultadCommand;
import com.university.cleanarchitecture.application.dto.response.FacultadResponse;
import com.university.cleanarchitecture.application.dto.response.FacultadSummaryResponse;
import com.university.cleanarchitecture.domain.model.Facultad;
import com.university.cleanarchitecture.domain.model.valueobjects.FacultadId;
import com.university.cleanarchitecture.domain.model.valueobjects.NombreAcademico;

/**
 * Mapper para convertir entre objetos del dominio Facultad y DTOs de aplicación.
 *
 * Responsabilidades:
 * - Convertir Commands → Domain (para operaciones de escritura)
 * - Convertir Domain → Responses (para operaciones de lectura)
 * - Aplicar reglas de transformación y normalización
 *
 * Principios aplicados:
 * - Single Responsibility: Solo convierte entre capas
 * - No contiene lógica de negocio (eso va en el dominio)
 * - Stateless: Métodos estáticos sin estado interno
 */
public final class FacultadMapper {

    // Prevenir instanciación
    private FacultadMapper() {
        throw new UnsupportedOperationException("Utility class - No debe ser instanciada");
    }

    // ========================================
    // COMMAND → DOMAIN (Escritura)
    // ========================================

    /**
     * Convierte un RegisterFacultadCommand a una entidad Facultad del dominio.
     * Usado al CREAR una nueva facultad.
     *
     * @param command el comando de registro
     * @param facultadId el ID generado para la nueva facultad
     * @return entidad Facultad lista para persistir
     */
    public static Facultad toDomain(RegisterFacultadCommand command, FacultadId facultadId) {
        if (command == null) {
            throw new IllegalArgumentException("RegisterFacultadCommand no puede ser nulo");
        }
        if (facultadId == null) {
            throw new IllegalArgumentException("FacultadId no puede ser nulo");
        }

        return Facultad.crear(
                facultadId,
                NombreAcademico.of(command.nombre()),
                command.descripcion(),
                command.ubicacion(),
                command.decano()
        );
    }

    /**
     * Aplica los cambios de un UpdateFacultadCommand a una Facultad existente.
     * Usado al ACTUALIZAR una facultad.
     *
     * NOTA: Este método NO crea una nueva entidad, solo aplica los cambios.
     *
     * @param command el comando de actualización
     * @param facultad la facultad existente a actualizar (se modifica in-place)
     */
    public static void applyUpdateCommand(UpdateFacultadCommand command, Facultad facultad) {
        if (command == null) {
            throw new IllegalArgumentException("UpdateFacultadCommand no puede ser nulo");
        }
        if (facultad == null) {
            throw new IllegalArgumentException("Facultad no puede ser nula");
        }

        // Delegar al método de dominio que contiene las validaciones
        facultad.actualizarInformacion(
                NombreAcademico.of(command.nombre()),
                command.descripcion(),
                command.ubicacion()
        );
    }

    // ========================================
    // DOMAIN → RESPONSE (Lectura)
    // ========================================

    /**
     * Convierte una entidad Facultad del dominio a FacultadResponse (completo).
     * Usado para retornar información detallada de una facultad.
     *
     * @param facultad la entidad del dominio
     * @param cantidadCarreras número de carreras asociadas (dato agregado)
     * @return DTO de respuesta con información completa
     */
    public static FacultadResponse toResponse(Facultad facultad, int cantidadCarreras) {
        if (facultad == null) {
            throw new IllegalArgumentException("Facultad no puede ser nula");
        }

        return new FacultadResponse(
                facultad.getId().getValue(),
                facultad.getNombre().getValue(),
                facultad.getDescripcion(),
                facultad.getUbicacion(),
                facultad.getDecano(),
                facultad.getFechaRegistro(),
                facultad.isActivo(),
                cantidadCarreras
        );
    }

    /**
     * Sobrecarga: Convierte sin cantidad de carreras (se asume 0).
     * Útil cuando no se necesita ese dato agregado.
     */
    public static FacultadResponse toResponse(Facultad facultad) {
        return toResponse(facultad, 0);
    }

    /**
     * Convierte una entidad Facultad a FacultadSummaryResponse (resumen).
     * Usado para listar múltiples facultades con información resumida.
     *
     * @param facultad la entidad del dominio
     * @param cantidadCarreras número de carreras asociadas
     * @return DTO de respuesta resumido
     */
    public static FacultadSummaryResponse toSummaryResponse(Facultad facultad, int cantidadCarreras) {
        if (facultad == null) {
            throw new IllegalArgumentException("Facultad no puede ser nula");
        }

        return new FacultadSummaryResponse(
                facultad.getId().getValue(),
                facultad.getNombre().getValue(),
                facultad.getUbicacion(),
                facultad.getDecano(),
                facultad.isActivo(),
                cantidadCarreras
        );
    }

    /**
     * Sobrecarga: Convierte sin cantidad de carreras.
     */
    public static FacultadSummaryResponse toSummaryResponse(Facultad facultad) {
        return toSummaryResponse(facultad, 0);
    }

    // ========================================
    // UTILIDADES DE CONVERSIÓN
    // ========================================

    /**
     * Convierte un Long primitivo a FacultadId (Value Object).
     * Helper común para conversiones.
     *
     * @param id el ID como Long
     * @return FacultadId inmutable
     */
    public static FacultadId toFacultadId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("El ID no puede ser nulo");
        }
        return FacultadId.of(id);
    }

    /**
     * Convierte un String a NombreAcademico (Value Object).
     * Helper común para conversiones.
     *
     * @param nombre el nombre como String
     * @return NombreAcademico inmutable y normalizado
     */
    public static NombreAcademico toNombreAcademico(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }
        return NombreAcademico.of(nombre);
    }
}

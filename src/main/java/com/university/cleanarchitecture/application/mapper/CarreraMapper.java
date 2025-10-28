package com.university.cleanarchitecture.application.mapper;

import com.university.cleanarchitecture.application.dto.command.RegisterCarreraCommand;
import com.university.cleanarchitecture.application.dto.command.UpdateCarreraCommand;
import com.university.cleanarchitecture.application.dto.response.CarreraResponse;
import com.university.cleanarchitecture.application.dto.response.CarreraSummaryResponse;
import com.university.cleanarchitecture.domain.model.Carrera;
import com.university.cleanarchitecture.domain.model.valueobjects.CarreraId;
import com.university.cleanarchitecture.domain.model.valueobjects.Duracion;
import com.university.cleanarchitecture.domain.model.valueobjects.FacultadId;
import com.university.cleanarchitecture.domain.model.valueobjects.NombreAcademico;

/**
 * Mapper para convertir entre objetos del dominio Carrera y DTOs de aplicación.
 *
 * Responsabilidades:
 * - Convertir Commands → Domain (para operaciones de escritura)
 * - Convertir Domain → Responses (para operaciones de lectura)
 * - Aplicar reglas de transformación de Value Objects
 *
 * Principios aplicados:
 * - Single Responsibility: Solo convierte entre capas
 * - No contiene lógica de negocio (eso va en el dominio)
 * - Stateless: Métodos estáticos sin estado interno
 * - Type Safety: Usa Value Objects del dominio
 */
public final class CarreraMapper {

    // Prevenir instanciación
    private CarreraMapper() {
        throw new UnsupportedOperationException("Utility class - No debe ser instanciada");
    }

    // ========================================
    // COMMAND → DOMAIN (Escritura)
    // ========================================

    /**
     * Convierte un RegisterCarreraCommand a una entidad Carrera del dominio.
     * Usado al CREAR una nueva carrera.
     *
     * @param command el comando de registro
     * @param carreraId el ID generado para la nueva carrera
     * @return entidad Carrera lista para persistir
     */
    public static Carrera toDomain(RegisterCarreraCommand command, CarreraId carreraId) {
        if (command == null) {
            throw new IllegalArgumentException("RegisterCarreraCommand no puede ser nulo");
        }
        if (carreraId == null) {
            throw new IllegalArgumentException("CarreraId no puede ser nulo");
        }

        return Carrera.crear(
                carreraId,
                FacultadId.of(command.facultadId()),
                NombreAcademico.of(command.nombre()),
                command.descripcion(),
                Duracion.of(command.duracionSemestres()),
                command.tituloOtorgado()
        );
    }

    /**
     * Aplica los cambios de un UpdateCarreraCommand a una Carrera existente.
     * Usado al ACTUALIZAR una carrera.
     *
     * NOTA: Este método NO crea una nueva entidad, solo aplica los cambios.
     *
     * @param command el comando de actualización
     * @param carrera la carrera existente a actualizar (se modifica in-place)
     */
    public static void applyUpdateCommand(UpdateCarreraCommand command, Carrera carrera) {
        if (command == null) {
            throw new IllegalArgumentException("UpdateCarreraCommand no puede ser nulo");
        }
        if (carrera == null) {
            throw new IllegalArgumentException("Carrera no puede ser nula");
        }

        // Delegar al método de dominio que contiene las validaciones
        carrera.actualizarInformacionAcademica(
                NombreAcademico.of(command.nombre()),
                command.descripcion(),
                Duracion.of(command.duracionSemestres()),
                command.tituloOtorgado()
        );
    }

    // ========================================
    // DOMAIN → RESPONSE (Lectura)
    // ========================================

    /**
     * Convierte una entidad Carrera del dominio a CarreraResponse (completo).
     * Usado para retornar información detallada de una carrera.
     *
     * NOTA: Requiere información adicional de la Facultad asociada.
     *
     * @param carrera la entidad del dominio
     * @param facultadNombre nombre de la facultad asociada
     * @return DTO de respuesta con información completa
     */
    public static CarreraResponse toResponse(Carrera carrera, String facultadNombre) {
        if (carrera == null) {
            throw new IllegalArgumentException("Carrera no puede ser nula");
        }
        if (facultadNombre == null || facultadNombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la facultad es requerido");
        }

        // Usar el constructor que calcula automáticamente duración en años y clasificación
        return new CarreraResponse(
                carrera.getId().getValue(),
                carrera.getNombre().getValue(),
                carrera.getDescripcion(),
                carrera.getDuracion().getSemestres(),
                carrera.getTituloOtorgado(),
                carrera.getFechaRegistro(),
                carrera.isActivo(),
                carrera.getFacultadId().getValue(),
                facultadNombre
        );
    }

    /**
     * Convierte una entidad Carrera a CarreraSummaryResponse (resumen).
     * Usado para listar múltiples carreras con información resumida.
     *
     * @param carrera la entidad del dominio
     * @param facultadNombre nombre de la facultad asociada
     * @return DTO de respuesta resumido
     */
    public static CarreraSummaryResponse toSummaryResponse(Carrera carrera, String facultadNombre) {
        if (carrera == null) {
            throw new IllegalArgumentException("Carrera no puede ser nula");
        }
        if (facultadNombre == null || facultadNombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la facultad es requerido");
        }

        return new CarreraSummaryResponse(
                carrera.getId().getValue(),
                carrera.getNombre().getValue(),
                carrera.getDuracion().getSemestres(),
                carrera.getTituloOtorgado(),
                carrera.isActivo(),
                carrera.getFacultadId().getValue(),
                facultadNombre
        );
    }

    // ========================================
    // UTILIDADES DE CONVERSIÓN
    // ========================================

    /**
     * Convierte un Long primitivo a CarreraId (Value Object).
     * Helper común para conversiones.
     *
     * @param id el ID como Long
     * @return CarreraId inmutable
     */
    public static CarreraId toCarreraId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("El ID no puede ser nulo");
        }
        return CarreraId.of(id);
    }

    /**
     * Convierte un Long primitivo a FacultadId (Value Object).
     * Helper común para conversiones de la facultad asociada.
     *
     * @param id el ID como Long
     * @return FacultadId inmutable
     */
    public static FacultadId toFacultadId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("El ID de la facultad no puede ser nulo");
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

    /**
     * Convierte un Integer a Duracion (Value Object).
     * Helper común para conversiones.
     *
     * @param semestres la duración en semestres
     * @return Duracion inmutable y validada
     */
    public static Duracion toDuracion(Integer semestres) {
        if (semestres == null) {
            throw new IllegalArgumentException("La duración no puede ser nula");
        }
        return Duracion.of(semestres);
    }

    /**
     * Obtiene la clasificación de una carrera basada en su duración.
     * Helper para construir responses sin acceder directamente al enum del response.
     *
     * @param duracion la duración de la carrera
     * @return clasificación como String
     */
    public static String getClasificacionString(Duracion duracion) {
        if (duracion == null) {
            throw new IllegalArgumentException("La duración no puede ser nula");
        }

        if (duracion.isCorta()) {
            return "CORTA";
        } else if (duracion.isEstandar()) {
            return "ESTANDAR";
        } else {
            return "LARGA";
        }
    }
}

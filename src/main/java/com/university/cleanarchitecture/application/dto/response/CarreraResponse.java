package com.university.cleanarchitecture.application.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

/**
 * Response inmutable que representa una Carrera completa.
 * Incluye información de la facultad asociada.
 *
 * @param id ID único de la carrera
 * @param nombre Nombre de la carrera (ya normalizado)
 * @param descripcion Descripción de la carrera
 * @param duracionSemestres Duración en semestres
 * @param duracionAnios Duración calculada en años
 * @param tituloOtorgado Título profesional que otorga
 * @param fechaRegistro Fecha de registro en el sistema
 * @param activo Estado de activación
 * @param facultadId ID de la facultad a la que pertenece
 * @param facultadNombre Nombre de la facultad
 * @param clasificacion Clasificación de la carrera (CORTA, ESTANDAR, LARGA)
 */
public record CarreraResponse(

        Long id,
        String nombre,
        String descripcion,
        int duracionSemestres,
        int duracionAnios,
        String tituloOtorgado,

        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime fechaRegistro,

        boolean activo,
        Long facultadId,
        String facultadNombre,
        ClasificacionCarrera clasificacion
) {

    /**
     * Enum para clasificación de carreras según duración.
     */
    public enum ClasificacionCarrera {
        CORTA,      // Menos de 10 semestres
        ESTANDAR,   // Exactamente 10 semestres (5 años)
        LARGA       // Más de 10 semestres
    }

    /**
     * Constructor que calcula automáticamente la clasificación.
     */
    public CarreraResponse(Long id, String nombre, String descripcion,
                           int duracionSemestres, String tituloOtorgado,
                           LocalDateTime fechaRegistro, boolean activo,
                           Long facultadId, String facultadNombre) {
        this(
                id,
                nombre,
                descripcion,
                duracionSemestres,
                calcularDuracionAnios(duracionSemestres),
                tituloOtorgado,
                fechaRegistro,
                activo,
                facultadId,
                facultadNombre,
                determinarClasificacion(duracionSemestres)
        );
    }

    private static int calcularDuracionAnios(int semestres) {
        return (int) Math.ceil(semestres / 2.0);
    }

    private static ClasificacionCarrera determinarClasificacion(int semestres) {
        if (semestres < 10) return ClasificacionCarrera.CORTA;
        if (semestres == 10) return ClasificacionCarrera.ESTANDAR;
        return ClasificacionCarrera.LARGA;
    }

    /**
     * Retorna un resumen descriptivo de la duración.
     */
    public String getDuracionDescriptiva() {
        return duracionSemestres + " semestres (" + duracionAnios + " años)";
    }
}

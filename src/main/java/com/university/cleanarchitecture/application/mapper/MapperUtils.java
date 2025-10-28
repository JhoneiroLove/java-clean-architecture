package com.university.cleanarchitecture.application.mapper;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Utilidades compartidas para todos los Mappers.
 *
 * Proporciona métodos helper genéricos para conversiones comunes:
 * - Conversión de colecciones
 * - Manejo de nulls
 * - Validaciones comunes
 *
 * Principios aplicados:
 * - DRY: Evita repetición de código en los mappers
 * - Type Safety: Usa genéricos para type-safe conversions
 * - Stateless: Sin estado interno
 */
public final class MapperUtils {

    // Prevenir instanciación
    private MapperUtils() {
        throw new UnsupportedOperationException("Utility class - No debe ser instanciada");
    }

    // ========================================
    // CONVERSIÓN DE COLECCIONES
    // ========================================

    /**
     * Convierte una lista de objetos de dominio a una lista de DTOs.
     * Maneja colecciones null retornando lista vacía.
     *
     * @param <T> tipo del objeto de dominio
     * @param <R> tipo del DTO resultante
     * @param source lista de objetos de dominio
     * @param mapper función de mapeo individual
     * @return lista inmutable de DTOs
     */
    public static <T, R> List<R> mapList(Collection<T> source, Function<T, R> mapper) {
        if (source == null || source.isEmpty()) {
            return Collections.emptyList();
        }

        return source.stream()
                .map(mapper)
                .collect(Collectors.toUnmodifiableList());
    }

    /**
     * Sobrecarga: Convierte una lista con un parámetro adicional constante.
     * Útil cuando el mapper necesita un parámetro extra (ej: facultadNombre).
     *
     * @param <T> tipo del objeto de dominio
     * @param <P> tipo del parámetro adicional
     * @param <R> tipo del DTO resultante
     * @param source lista de objetos de dominio
     * @param mapper función de mapeo con parámetro adicional
     * @param parameter parámetro adicional para el mapper
     * @return lista inmutable de DTOs
     */
    public static <T, P, R> List<R> mapListWithParam(
            Collection<T> source,
            BiFunction<T, P, R> mapper,
            P parameter) {

        if (source == null || source.isEmpty()) {
            return Collections.emptyList();
        }

        return source.stream()
                .map(item -> mapper.apply(item, parameter))
                .collect(Collectors.toUnmodifiableList());
    }

    // ========================================
    // VALIDACIONES COMUNES
    // ========================================

    /**
     * Valida que un objeto no sea nulo.
     *
     * @param <T> tipo del objeto
     * @param object el objeto a validar
     * @param fieldName nombre del campo (para mensaje de error)
     * @return el mismo objeto si no es nulo
     * @throws IllegalArgumentException si el objeto es nulo
     */
    public static <T> T requireNonNull(T object, String fieldName) {
        if (object == null) {
            throw new IllegalArgumentException(fieldName + " no puede ser nulo");
        }
        return object;
    }

    /**
     * Valida que un String no esté vacío.
     *
     * @param value el String a validar
     * @param fieldName nombre del campo (para mensaje de error)
     * @return el String trimmed si no está vacío
     * @throws IllegalArgumentException si el String es nulo o vacío
     */
    public static String requireNonBlank(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " no puede estar vacío");
        }
        return value.trim();
    }

    /**
     * Valida que un ID sea positivo.
     *
     * @param id el ID a validar
     * @param fieldName nombre del campo (para mensaje de error)
     * @return el mismo ID si es válido
     * @throws IllegalArgumentException si el ID es nulo o no positivo
     */
    public static Long requirePositive(Long id, String fieldName) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException(fieldName + " debe ser un número positivo");
        }
        return id;
    }

    /**
     * Valida que un número entero esté dentro de un rango.
     *
     * @param value el valor a validar
     * @param min valor mínimo permitido (inclusive)
     * @param max valor máximo permitido (inclusive)
     * @param fieldName nombre del campo (para mensaje de error)
     * @return el mismo valor si está en el rango
     * @throws IllegalArgumentException si el valor está fuera del rango
     */
    public static Integer requireInRange(Integer value, int min, int max, String fieldName) {
        if (value == null) {
            throw new IllegalArgumentException(fieldName + " no puede ser nulo");
        }
        if (value < min || value > max) {
            throw new IllegalArgumentException(
                    fieldName + " debe estar entre " + min + " y " + max +
                            " (valor recibido: " + value + ")"
            );
        }
        return value;
    }

    // ========================================
    // CONVERSIONES SEGURAS
    // ========================================

    /**
     * Retorna un String seguro (nunca null, siempre trimmed).
     * Convierte null a String vacío.
     *
     * @param value el String a procesar
     * @return String no-null y trimmed
     */
    public static String safeString(String value) {
        return value != null ? value.trim() : "";
    }

    /**
     * Retorna un valor por defecto si el objeto es null.
     *
     * @param <T> tipo del objeto
     * @param value el valor a verificar
     * @param defaultValue valor por defecto si value es null
     * @return value si no es null, defaultValue en caso contrario
     */
    public static <T> T defaultIfNull(T value, T defaultValue) {
        return value != null ? value : defaultValue;
    }

    // ========================================
    // FUNCIÓN AUXILIAR PARA BI-FUNCTION
    // ========================================

    /**
     * Interfaz funcional para mappers que requieren un parámetro adicional.
     * Similar a BiFunction pero con nombres más descriptivos.
     *
     * @param <T> tipo del objeto de dominio
     * @param <P> tipo del parámetro adicional
     * @param <R> tipo del resultado (DTO)
     */
    @FunctionalInterface
    public interface BiFunction<T, P, R> {
        R apply(T domain, P parameter);
    }
}



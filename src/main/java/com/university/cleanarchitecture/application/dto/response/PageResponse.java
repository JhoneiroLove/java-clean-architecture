package com.university.cleanarchitecture.application.dto.response;

import java.util.List;

/**
 * Response inmutable genérico para listas paginadas.
 * Incluye metadatos de paginación.
 *
 * @param <T> Tipo de los elementos en la lista
 * @param content Lista de elementos
 * @param pageNumber Número de página actual (0-indexed)
 * @param pageSize Tamaño de la página
 * @param totalElements Total de elementos disponibles
 * @param totalPages Total de páginas disponibles
 * @param isFirst Indica si es la primera página
 * @param isLast Indica si es la última página
 * @param hasNext Indica si hay una página siguiente
 * @param hasPrevious Indica si hay una página anterior
 */
public record PageResponse<T>(

        List<T> content,
        int pageNumber,
        int pageSize,
        long totalElements,
        int totalPages,
        boolean isFirst,
        boolean isLast,
        boolean hasNext,
        boolean hasPrevious
) {

    /**
     * Constructor que calcula automáticamente los metadatos.
     */
    public PageResponse(List<T> content, int pageNumber, int pageSize, long totalElements) {
        this(
                content,
                pageNumber,
                pageSize,
                totalElements,
                calculateTotalPages(totalElements, pageSize),
                pageNumber == 0,
                isLastPage(pageNumber, totalElements, pageSize),
                hasNextPage(pageNumber, totalElements, pageSize),
                pageNumber > 0
        );
    }

    private static int calculateTotalPages(long totalElements, int pageSize) {
        return pageSize == 0 ? 0 : (int) Math.ceil((double) totalElements / pageSize);
    }

    private static boolean isLastPage(int pageNumber, long totalElements, int pageSize) {
        int totalPages = calculateTotalPages(totalElements, pageSize);
        return totalPages == 0 || pageNumber >= totalPages - 1;
    }

    private static boolean hasNextPage(int pageNumber, long totalElements, int pageSize) {
        return !isLastPage(pageNumber, totalElements, pageSize);
    }

    /**
     * Retorna el número de elementos en la página actual.
     */
    public int numberOfElements() {
        return content != null ? content.size() : 0;
    }

    /**
     * Verifica si la página está vacía.
     */
    public boolean isEmpty() {
        return content == null || content.isEmpty();
    }
}




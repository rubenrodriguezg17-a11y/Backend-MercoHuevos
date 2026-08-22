package com.mercohuevos.common.dto;

import java.util.List;
import org.springframework.data.domain.Page;

public record PaginaResponseDTO<T>(
        List<T> contenido,
        int paginaActual,
        int totalPaginas,
        long totalElementos,
        int tamanoPagina,
        boolean esUltima) {

    public static <T> PaginaResponseDTO<T> from(Page<T> page) {
        return new PaginaResponseDTO<>(
                page.getContent(),
                page.getNumber(),
                page.getTotalPages(),
                page.getTotalElements(),
                page.getSize(),
                page.isLast());
    }
}
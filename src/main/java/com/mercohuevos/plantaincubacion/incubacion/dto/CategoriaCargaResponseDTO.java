package com.mercohuevos.plantaincubacion.incubacion.dto;

public record CategoriaCargaResponseDTO(
    Long idCategoriaEmbandejado,
    String codigoCategoria,
    Integer cantidadCargada,
    Long idMaquina,
    String maquinaNombre
) {}
package com.mercohuevos.plantaincubacion.dto;

public record CategoriaCargaResponseDTO(
    Long idCategoriaEmbandejado,
    String codigoCategoria,
    Integer cantidadCargada,
    Long idMaquina,
    String maquinaNombre
) {}
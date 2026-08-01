package com.mercohuevos.plantaincubacion.dto;

import java.util.List;

public record LineaGeneticaEmbandejadoResponseDTO(
    Long idLineaGenetica,
    String lineaGeneticaNombre,
    List<LoteFusionadoEmbandejadoResponseDTO> lotesFusionados,
    Integer totalEmbandejadoGen
) {}
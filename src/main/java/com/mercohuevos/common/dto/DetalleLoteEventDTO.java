package com.mercohuevos.common.dto;

import java.util.List;

public record DetalleLoteEventDTO(
    Long idLote,
    String codigoLote,
    Long idLineaGenetica,
    String lineaGeneticaNombre,
    List<ConteoTipoHuevoEventDTO> conteos
) {}
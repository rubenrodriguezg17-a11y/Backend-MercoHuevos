package com.mercohuevos.common.dto;

import java.util.List;

public record DetalleLoteEventDTO(
        String codigoLote,
        String lineaGeneticaNombre,
        List<ConteoTipoHuevoEventDTO> conteos
) {}
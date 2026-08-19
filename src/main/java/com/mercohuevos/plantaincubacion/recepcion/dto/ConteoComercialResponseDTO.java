package com.mercohuevos.plantaincubacion.recepcion.dto;

import java.util.List;

public record ConteoComercialResponseDTO(
        Long idRecepcion,
        List<ComparacionLineaGeneticaDTO> lineas,
        Integer totalGuia,
        Integer totalContado,
        Integer diferenciaTotal,
        boolean conformeTotal,
        List<String> avisosAjuste
) {}
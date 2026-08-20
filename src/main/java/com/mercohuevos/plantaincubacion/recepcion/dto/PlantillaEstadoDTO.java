package com.mercohuevos.plantaincubacion.recepcion.dto;

import java.util.List;

public record PlantillaEstadoDTO(
        Long idPlantilla,
        String codigoFusion,
        Long idLineaGenetica,
        String lineaGeneticaNombre,
        List<String> lotesEsperados,
        List<String> lotesPresentesHoy,
        List<String> lotesFaltantes,
        boolean aplicadaAutomaticamente
) {}
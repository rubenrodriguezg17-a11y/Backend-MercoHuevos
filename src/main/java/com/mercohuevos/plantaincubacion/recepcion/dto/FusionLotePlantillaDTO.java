package com.mercohuevos.plantaincubacion.recepcion.dto;

import java.time.LocalDate;
import java.util.List;

public record FusionLotePlantillaDTO(
        Long idPlantilla,
        Long idLineaGenetica,
        String lineaGeneticaNombre,
        String codigoFusion,
        Boolean activa,
        LocalDate fechaCreacion,
        List<String> codigosLoteGranja
) {}
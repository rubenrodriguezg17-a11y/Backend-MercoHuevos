package com.mercohuevos.plantaincubacion.dto;

import java.time.LocalDate;
import java.util.List;

public record FusionLoteDTO(
        Long idFusionLote,
        String nombre,
        String lineaGeneticaNombre,
        LocalDate fechaCreacion,
        List<String> codigosLoteGranja
) {}
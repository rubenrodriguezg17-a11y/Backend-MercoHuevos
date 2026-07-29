package com.mercohuevos.plantaincubacion.dto;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record EmbandejadoDetalleRequestDTO(

        @NotNull(message = "La fusion de lote es obligatoria")
        Long idFusionLote,

        @Min(value = 0, message = "Los rotos de transporte no pueden ser negativos")
        Integer rotosTransporte,

        @Min(value = 0, message = "Los rotos de embandejado no pueden ser negativos")
        Integer rotosEmbandejado,

        @NotNull(message = "La seleccion descartada es obligatoria")
        @Min(value = 0, message = "La seleccion descartada no puede ser negativa")
        Integer seleccionDescartada,

        String observaciones,

        @NotEmpty(message = "Debe registrar al menos una categoria embandejada")
        @Valid
        List<ConteoCategoriaEmbandejadoRequestDTO> conteos
) {}
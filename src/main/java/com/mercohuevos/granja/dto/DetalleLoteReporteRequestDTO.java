package com.mercohuevos.granja.dto;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record DetalleLoteReporteRequestDTO(

        @NotNull(message = "El idLote es obligatorio")
        Long idLote,

        @Min(value = 0, message = "La cantidad de muertas no puede ser negativa")
        Integer cantidadMuertas,   // opcional: si viene, se registra/actualiza mortalidad de ese dia

        @NotEmpty(message = "Cada lote debe tener al menos un conteo de huevos")
        @Valid
        List<ConteoTipoHuevoRequestDTO> conteos
) {}
package com.mercohuevos.plantaincubacion.despacho.dto;

import java.time.LocalDate;
import com.mercohuevos.plantaincubacion.enums.TipoSalidaConsumo;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record SalidaConsumoRequestDTO(

        @NotNull(message = "La fecha es obligatoria")
        LocalDate fecha,

        @NotNull(message = "La cantidad es obligatoria")
        @Min(value = 1, message = "La cantidad debe ser mayor a 0")
        Integer cantidad,

        @NotNull(message = "El tipo de salida es obligatorio")
        TipoSalidaConsumo tipoSalida,

        String destino,
        String observacion
) {}
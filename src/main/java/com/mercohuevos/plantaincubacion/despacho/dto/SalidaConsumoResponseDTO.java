package com.mercohuevos.plantaincubacion.despacho.dto;

import java.time.LocalDate;
import com.mercohuevos.plantaincubacion.enums.TipoSalidaConsumo;

public record SalidaConsumoResponseDTO(
        Long idSalida,
        LocalDate fecha,
        Integer cantidad,
        TipoSalidaConsumo tipoSalida,
        String destino,
        Integer saldoTotalRestante,
        Boolean anulado
) {}
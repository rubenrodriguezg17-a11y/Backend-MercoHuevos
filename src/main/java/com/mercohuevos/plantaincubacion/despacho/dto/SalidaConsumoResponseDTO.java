package com.mercohuevos.plantaincubacion.despacho.dto;

import java.time.LocalDate;

public record SalidaConsumoResponseDTO(
        Long idSalida,
        LocalDate fecha,
        Integer cantidad,
        String destino,
        Integer saldoTotalRestante   // cuanto queda disponible en todo el consumo despues de esta salida
) {}
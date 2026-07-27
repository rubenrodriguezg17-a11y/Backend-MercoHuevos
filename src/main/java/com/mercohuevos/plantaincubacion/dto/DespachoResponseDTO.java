package com.mercohuevos.plantaincubacion.dto;

import java.time.LocalDate;

public record DespachoResponseDTO(
        Long idDespacho,
        String categoriaDespachoCodigo,
        String cliente,
        Integer cantidad,
        String destino,
        LocalDate fechaDespacho
) {}
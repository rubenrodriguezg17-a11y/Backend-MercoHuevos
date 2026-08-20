package com.mercohuevos.plantaincubacion.recepcion.dto;

import java.time.LocalTime;

public record ConfirmarRecepcionRequestDTO(
        LocalTime horaLlegada
) {}
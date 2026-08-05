package com.mercohuevos.plantaincubacion.incubacion.dto;

import java.time.LocalDateTime;
import java.util.List;

public record MirajeResponseDTO(
        Long idMiraje,
        Long idCarga,
        LocalDateTime fechaMiraje,
        String responsable,
        List<DetalleMirajeLoteResponseDTO> detalles
) {}
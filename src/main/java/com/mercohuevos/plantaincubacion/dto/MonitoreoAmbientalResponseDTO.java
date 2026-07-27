package com.mercohuevos.plantaincubacion.dto;

import java.time.LocalDateTime;

public record MonitoreoAmbientalResponseDTO(
        Long idMonitoreo,
        LocalDateTime fechaHora,
        Double humedad,
        Double temperatura1,
        Double temperatura2,
        Boolean volteo
) {}
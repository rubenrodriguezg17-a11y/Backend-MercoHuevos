package com.mercohuevos.plantaincubacion.vacunacion.dto;

import java.time.LocalDateTime;
import java.util.List;

public record OrdenVacunacionResponseDTO(
        Long idOrdenVacunacion,
        Long idCarga,
        LocalDateTime fechaVacunacion,
        String responsableVacunacion,
        List<DetalleVacunacionClienteLoteResponseDTO> detalles
) {}
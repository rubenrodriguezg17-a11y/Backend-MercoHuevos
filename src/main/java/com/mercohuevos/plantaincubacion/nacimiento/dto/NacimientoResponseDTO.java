package com.mercohuevos.plantaincubacion.nacimiento.dto;

import java.time.LocalDate;
import java.util.List;

public record NacimientoResponseDTO(
        Long idNacimiento,
        Long idCarga,
        LocalDate fechaNacimiento,
        String responsable,
        List<DetalleNacimientoLoteResponseDTO> detalles
) {}
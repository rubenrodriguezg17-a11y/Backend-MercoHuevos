package com.mercohuevos.plantaincubacion.incubacion.dto;

import java.time.LocalDate;
import java.util.List;

public record CargaDetalleResponseDTO(
        Long idCarga,
        Long idLineaGenetica,
        String lineaGeneticaNombre,
        List<LoteFusionCargaResponseDTO> lotes,
        Integer cantidadInicial,
        int bandejasCompletas,
        int residuo,
        LocalDate fechaCarga,
        LocalDate fechaTransferencia,
        LocalDate fechaNacimiento,
        String estado,
        List<AsignacionMaquinaResponseDTO> asignaciones
) {}
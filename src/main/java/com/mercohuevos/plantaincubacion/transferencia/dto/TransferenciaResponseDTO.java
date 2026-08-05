package com.mercohuevos.plantaincubacion.transferencia.dto;

import java.time.LocalDate;
import java.util.List;

public record TransferenciaResponseDTO(
        Long idTransferencia,
        Long idCarga,
        LocalDate fechaTransferencia,
        String responsable,
        List<DetalleTransferenciaLoteResponseDTO> detalles
) {}
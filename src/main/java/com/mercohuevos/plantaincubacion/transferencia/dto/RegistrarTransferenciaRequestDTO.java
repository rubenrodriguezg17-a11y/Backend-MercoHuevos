package com.mercohuevos.plantaincubacion.transferencia.dto;

import java.time.LocalDate;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record RegistrarTransferenciaRequestDTO(

        @NotNull(message = "La fecha de transferencia es obligatoria")
        LocalDate fechaTransferencia,

        @NotNull(message = "El responsable es obligatorio")
        String responsable,

        @NotEmpty(message = "El detalle de transferencia no puede estar vacio")
        @Valid
        List<DetalleTransferenciaLoteRequestDTO> detalleTransferencia
) {}
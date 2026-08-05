package com.mercohuevos.plantaincubacion.transferencia.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record DetalleTransferenciaLoteRequestDTO(

        @NotNull(message = "Debe elegir un lote obligatoriamente")
        Long idCargaLote,

        @NotNull(message = "Debe indicar la incubadora de origen")
        Long idMaquinaOrigen,

        @NotNull(message = "Debe indicar la nacedora de destino")
        Long idNacedoraDestino,

        @NotNull(message = "La cantidad de huevos transferidos es obligatoria")
        @Min(value = 1, message = "No se aceptan valores menores o iguales a 0")
        Integer huevosTransferidos
) {}
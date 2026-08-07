package com.mercohuevos.plantaincubacion.despacho.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record RegistrarDespachoRequestDTO(

        @NotNull(message = "El cliente es obligatorio")
        Long idCliente,

        @NotNull(message = "La fecha de despacho es obligatoria")
        LocalDate fechaDespacho,

        @NotNull(message = "La hora de despacho es obligatoria")
        LocalTime horaDespacho,

        @NotBlank(message = "La placa del vehiculo es obligatoria")
        String placaVehiculo,

        @NotBlank(message = "El nombre del conductor es obligatorio")
        String nombreConductor,

        @NotBlank(message = "El destino es obligatorio")
        String destino,

        @NotEmpty(message = "El detalle de despacho no puede estar vacio")
        @Valid
        List<DetalleDespachoLoteRequestDTO> detalleDespacho
) {}
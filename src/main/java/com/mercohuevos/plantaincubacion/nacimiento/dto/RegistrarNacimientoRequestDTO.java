package com.mercohuevos.plantaincubacion.nacimiento.dto;

import java.time.LocalDate;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record RegistrarNacimientoRequestDTO(
        @NotNull(message = "La fecha de nacimiento es obligatoria")
        LocalDate fechaNacimiento,

        @NotNull(message = "El responsable es obligatorio")
        String responsable,

        @NotEmpty(message = "El detalle de nacimiento no puede estar vacio")
        @Valid
        List<DetalleNacimientoLoteRequestDTO> detalleNacimiento
) {}
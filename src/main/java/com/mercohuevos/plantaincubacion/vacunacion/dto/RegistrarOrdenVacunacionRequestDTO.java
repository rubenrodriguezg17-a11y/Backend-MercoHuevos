package com.mercohuevos.plantaincubacion.vacunacion.dto;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record RegistrarOrdenVacunacionRequestDTO(
        @NotNull(message = "La fecha de vacunacion es obligatoria")
        LocalDateTime fechaVacunacion,

        @NotNull(message = "El responsable es obligatorio")
        String responsableVacunacion,

        @NotEmpty(message = "El detalle de vacunacion no puede estar vacio")
        @Valid
        List<DetalleVacunacionClienteLoteRequestDTO> detalleVacunacion
) {}
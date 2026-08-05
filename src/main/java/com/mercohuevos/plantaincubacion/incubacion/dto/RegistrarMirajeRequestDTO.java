package com.mercohuevos.plantaincubacion.incubacion.dto;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record RegistrarMirajeRequestDTO(

        @NotNull(message = "La fecha del miraje es obligatoria")
        LocalDateTime fechaMiraje,

        @NotNull(message = "El responsable es obligatorio")
        String responsable,

        @NotEmpty(message = "El detalle de miraje no puede estar vacio")
        @Valid
        List<DetalleMirajeLoteRequestDTO> detalleMiraje
) {}
package com.mercohuevos.plantaincubacion.despacho.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DespachoRequestDTO(

        @NotNull(message = "La categoria de despacho es obligatoria")
        Long idCategoriaDespacho,

        @NotBlank(message = "El cliente es obligatorio")
        String cliente,

        @NotNull(message = "La cantidad es obligatoria")
        @Min(value = 1, message = "La cantidad debe ser mayor a 0")
        Integer cantidad,

        @NotBlank(message = "El destino es obligatorio")
        String destino,

        @NotNull(message = "La fecha de despacho es obligatoria")
        LocalDate fechaDespacho
) {}
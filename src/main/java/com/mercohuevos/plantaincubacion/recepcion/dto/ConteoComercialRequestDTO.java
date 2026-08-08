package com.mercohuevos.plantaincubacion.recepcion.dto;

import java.util.List;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

public record ConteoComercialRequestDTO(

        @NotEmpty(message = "Debe enviar el conteo de al menos una linea genetica")
        @Valid
        List<ConteoLineaGeneticaRequestDTO> lineas
) {}
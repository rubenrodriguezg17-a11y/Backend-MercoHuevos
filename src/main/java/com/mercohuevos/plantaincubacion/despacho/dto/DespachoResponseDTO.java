package com.mercohuevos.plantaincubacion.despacho.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record DespachoResponseDTO(
        Long idDespacho,
        Long idCarga,
        Long idCliente,
        String razonSocialCliente,
        LocalDate fechaDespacho,
        LocalTime horaDespacho,
        String placaVehiculo,
        String nombreConductor,
        String destino,
        List<DetalleDespachoLoteResponseDTO> detalles
) {}
package com.mercohuevos.plantaincubacion.vacunacion.service;

import java.util.List;

import com.mercohuevos.plantaincubacion.vacunacion.dto.DetalleVacunacionInfoDTO;
import com.mercohuevos.plantaincubacion.vacunacion.dto.OrdenVacunacionResponseDTO;
import com.mercohuevos.plantaincubacion.vacunacion.dto.RegistrarOrdenVacunacionRequestDTO;

public interface IOrdenVacunacionService {
    OrdenVacunacionResponseDTO registrar(Long idCarga, RegistrarOrdenVacunacionRequestDTO request);
    List<OrdenVacunacionResponseDTO> listarPorCarga(Long idCarga);
    DetalleVacunacionInfoDTO obtenerDetallePorId(Long idDetalleVacunacion);
    List<DetalleVacunacionInfoDTO> listarDetallesPorCarga(Long idCarga);
}
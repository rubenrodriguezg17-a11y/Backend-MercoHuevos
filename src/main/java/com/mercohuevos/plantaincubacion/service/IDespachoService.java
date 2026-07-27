package com.mercohuevos.plantaincubacion.service;

import java.util.List;

import com.mercohuevos.plantaincubacion.dto.DespachoRequestDTO;
import com.mercohuevos.plantaincubacion.dto.DespachoResponseDTO;

public interface IDespachoService {
    DespachoResponseDTO registrar(Long idCarga, DespachoRequestDTO request);
    List<DespachoResponseDTO> listarPorCarga(Long idCarga);
}
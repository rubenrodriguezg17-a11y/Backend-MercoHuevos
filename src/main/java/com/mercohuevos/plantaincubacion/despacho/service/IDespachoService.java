package com.mercohuevos.plantaincubacion.despacho.service;

import java.util.List;

import com.mercohuevos.plantaincubacion.despacho.dto.DespachoRequestDTO;
import com.mercohuevos.plantaincubacion.despacho.dto.DespachoResponseDTO;

public interface IDespachoService {
    DespachoResponseDTO registrar(Long idCarga, DespachoRequestDTO request);
    List<DespachoResponseDTO> listarPorCarga(Long idCarga);
}
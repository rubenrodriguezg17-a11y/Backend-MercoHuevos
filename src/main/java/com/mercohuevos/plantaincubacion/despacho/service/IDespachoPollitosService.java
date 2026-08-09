package com.mercohuevos.plantaincubacion.despacho.service;

import java.util.List;

import com.mercohuevos.plantaincubacion.despacho.dto.DespachoResponseDTO;
import com.mercohuevos.plantaincubacion.despacho.dto.RegistrarDespachoRequestDTO;

public interface IDespachoPollitosService {
    DespachoResponseDTO         registrar(Long idCarga, RegistrarDespachoRequestDTO request);
    List<DespachoResponseDTO>   listarPorCarga(Long idCarga);
    DespachoResponseDTO         obtenerPorId(Long idCarga, Long idDespacho);
}
package com.mercohuevos.plantaincubacion.incubacion.service;

import com.mercohuevos.plantaincubacion.incubacion.dto.MirajeResponseDTO;
import com.mercohuevos.plantaincubacion.incubacion.dto.RegistrarMirajeRequestDTO;

import java.util.List;

public interface IMirajeService {
    MirajeResponseDTO               registrar(Long idCarga, RegistrarMirajeRequestDTO request);
    List<MirajeResponseDTO>         listarPorCarga(Long idCarga);
    Integer                         obtenerHuevosViablesPorCargaLote(Long idCargaLote);
}

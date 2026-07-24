package com.mercohuevos.plantaincubacion.service;

import java.util.List;

import com.mercohuevos.plantaincubacion.dto.FusionLoteDTO;
import com.mercohuevos.plantaincubacion.dto.FusionLoteRequestDTO;

public interface IFusionLoteService {
    FusionLoteDTO crear(FusionLoteRequestDTO request);
    FusionLoteDTO obtenerPorId(Long id);
    List<FusionLoteDTO> listarTodos();
    void eliminar(Long id);
}
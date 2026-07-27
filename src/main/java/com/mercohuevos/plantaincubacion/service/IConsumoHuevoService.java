package com.mercohuevos.plantaincubacion.service;

import java.util.List;

import com.mercohuevos.plantaincubacion.dto.ConsumoHuevoDTO;
import com.mercohuevos.plantaincubacion.dto.ConsumoHuevoResumenDTO;

public interface IConsumoHuevoService {
    List<ConsumoHuevoDTO> listarTodos();
    List<ConsumoHuevoDTO> listarPorFusionLote(Long idFusionLote);
    ConsumoHuevoResumenDTO obtenerResumenPorFusionLote(Long idFusionLote);
}
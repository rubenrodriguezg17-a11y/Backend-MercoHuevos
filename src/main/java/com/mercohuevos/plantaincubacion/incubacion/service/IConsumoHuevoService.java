package com.mercohuevos.plantaincubacion.incubacion.service;

import java.util.List;

import com.mercohuevos.plantaincubacion.incubacion.dto.ConsumoHuevoDTO;
import com.mercohuevos.plantaincubacion.incubacion.dto.ConsumoHuevoResumenDTO;

public interface IConsumoHuevoService {
    List<ConsumoHuevoDTO> listarTodos();
    List<ConsumoHuevoDTO> listarPorFusionLote(Long idFusionLote);
    ConsumoHuevoResumenDTO obtenerResumenPorFusionLote(Long idFusionLote);
    int descontarSaldo(int cantidad);
}
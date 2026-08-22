package com.mercohuevos.plantaincubacion.incubacion.service;

import java.time.LocalDate;
import java.util.List;

import com.mercohuevos.plantaincubacion.enums.OrigenConsumo;
import com.mercohuevos.plantaincubacion.incubacion.dto.ConsumoHuevoDTO;
import com.mercohuevos.plantaincubacion.incubacion.dto.ConsumoHuevoResumenDTO;
import com.mercohuevos.plantaincubacion.shared.model.FusionLote;

public interface IConsumoHuevoService {
    List<ConsumoHuevoDTO>           listarTodos();
    List<ConsumoHuevoDTO>           listarPorFusionLote(Long idFusionLote);
    ConsumoHuevoResumenDTO          obtenerResumenPorFusionLote(Long idFusionLote);
    int                             descontarSaldo(int cantidad);
    void                            revertirSaldo(int cantidad);
    void                            registrarIngreso(FusionLote fusionLote, OrigenConsumo origen, Integer cantidad, LocalDate fecha, String observacion);
    int                             consultarSaldoDisponible();
}
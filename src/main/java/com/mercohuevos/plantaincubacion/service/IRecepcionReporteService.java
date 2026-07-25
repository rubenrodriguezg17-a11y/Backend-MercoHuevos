package com.mercohuevos.plantaincubacion.service;

import java.util.List;

import com.mercohuevos.common.dto.ReporteTrasladoEventDTO;
import com.mercohuevos.plantaincubacion.dto.RecepcionReporteDTO;

public interface IRecepcionReporteService {
    void procesarReporteRecibido(ReporteTrasladoEventDTO reporteEvento);
    RecepcionReporteDTO obtenerPorId(Long id);
    List<RecepcionReporteDTO> listarTodos();
    RecepcionReporteDTO confirmarRecepcion(Long id);
}
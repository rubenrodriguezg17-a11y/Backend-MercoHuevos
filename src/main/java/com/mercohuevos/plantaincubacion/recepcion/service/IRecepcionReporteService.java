package com.mercohuevos.plantaincubacion.recepcion.service;

import java.util.List;

import com.mercohuevos.common.dto.ReporteTrasladoEventDTO;
import com.mercohuevos.plantaincubacion.recepcion.dto.ConteoComercialRequestDTO;
import com.mercohuevos.plantaincubacion.recepcion.dto.ConteoComercialResponseDTO;
import com.mercohuevos.plantaincubacion.recepcion.dto.RecepcionReporteDTO;

public interface IRecepcionReporteService {
    void                        procesarReporteRecibido(ReporteTrasladoEventDTO reporteEvento);
    RecepcionReporteDTO         obtenerPorId(Long id);
    List<RecepcionReporteDTO>   listarTodos();
    RecepcionReporteDTO         confirmarRecepcion(Long id);
    ConteoComercialResponseDTO  compararConteoComercial(Long idRecepcion, ConteoComercialRequestDTO request);
}
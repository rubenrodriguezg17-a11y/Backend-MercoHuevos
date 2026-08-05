package com.mercohuevos.plantaincubacion.incubacion.service;

import java.time.LocalDate;
import java.util.List;

import com.mercohuevos.plantaincubacion.incubacion.dto.PasarACartonRequestDTO;
import com.mercohuevos.plantaincubacion.incubacion.dto.StockIncubableConsultaDTO;
import com.mercohuevos.plantaincubacion.incubacion.dto.StockIncubableDTO;

public interface IStockIncubableService {
    List<StockIncubableDTO> 	listarTodos();
    List<StockIncubableDTO> 	listarPorFusionLote(Long idFusionLote);
    StockIncubableDTO 			pasarACarton(PasarACartonRequestDTO request);
    StockIncubableConsultaDTO 	consultarPorFecha(LocalDate fecha);
}
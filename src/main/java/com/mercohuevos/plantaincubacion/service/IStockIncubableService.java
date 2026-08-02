package com.mercohuevos.plantaincubacion.service;

import java.time.LocalDate;
import java.util.List;

import com.mercohuevos.plantaincubacion.dto.PasarACartonRequestDTO;
import com.mercohuevos.plantaincubacion.dto.StockIncubableConsultaDTO;
import com.mercohuevos.plantaincubacion.dto.StockIncubableDTO;

public interface IStockIncubableService {
    List<StockIncubableDTO> 	listarTodos();
    List<StockIncubableDTO> 	listarPorFusionLote(Long idFusionLote);
    StockIncubableDTO 			pasarACarton(PasarACartonRequestDTO request);
    StockIncubableConsultaDTO 	consultarPorFecha(LocalDate fecha);
}
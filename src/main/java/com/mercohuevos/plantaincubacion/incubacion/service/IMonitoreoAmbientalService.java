package com.mercohuevos.plantaincubacion.incubacion.service;

import java.util.List;

import com.mercohuevos.plantaincubacion.incubacion.dto.MonitoreoAmbientalRequestDTO;
import com.mercohuevos.plantaincubacion.incubacion.dto.MonitoreoAmbientalResponseDTO;

public interface IMonitoreoAmbientalService {
    MonitoreoAmbientalResponseDTO registrar(Long idMaquina, MonitoreoAmbientalRequestDTO request);
    List<MonitoreoAmbientalResponseDTO> listarPorMaquina(Long idMaquina);
}
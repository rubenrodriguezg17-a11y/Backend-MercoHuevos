package com.mercohuevos.plantaincubacion.service;

import java.util.List;

import com.mercohuevos.plantaincubacion.dto.MonitoreoAmbientalRequestDTO;
import com.mercohuevos.plantaincubacion.dto.MonitoreoAmbientalResponseDTO;

public interface IMonitoreoAmbientalService {
    MonitoreoAmbientalResponseDTO registrar(Long idMaquina, MonitoreoAmbientalRequestDTO request);
    List<MonitoreoAmbientalResponseDTO> listarPorMaquina(Long idMaquina);
}
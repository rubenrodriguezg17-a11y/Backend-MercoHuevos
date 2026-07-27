package com.mercohuevos.plantaincubacion.mapper;

import org.mapstruct.Mapper;

import com.mercohuevos.plantaincubacion.dto.MonitoreoAmbientalResponseDTO;
import com.mercohuevos.plantaincubacion.model.MonitoreoAmbiental;

@Mapper(componentModel = "spring")
public interface IMonitoreoAmbientalMapper {
    MonitoreoAmbientalResponseDTO toResponseDTO(MonitoreoAmbiental entity);
}
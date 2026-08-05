package com.mercohuevos.plantaincubacion.incubacion.mapper;

import org.mapstruct.Mapper;

import com.mercohuevos.plantaincubacion.incubacion.dto.MonitoreoAmbientalResponseDTO;
import com.mercohuevos.plantaincubacion.incubacion.model.MonitoreoAmbiental;

@Mapper(componentModel = "spring")
public interface IMonitoreoAmbientalMapper {
    MonitoreoAmbientalResponseDTO toResponseDTO(MonitoreoAmbiental entity);
}
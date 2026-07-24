package com.mercohuevos.plantaincubacion.mapper;

import org.mapstruct.Mapper;

import com.mercohuevos.plantaincubacion.dto.MaquinaDTO;
import com.mercohuevos.plantaincubacion.model.Maquina;

@Mapper(componentModel = "spring")
public interface IMaquinaMapper {
	MaquinaDTO toDTO(Maquina entity);
}

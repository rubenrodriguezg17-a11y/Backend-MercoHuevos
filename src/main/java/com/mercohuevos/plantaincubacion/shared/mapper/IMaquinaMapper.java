package com.mercohuevos.plantaincubacion.shared.mapper;

import org.mapstruct.Mapper;

import com.mercohuevos.plantaincubacion.shared.dto.MaquinaDTO;
import com.mercohuevos.plantaincubacion.shared.model.Maquina;

@Mapper(componentModel = "spring")
public interface IMaquinaMapper {
	MaquinaDTO toDTO(Maquina entity);
}

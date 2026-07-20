package com.mercohuevos.granja.mapper;

import org.mapstruct.Mapper;

import com.mercohuevos.granja.dto.TipoHuevoDTO;
import com.mercohuevos.granja.dto.TipoHuevoRequestDTO;
import com.mercohuevos.granja.model.TipoHuevo;

@Mapper(componentModel = "spring")
public interface ITipoHuevoMapper {
	TipoHuevoDTO toDTO(TipoHuevo entity);
	TipoHuevo toEntity(TipoHuevoRequestDTO dto);
}

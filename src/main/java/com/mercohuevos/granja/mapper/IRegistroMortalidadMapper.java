package com.mercohuevos.granja.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.mercohuevos.granja.dto.RegistroMortalidadResponseDTO;
import com.mercohuevos.granja.model.RegistroMortalidad;

@Mapper(componentModel = "spring")
public interface IRegistroMortalidadMapper {
	
	@Mapping(source = "lote.codigoLote", target= "codigoLote")
	RegistroMortalidadResponseDTO toResponseDTO(RegistroMortalidad entity);
}

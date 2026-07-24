package com.mercohuevos.plantaincubacion.mapper;

import org.mapstruct.Mapper;

import com.mercohuevos.plantaincubacion.dto.CategoriaDespachoDTO;
import com.mercohuevos.plantaincubacion.dto.CategoriaDespachoRequestDTO;
import com.mercohuevos.plantaincubacion.model.CategoriaDespacho;

@Mapper(componentModel = "spring")
public interface ICategoriaDespachoMapper {
	CategoriaDespachoDTO toDTO(CategoriaDespacho entity);
	CategoriaDespacho toEntity(CategoriaDespachoRequestDTO dto);
}

package com.mercohuevos.plantaincubacion.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.mercohuevos.plantaincubacion.dto.DespachoResponseDTO;
import com.mercohuevos.plantaincubacion.model.Despacho;

@Mapper(componentModel = "spring")
public interface IDespachoMapper {

    @Mapping(source = "categoriaDespacho.codigo", target = "categoriaDespachoCodigo")
    DespachoResponseDTO toResponseDTO(Despacho entity);
}
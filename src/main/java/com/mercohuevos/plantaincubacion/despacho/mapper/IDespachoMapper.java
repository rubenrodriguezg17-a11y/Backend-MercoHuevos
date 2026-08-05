package com.mercohuevos.plantaincubacion.despacho.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.mercohuevos.plantaincubacion.despacho.dto.DespachoResponseDTO;
import com.mercohuevos.plantaincubacion.despacho.model.Despacho;

@Mapper(componentModel = "spring")
public interface IDespachoMapper {

    @Mapping(source = "categoriaDespacho.codigo", target = "categoriaDespachoCodigo")
    DespachoResponseDTO toResponseDTO(Despacho entity);
}
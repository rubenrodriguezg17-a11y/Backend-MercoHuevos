package com.mercohuevos.plantaincubacion.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.mercohuevos.plantaincubacion.dto.StockIncubableDTO;
import com.mercohuevos.plantaincubacion.model.StockIncubable;

@Mapper(componentModel = "spring")
public interface IStockIncubableMapper {

    @Mapping(source = "fusionLote.nombre", target = "fusionLoteNombre")
    @Mapping(source = "categoriaEmbandejado.codigo", target = "categoriaEmbandejadoCodigo")
    StockIncubableDTO toDTO(StockIncubable entity);
}
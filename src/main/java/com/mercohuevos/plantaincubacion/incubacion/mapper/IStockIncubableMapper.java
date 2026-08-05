package com.mercohuevos.plantaincubacion.incubacion.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.mercohuevos.plantaincubacion.incubacion.dto.StockIncubableDTO;
import com.mercohuevos.plantaincubacion.incubacion.model.StockIncubable;

@Mapper(componentModel = "spring")
public interface IStockIncubableMapper {

    @Mapping(source = "fusionLote.codigoFusion", target = "fusionLoteNombre")
    @Mapping(source = "categoriaEmbandejado.codigoCategoria", target = "categoriaEmbandejadoCodigo")
    StockIncubableDTO toDTO(StockIncubable entity);
}
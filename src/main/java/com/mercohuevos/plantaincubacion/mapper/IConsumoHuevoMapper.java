package com.mercohuevos.plantaincubacion.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.mercohuevos.plantaincubacion.dto.ConsumoHuevoDTO;
import com.mercohuevos.plantaincubacion.model.ConsumoHuevo;

@Mapper(componentModel = "spring")
public interface IConsumoHuevoMapper {

    @Mapping(source = "fusionLote.nombre", target = "fusionLoteNombre")
    @Mapping(source = "origen", target = "origen")
    ConsumoHuevoDTO toDTO(ConsumoHuevo entity);
}
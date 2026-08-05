package com.mercohuevos.plantaincubacion.incubacion.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.mercohuevos.plantaincubacion.incubacion.dto.ConsumoHuevoDTO;
import com.mercohuevos.plantaincubacion.incubacion.model.ConsumoHuevo;

@Mapper(componentModel = "spring")
public interface IConsumoHuevoMapper {

    @Mapping(source = "fusionLote.codigoFusion", target = "fusionLoteNombre")
    @Mapping(source = "origen", target = "origen")
    ConsumoHuevoDTO toDTO(ConsumoHuevo entity);
}
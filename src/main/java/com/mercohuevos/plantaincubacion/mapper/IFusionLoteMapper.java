package com.mercohuevos.plantaincubacion.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.mercohuevos.plantaincubacion.dto.FusionLoteDTO;
import com.mercohuevos.plantaincubacion.model.FusionLote;

@Mapper(componentModel = "spring")
public interface IFusionLoteMapper {

    @Mapping(target = "codigosLoteGranja", ignore = true)
    FusionLoteDTO toDTO(FusionLote entity);
}
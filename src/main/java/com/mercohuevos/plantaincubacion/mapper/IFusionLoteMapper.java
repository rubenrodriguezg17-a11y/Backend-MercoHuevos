package com.mercohuevos.plantaincubacion.mapper;

import org.mapstruct.Mapper;

import com.mercohuevos.plantaincubacion.dto.FusionLoteDTO;
import com.mercohuevos.plantaincubacion.model.FusionLote;

@Mapper(componentModel = "spring")
public interface IFusionLoteMapper {
    FusionLoteDTO toDTO(FusionLote entity);
}
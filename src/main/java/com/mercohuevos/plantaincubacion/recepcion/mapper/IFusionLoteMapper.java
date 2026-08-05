package com.mercohuevos.plantaincubacion.recepcion.mapper;

import org.mapstruct.Mapper;

import com.mercohuevos.plantaincubacion.recepcion.dto.FusionLoteDTO;
import com.mercohuevos.plantaincubacion.shared.model.FusionLote;

@Mapper(componentModel = "spring")
public interface IFusionLoteMapper {
    FusionLoteDTO toDTO(FusionLote entity);
}
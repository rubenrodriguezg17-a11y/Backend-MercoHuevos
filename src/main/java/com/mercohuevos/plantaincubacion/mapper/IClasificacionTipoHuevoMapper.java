package com.mercohuevos.plantaincubacion.mapper;

import org.mapstruct.Mapper;

import com.mercohuevos.plantaincubacion.dto.ClasificacionTipoHuevoDTO;
import com.mercohuevos.plantaincubacion.dto.ClasificacionTipoHuevoRequestDTO;
import com.mercohuevos.plantaincubacion.model.ClasificacionTipoHuevo;

@Mapper(componentModel = "spring")
public interface IClasificacionTipoHuevoMapper {
    ClasificacionTipoHuevoDTO toDTO(ClasificacionTipoHuevo entity);
    ClasificacionTipoHuevo toEntity(ClasificacionTipoHuevoRequestDTO dto);
}
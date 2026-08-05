package com.mercohuevos.plantaincubacion.recepcion.mapper;

import org.mapstruct.Mapper;

import com.mercohuevos.plantaincubacion.recepcion.dto.ClasificacionTipoHuevoDTO;
import com.mercohuevos.plantaincubacion.recepcion.dto.ClasificacionTipoHuevoRequestDTO;
import com.mercohuevos.plantaincubacion.recepcion.model.ClasificacionTipoHuevo;

@Mapper(componentModel = "spring")
public interface IClasificacionTipoHuevoMapper {
    ClasificacionTipoHuevoDTO toDTO(ClasificacionTipoHuevo entity);
    ClasificacionTipoHuevo toEntity(ClasificacionTipoHuevoRequestDTO dto);
}
package com.mercohuevos.granja.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.mercohuevos.granja.dto.DetalleLoteReporteResponseDTO;
import com.mercohuevos.granja.model.DetalleLoteReporte;

@Mapper(componentModel = "spring", uses = { IConteoTipoHuevoMapper.class })
public interface IDetalleLoteReporteMapper {

    @Mapping(source = "lote.codigoLote", target = "codigoLote")
    @Mapping(source = "lote.lineaGenetica.nombreGen", target = "lineaGeneticaNombre")
    @Mapping(target = "cantidadMuertasDelDia", ignore = true)
    DetalleLoteReporteResponseDTO toResponseDTO(DetalleLoteReporte entity);

    List<DetalleLoteReporteResponseDTO> toResponseDTOList(List<DetalleLoteReporte> entities);
}
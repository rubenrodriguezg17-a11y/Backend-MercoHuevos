package com.mercohuevos.granja.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import com.mercohuevos.granja.dto.ReporteTrasladoResponseDTO;
import com.mercohuevos.granja.model.ReporteTraslado;

@Mapper(componentModel = "spring", uses = { IDetalleLoteReporteMapper.class })
public interface IReporteTrasladoMapper {

    @Mapping(target = "resumen", ignore = true)  // se llena aparte, no es mapeo directo
    @Mapping(source = "estado", target = "estado")
    ReporteTrasladoResponseDTO toResponseDTO(ReporteTraslado entity);
}
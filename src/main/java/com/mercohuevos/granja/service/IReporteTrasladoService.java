package com.mercohuevos.granja.service;

import java.util.List;

import com.mercohuevos.common.dto.PaginaResponseDTO;
import com.mercohuevos.granja.dto.ReporteTrasladoFiltroDTO;
import com.mercohuevos.granja.dto.ReporteTrasladoRequestDTO;
import com.mercohuevos.granja.dto.ReporteTrasladoResponseDTO;

public interface IReporteTrasladoService {
    ReporteTrasladoResponseDTO          crear(ReporteTrasladoRequestDTO request);
    ReporteTrasladoResponseDTO          obtenerPorId(Long id);
    PaginaResponseDTO<ReporteTrasladoResponseDTO> listarPaginado(int page, int size, ReporteTrasladoFiltroDTO filtro);
    ReporteTrasladoResponseDTO          editar(Long id, ReporteTrasladoRequestDTO request);
    List<ReporteTrasladoResponseDTO>    listarPorSemana(int semanasAtras);
    void                                anular(Long id);
}
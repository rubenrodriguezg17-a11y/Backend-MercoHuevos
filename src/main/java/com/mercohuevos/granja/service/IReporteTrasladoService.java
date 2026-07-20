package com.mercohuevos.granja.service;

import java.util.List;

import com.mercohuevos.granja.dto.ReporteTrasladoRequestDTO;
import com.mercohuevos.granja.dto.ReporteTrasladoResponseDTO;

public interface IReporteTrasladoService {
    ReporteTrasladoResponseDTO crear(ReporteTrasladoRequestDTO request);
    ReporteTrasladoResponseDTO obtenerPorId(Long id);
    List<ReporteTrasladoResponseDTO> listarTodos();
    ReporteTrasladoResponseDTO editar(Long id, ReporteTrasladoRequestDTO request);
    void anular(Long id);
}
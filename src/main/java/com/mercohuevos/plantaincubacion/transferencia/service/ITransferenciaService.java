package com.mercohuevos.plantaincubacion.transferencia.service;

import java.util.List;

import com.mercohuevos.plantaincubacion.transferencia.dto.*;

public interface ITransferenciaService {
    TransferenciaResponseDTO                registrar(Long idCarga, RegistrarTransferenciaRequestDTO request);
    List<TransferenciaResponseDTO>          listarPorCarga(Long idCarga);
    List<LoteDisponibleTransferenciaDTO>    listarLotesDisponibles(Long idCarga);
    List<NacedoraDisponibleDTO>             listarNacedorasDisponibles();
    DetalleTransferenciaInfoDTO             obtenerDetallePorId(Long idDetalleTransferencia);
    void                                    liberarDetalle(Long idDetalleTransferencia);
}
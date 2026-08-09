package com.mercohuevos.plantaincubacion.incubacion.service;

import java.util.List;

import com.mercohuevos.plantaincubacion.enums.EstadoCarga;
import com.mercohuevos.plantaincubacion.incubacion.dto.*;
import jakarta.validation.Valid;

public interface ICargaService {
    CargaResponseDTO 				crear(CargaRequestDTO request);
    CargaDetalleResponseDTO 		obtenerPorId(Long id);
    List<CargaDetalleResponseDTO> 	listarCargas();
    List<CargaDisponibleMirajeDTO>  listarDisponiblesParaMirajePorLinea(Long idLineaGenetica);
    List<CargaLoteResumenDTO>       listarLotesPorCarga(Long idCarga);
    CargaLoteResumenDTO 	        obtenerLote(Long idCargaLote);
    void                            cambiarEstado(Long idCarga, EstadoCarga nuevoEstado);
    CargaResponseDTO                editar(Long id, @Valid CargaRequestDTO request);
    void                            anular(Long id);
}
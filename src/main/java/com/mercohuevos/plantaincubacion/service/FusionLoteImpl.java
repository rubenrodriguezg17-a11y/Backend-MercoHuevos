package com.mercohuevos.plantaincubacion.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.mercohuevos.plantaincubacion.dto.*;
import com.mercohuevos.plantaincubacion.enums.EstadoRecepcion;
import com.mercohuevos.plantaincubacion.model.*;
import com.mercohuevos.plantaincubacion.repository.*;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FusionLoteImpl implements IFusionLoteService {

    private final IFusionLoteRepository fusionLoteRepo;
    private final IFusionLoteDetalleRepository fusionDetalleRepo;
    private final ILoteOrigenReporteRepository loteOrigenRepo;
    private final IRecepcionReporteRepository recepcionRepo;

    @Override
    @Transactional
    public FusionLoteDTO crear(FusionLoteRequestDTO request) {
        RecepcionReporte recepcion = recepcionRepo.findById(request.idRecepcion())
            .orElseThrow(() -> new EntityNotFoundException("Recepcion no encontrada: " + request.idRecepcion()));
        validarRecepcionEditable(recepcion);
        
        if ( fusionLoteRepo.existsByCodigoFusion(request.nombreFusion())) {
        	throw new IllegalArgumentException("Ya existe una fusion con el nombre: " + request.nombreFusion());
        }
        List<LoteOrigenReporte> lotesOrigen = cargarYValidarLotes(request.idsLoteOrigen(), recepcion, request.idLineaGenetica());

        FusionLote fusionLote = new FusionLote();
        fusionLote.setRecepcion(recepcion);
        fusionLote.setIdLineaGenetica(request.idLineaGenetica());
        fusionLote.setLineaGeneticaNombre(lotesOrigen.get(0).getLineaGeneticaNombre());
        fusionLote.setActiva(true);
        fusionLote.setCodigoFusion(request.nombreFusion());
        aplicarTotales(fusionLote, lotesOrigen);

        FusionLote guardada = fusionLoteRepo.save(fusionLote);
        guardarDetalles(guardada, lotesOrigen);

        return construirResponse(guardada, lotesOrigen);
    }

    @Override
    @Transactional
    public FusionLoteDTO editar(Long idFusionLote, EditarFusionLoteRequestDTO request) {
        FusionLote fusionLote = buscarFusion(idFusionLote);
        RecepcionReporte recepcion = fusionLote.getRecepcion();
        validarRecepcionEditable(recepcion);
        
        if (!fusionLote.getCodigoFusion().equals(request.nombreFusion())
        		&& fusionLoteRepo.existsByCodigoFusion(request.nombreFusion())) {
        	throw new IllegalArgumentException("Ya existe uan fusion co el nombre: " + request.nombreFusion());
        }

        fusionDetalleRepo.deleteByFusionLote(fusionLote);

        List<LoteOrigenReporte> lotesOrigen = cargarYValidarLotes(request.idsLoteOrigen(), recepcion, fusionLote.getIdLineaGenetica());
        fusionLote.setCodigoFusion(request.nombreFusion());
        aplicarTotales(fusionLote, lotesOrigen);
        FusionLote actualizada = fusionLoteRepo.save(fusionLote);
        guardarDetalles(actualizada, lotesOrigen);

        return construirResponse(actualizada, lotesOrigen);
    }

    @Override
    @Transactional
    public void anular(Long idFusionLote) {
        FusionLote fusionLote = buscarFusion(idFusionLote);
        validarRecepcionEditable(fusionLote.getRecepcion());
        fusionDetalleRepo.deleteByFusionLote(fusionLote);
        fusionLote.setActiva(false);
        fusionLoteRepo.save(fusionLote);
    }

    @Override
    public List<FusionLoteDTO> listarPorRecepcion(Long idRecepcion) {
        RecepcionReporte recepcion = recepcionRepo.findById(idRecepcion)
            .orElseThrow(() -> new EntityNotFoundException("Recepcion no encontrada: " + idRecepcion));
        return fusionLoteRepo.findByRecepcionAndActivaTrue(recepcion).stream()
            .map(f -> construirResponse(f, obtenerLotesOrigen(f)))
            .toList();
    }

    @Override
    public FusionLoteDTO obtenerPorId(Long idFusionLote) {
        FusionLote fusionLote = buscarFusion(idFusionLote);
        return construirResponse(fusionLote, obtenerLotesOrigen(fusionLote));
    }

    // ---------------------- Métodos privados de apoyo ----------------------
    private FusionLote buscarFusion(Long id) {
        return fusionLoteRepo.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("FusionLote no encontrado: " + id));
    }

    private void validarRecepcionEditable(RecepcionReporte recepcion) {
        if (recepcion.getEstado() == EstadoRecepcion.PROCESADO) {
            throw new IllegalStateException("No se pueden modificar fusiones: el embandejado de esta recepcion ya fue confirmado");
        }
    }

    private List<LoteOrigenReporte> cargarYValidarLotes(List<Long> idsLoteOrigen, RecepcionReporte recepcion, Long idLineaGenetica) {
        List<LoteOrigenReporte> lotesOrigen = idsLoteOrigen.stream()
            .map(id -> loteOrigenRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("LoteOrigenReporte no encontrado: " + id)))
            .toList();

        for (LoteOrigenReporte lote : lotesOrigen) {
            if (!lote.getRecepcion().getIdRecepcion().equals(recepcion.getIdRecepcion())) {
                throw new IllegalArgumentException("El lote " + lote.getIdLoteGranja() + " no pertenece a esta recepcion");
            }
            if (!lote.getIdLineaGenetica().equals(idLineaGenetica)) {
                throw new IllegalArgumentException("El lote " + lote.getIdLoteGranja() + " no pertenece a la linea genetica " + idLineaGenetica);
            }
            if (fusionDetalleRepo.existsByLoteOrigenReporte(lote)) {
                throw new IllegalArgumentException("El lote " + lote.getCodigoLoteGranja() + " ya pertenece a otra fusion activa");
            }
        }
        return lotesOrigen;
    }

    private void aplicarTotales(FusionLote fusionLote, List<LoteOrigenReporte> lotesOrigen) {
        int totalIncubable = lotesOrigen.stream().mapToInt(LoteOrigenReporte::getHuevosIncubablesGuia).sum();
        int totalComercial = lotesOrigen.stream().mapToInt(LoteOrigenReporte::getHuevosComercialGuia).sum();
        fusionLote.setHuevosIncubablesGuia(totalIncubable);
        fusionLote.setHuevosComercialGuia(totalComercial);
    }

    private void guardarDetalles(FusionLote fusionLote, List<LoteOrigenReporte> lotesOrigen) {
        List<FusionLoteDetalle> detalles = lotesOrigen.stream().map(lote -> {
            FusionLoteDetalle detalle = new FusionLoteDetalle();
            detalle.setFusionLote(fusionLote);
            detalle.setLoteOrigenReporte(lote);
            return detalle;
        }).toList();
        fusionDetalleRepo.saveAll(detalles);
    }

    private List<LoteOrigenReporte> obtenerLotesOrigen(FusionLote fusionLote) {
        return fusionDetalleRepo.findByFusionLote(fusionLote).stream()
            .map(FusionLoteDetalle::getLoteOrigenReporte)
            .toList();
    }

    private FusionLoteDTO construirResponse(FusionLote fusionLote, List<LoteOrigenReporte> lotesOrigen) {
        List<LoteOrigenSimpleDTO> lotesDTO = lotesOrigen.stream()
            .map(l -> new LoteOrigenSimpleDTO(l.getIdLoteGranja(), l.getCodigoLoteGranja()))
            .toList();

        return new FusionLoteDTO(
            fusionLote.getIdFusionLote(),
            fusionLote.getRecepcion().getIdRecepcion(),
            fusionLote.getIdLineaGenetica(),
            fusionLote.getLineaGeneticaNombre(),
            fusionLote.getCodigoFusion(),
            fusionLote.getHuevosIncubablesGuia(),
            fusionLote.getHuevosComercialGuia(),
            lotesDTO
        );
    }
}
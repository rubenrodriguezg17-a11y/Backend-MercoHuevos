package com.mercohuevos.plantaincubacion.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.mercohuevos.plantaincubacion.dto.*;
import com.mercohuevos.plantaincubacion.enums.EstadoRecepcion;
import com.mercohuevos.plantaincubacion.mapper.IFusionLoteMapper;
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
    private final IRecepcionReporteRepository recepcionRepo;
    private final ICargaRepository cargaRepo;
    private final IFusionLoteMapper mapper;

   @Override
    @Transactional
    public FusionLoteDTO crear(FusionLoteRequestDTO request) {
        RecepcionReporte recepcion = recepcionRepo.findById(request.idRecepcion())
            .orElseThrow(() -> new EntityNotFoundException("Recepcion no encontrada: " + request.idRecepcion()));
        validarRecepcionEditable(recepcion);

        if (fusionLoteRepo.existsByRecepcionAndIdLineaGeneticaAndCodigoFusion(
                recepcion, request.idLineaGenetica(), request.nombreFusion())) {
            throw new IllegalArgumentException("Ya existe una fusion con ese nombre en esta linea genetica y recepcion");
        }

        List<FusionLote> fusionesOrigen = cargarYValidarFusionesOrigen(
            request.idsFusionOrigen(), recepcion, request.idLineaGenetica());

        FusionLote nueva = new FusionLote();
        nueva.setRecepcion(recepcion);
        nueva.setIdLineaGenetica(request.idLineaGenetica());
        nueva.setLineaGeneticaNombre(fusionesOrigen.get(0).getLineaGeneticaNombre());
        nueva.setCodigoFusion(request.nombreFusion());
        nueva.setActiva(true);
        aplicarTotales(nueva, fusionesOrigen);

        FusionLote guardada = fusionLoteRepo.save(nueva);

        moverDetallesYAnularOrigen(guardada, fusionesOrigen);

        return construirResponse(guardada, obtenerLotesOrigen(guardada));
    }

    @Override
    @Transactional
    public FusionLoteDTO editar(Long idFusionLote, EditarFusionLoteRequestDTO request) {
        FusionLote fusionLote = buscarFusion(idFusionLote);
        validarRecepcionEditable(fusionLote.getRecepcion());

        if (!fusionLote.getCodigoFusion().equals(request.nombreFusion())
                && fusionLoteRepo.existsByRecepcionAndIdLineaGeneticaAndCodigoFusion(
                       fusionLote.getRecepcion(), fusionLote.getIdLineaGenetica(), request.nombreFusion())) {
            throw new IllegalArgumentException("Ya existe una fusion con ese nombre en esta linea genetica y recepcion");
        }

        fusionLote.setCodigoFusion(request.nombreFusion());
        FusionLote actualizada = fusionLoteRepo.save(fusionLote);

        return construirResponse(actualizada, obtenerLotesOrigen(actualizada));
    }

    @Override
    @Transactional
    public void anular(Long idFusionLote) {
        FusionLote fusionLote = buscarFusion(idFusionLote);
        validarRecepcionEditable(fusionLote.getRecepcion());

        if (cargaRepo.existsByFusionLote(fusionLote)) {
            throw new IllegalStateException("No se puede anular: esta fusion ya tiene una carga registrada");
        }

        fusionLote.setActiva(false);
        fusionLoteRepo.save(fusionLote);
    }

    @Override
    public FusionLoteDTO obtenerPorId(Long idFusionLote) {
        FusionLote fusionLote = buscarFusion(idFusionLote);
        return construirResponse(fusionLote, obtenerLotesOrigen(fusionLote));
    }


    @Override
    public List<FusionLoteDTO> listarActivasPorRecepcion(Long idRecepcion) {
        RecepcionReporte recepcion = recepcionRepo.findById(idRecepcion)
            .orElseThrow(() -> new EntityNotFoundException("Recepcion no encontrada: " + idRecepcion));
        return fusionLoteRepo.findByRecepcionAndActivaTrue(recepcion).stream()
            .map(f -> construirResponse(f, obtenerLotesOrigen(f)))
            .toList();
    }

    @Override
    public List<FusionLoteDTO> listarAnuladasPorRecepcion(Long idRecepcion) {
        RecepcionReporte recepcion = recepcionRepo.findById(idRecepcion)
            .orElseThrow(() -> new EntityNotFoundException("Recepcion no encontrada: " + idRecepcion));
        return fusionLoteRepo.findByRecepcionAndActivaFalse(recepcion).stream()
            .map(f -> construirResponse(f, obtenerLotesOrigen(f)))
            .toList();
    }

    @Override
    public List<FusionLoteDTO> listarTodasPorRecepcion(Long idRecepcion) {
        RecepcionReporte recepcion = recepcionRepo.findById(idRecepcion)
            .orElseThrow(() -> new EntityNotFoundException("Recepcion no encontrada: " + idRecepcion));
        return fusionLoteRepo.findByRecepcion(recepcion).stream()
            .map(f -> construirResponse(f, obtenerLotesOrigen(f)))
            .toList();
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

    private List<FusionLote> cargarYValidarFusionesOrigen(List<Long> idsFusionOrigen, RecepcionReporte recepcion, Long idLineaGenetica) {
        List<FusionLote> fusiones = fusionLoteRepo.findAllById(idsFusionOrigen);

        if (fusiones.size() != idsFusionOrigen.size()) {
            throw new EntityNotFoundException("Una o mas fusiones de origen no existen");
        }

        for (FusionLote fusion : fusiones) {
            if (!fusion.getActiva()) {
                throw new IllegalArgumentException("La fusion " + fusion.getCodigoFusion() + " ya fue anulada");
            }
            if (!fusion.getRecepcion().getIdRecepcion().equals(recepcion.getIdRecepcion())) {
                throw new IllegalArgumentException("La fusion " + fusion.getCodigoFusion() + " no pertenece a esta recepcion");
            }
            if (!fusion.getIdLineaGenetica().equals(idLineaGenetica)) {
                throw new IllegalArgumentException("La fusion " + fusion.getCodigoFusion() + " no pertenece a la linea genetica " + idLineaGenetica);
            }
            if (cargaRepo.existsByFusionLote(fusion)) {
                throw new IllegalStateException("La fusion " + fusion.getCodigoFusion() + " ya tiene una carga registrada, no se puede fusionar");
            }
        }
        return fusiones;
    }

    private void aplicarTotales(FusionLote nueva, List<FusionLote> fusionesOrigen) {
        int totalIncubable = fusionesOrigen.stream().mapToInt(FusionLote::getHuevosIncubablesGuia).sum();
        int totalComercial = fusionesOrigen.stream().mapToInt(FusionLote::getHuevosComercialGuia).sum();
        nueva.setHuevosIncubablesGuia(totalIncubable);
        nueva.setHuevosComercialGuia(totalComercial);
    }

    private void moverDetallesYAnularOrigen(FusionLote nueva, List<FusionLote> fusionesOrigen) {
        List<FusionLoteDetalle> detalles = fusionDetalleRepo.findByFusionLoteIn(fusionesOrigen);
        detalles.forEach(d -> d.setFusionLote(nueva));
        fusionDetalleRepo.saveAll(detalles);

        fusionesOrigen.forEach(f -> f.setActiva(false));
        fusionLoteRepo.saveAll(fusionesOrigen);
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
            fusionLote.getIdLineaGenetica(),
            fusionLote.getLineaGeneticaNombre(),
            fusionLote.getCodigoFusion(),
            fusionLote.getHuevosIncubablesGuia(),
            fusionLote.getHuevosComercialGuia()
        );
    }
}
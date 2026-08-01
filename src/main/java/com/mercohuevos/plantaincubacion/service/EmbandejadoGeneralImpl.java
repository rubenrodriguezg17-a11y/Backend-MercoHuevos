package com.mercohuevos.plantaincubacion.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.mercohuevos.plantaincubacion.dto.*;
import com.mercohuevos.plantaincubacion.enums.EstadoEmbandejado;
import com.mercohuevos.plantaincubacion.enums.EstadoRecepcion;
import com.mercohuevos.plantaincubacion.model.*;
import com.mercohuevos.plantaincubacion.repository.*;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmbandejadoGeneralImpl implements IEmbandejadoGeneralService {

    private final IEmbandejadoGeneralRepository embandejadoRepo;
    private final IRecepcionReporteRepository recepcionRepo;
    private final IFusionLoteRepository fusionLoteRepo;
    private final ICategoriaEmbandejadoRepository categoriaRepo;

    @Override
    @Transactional
    public EmbandejadoGeneralResponseDTO guardar(EmbandejadoGeneralRequestDTO request) {
        RecepcionReporte recepcion = recepcionRepo.findById(request.idRecepcion())
            .orElseThrow(() -> new EntityNotFoundException("Recepcion no encontrada: " + request.idRecepcion()));

        EmbandejadoGeneral embandejado = embandejadoRepo.findByRecepcion(recepcion).orElseGet(() -> {
            EmbandejadoGeneral nuevo = new EmbandejadoGeneral();
            nuevo.setRecepcion(recepcion);
            nuevo.setFechaEmbandejado(LocalDate.now());
            nuevo.setEstado(EstadoEmbandejado.PENDIENTE);
            return nuevo;
        });

        for (LineaGeneticaEmbandejadoRequestDTO linea : request.lineasGeneticas()) {
            for (LoteFusionadoEmbandejadoRequestDTO loteReq : linea.lotesFusionados()) {
                FusionLote fusionLote = fusionLoteRepo.findById(loteReq.idFusionLote())
                    .orElseThrow(() -> new EntityNotFoundException("FusionLote no encontrado: " + loteReq.idFusionLote()));
                if (!fusionLote.getIdLineaGenetica().equals(linea.idLineaGenetica())) {
                    throw new IllegalArgumentException(
                        "El fusionLote " + loteReq.idFusionLote() + " no pertenece a la linea genetica " + linea.idLineaGenetica());
                }

                EmbandejadoLoteFusion detalle = embandejado.getLotesFusionados().stream()
                    .filter(d -> d.getFusionLote().getIdFusionLote().equals(loteReq.idFusionLote()))
                    .findFirst()
                    .orElseGet(() -> {
                        EmbandejadoLoteFusion nuevoDetalle = new EmbandejadoLoteFusion();
                        nuevoDetalle.setEmbandejadoGeneral(embandejado);
                        nuevoDetalle.setFusionLote(fusionLote);
                        embandejado.getLotesFusionados().add(nuevoDetalle);
                        return nuevoDetalle;
                    });

                detalle.setRotosTransporte(loteReq.rotosTransporte());
                detalle.setRotosEmbandejado(loteReq.rotosEmbandejado());
                detalle.setSeleccionDescartada(loteReq.seleccionDescartada());
                detalle.setObservaciones(loteReq.observaciones());

                detalle.getConteos().clear();
                for (ConteoCategoriaEmbandejadoRequestDTO conteoReq : loteReq.conteos()) {
                    CategoriaEmbandejado categoria = categoriaRepo.findById(conteoReq.idCategoriaEmbandejado())
                        .orElseThrow(() -> new EntityNotFoundException("Categoria no encontrada: " + conteoReq.idCategoriaEmbandejado()));
                    ConteoCategoriaEmbandejado conteo = new ConteoCategoriaEmbandejado();
                    conteo.setEmbandejadoLoteFusion(detalle);
                    conteo.setCategoriaEmbandejado(categoria);
                    conteo.setCantidad(conteoReq.cantidad());
                    detalle.getConteos().add(conteo);
                }
            }
        }

        EmbandejadoGeneral guardado = embandejadoRepo.save(embandejado);
        return construirResponse(guardado);
    }

    @Override
    @Transactional
    public EmbandejadoGeneralResponseDTO confirmar(Long idEmbandejadoGeneral) {
        EmbandejadoGeneral embandejado = embandejadoRepo.findById(idEmbandejadoGeneral)
            .orElseThrow(() -> new EntityNotFoundException("Embandejado no encontrado: " + idEmbandejadoGeneral));

        if (embandejado.getEstado() == EstadoEmbandejado.PROCESADO) {
            throw new IllegalStateException("Este embandejado ya fue confirmado anteriormente");
        }

        embandejado.setEstado(EstadoEmbandejado.PROCESADO);
        embandejadoRepo.save(embandejado);

        RecepcionReporte recepcion = embandejado.getRecepcion();
        recepcion.setEstado(EstadoRecepcion.PROCESADO);
        recepcionRepo.save(recepcion);

        return construirResponse(embandejado);
    }

    @Override
    public EmbandejadoGeneralResponseDTO obtenerPorId(Long id) {
        return construirResponse(embandejadoRepo.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Embandejado no encontrado: " + id)));
    }

    private EmbandejadoGeneralResponseDTO construirResponse(EmbandejadoGeneral embandejado) {
        Map<Long, List<EmbandejadoLoteFusion>> agrupadoPorGenetica = embandejado.getLotesFusionados().stream()
            .collect(Collectors.groupingBy(d -> d.getFusionLote().getIdLineaGenetica()));

        List<LineaGeneticaEmbandejadoResponseDTO> lineasDTO = agrupadoPorGenetica.entrySet().stream()
            .map(entry -> {
                List<EmbandejadoLoteFusion> grupo = entry.getValue();
                String nombreLinea = grupo.get(0).getFusionLote().getLineaGeneticaNombre();
                List<LoteFusionadoEmbandejadoResponseDTO> lotesDTO = grupo.stream()
                    .map(this::construirLoteFusionadoResponse)
                    .toList();
                int totalEmbandejadoGen = lotesDTO.stream()
                    .mapToInt(LoteFusionadoEmbandejadoResponseDTO::totalEmbandejado)
                    .sum();
                return new LineaGeneticaEmbandejadoResponseDTO(entry.getKey(), nombreLinea, lotesDTO, totalEmbandejadoGen);
            }).toList();

        return new EmbandejadoGeneralResponseDTO(
            embandejado.getIdEmbandejadoGeneral(),
            embandejado.getRecepcion().getIdRecepcion(),
            embandejado.getFechaEmbandejado(),
            embandejado.getEstado().name(),
            lineasDTO
        );
    }

    private LoteFusionadoEmbandejadoResponseDTO construirLoteFusionadoResponse(EmbandejadoLoteFusion detalle) {
        FusionLote fusionLote = detalle.getFusionLote();
        List<ConteoCategoriaEmbandejadoResponseDTO> conteosDTO = detalle.getConteos().stream()
            .map(c -> new ConteoCategoriaEmbandejadoResponseDTO(c.getCategoriaEmbandejado().getCodigoCategoria(), c.getCantidad()))
            .toList();

        int totalEmbandejado = detalle.getConteos().stream().mapToInt(ConteoCategoriaEmbandejado::getCantidad).sum();
        int diferenciaGuia = fusionLote.getHuevosIncubablesGuia()
            - (detalle.getRotosTransporte() + detalle.getRotosEmbandejado() + detalle.getSeleccionDescartada())
            - totalEmbandejado;

        return new LoteFusionadoEmbandejadoResponseDTO(
            fusionLote.getIdFusionLote(),
            fusionLote.getCodigoFusion(),
            fusionLote.getHuevosIncubablesGuia(),
            fusionLote.getHuevosComercialGuia(),
            detalle.getRotosTransporte(),
            detalle.getRotosEmbandejado(),
            detalle.getSeleccionDescartada(),
            totalEmbandejado,
            detalle.getObservaciones(),
            conteosDTO,
            diferenciaGuia
        );
    }
}
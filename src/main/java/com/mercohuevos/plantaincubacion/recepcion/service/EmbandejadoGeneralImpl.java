package com.mercohuevos.plantaincubacion.recepcion.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.mercohuevos.plantaincubacion.enums.OrigenConsumo;
import com.mercohuevos.plantaincubacion.incubacion.model.StockIncubable;
import com.mercohuevos.plantaincubacion.incubacion.service.IConsumoHuevoService;
import com.mercohuevos.plantaincubacion.recepcion.dto.*;
import com.mercohuevos.plantaincubacion.recepcion.model.ConteoCategoriaEmbandejado;
import com.mercohuevos.plantaincubacion.recepcion.model.EmbandejadoGeneral;
import com.mercohuevos.plantaincubacion.recepcion.model.EmbandejadoLoteFusion;
import com.mercohuevos.plantaincubacion.recepcion.model.RecepcionReporte;
import com.mercohuevos.plantaincubacion.recepcion.repository.IEmbandejadoGeneralRepository;
import com.mercohuevos.plantaincubacion.recepcion.repository.IRecepcionReporteRepository;
import com.mercohuevos.plantaincubacion.incubacion.service.StockIncubableMovimientoService;
import com.mercohuevos.plantaincubacion.shared.model.CategoriaEmbandejado;
import com.mercohuevos.plantaincubacion.shared.model.FusionLote;
import com.mercohuevos.plantaincubacion.shared.repository.ICategoriaEmbandejadoRepository;
import com.mercohuevos.plantaincubacion.shared.repository.IFusionLoteRepository;
import org.springframework.stereotype.Service;

import com.mercohuevos.plantaincubacion.enums.EstadoEmbandejado;
import com.mercohuevos.plantaincubacion.enums.EstadoRecepcion;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmbandejadoGeneralImpl implements IEmbandejadoGeneralService {

    private static final int TOLERANCIA_DIFERENCIA_GUIA = 1000;

    private final IEmbandejadoGeneralRepository embandejadoRepo;
    private final IRecepcionReporteRepository recepcionRepo;
    private final IFusionLoteRepository fusionLoteRepo;
    private final ICategoriaEmbandejadoRepository categoriaRepo;
    private final StockIncubableMovimientoService stockMovimiento;
    private final IConsumoHuevoService consumoHuevoService;


    @Override
    @Transactional
    public EmbandejadoGeneralResponseDTO guardar(EmbandejadoGeneralRequestDTO request) {
        RecepcionReporte recepcion = recepcionRepo.findById(request.idRecepcion())
            .orElseThrow(() -> new EntityNotFoundException("Recepcion no encontrada: " + request.idRecepcion()));

        if (recepcion.getEstado() == EstadoRecepcion.PENDIENTE) {
            throw new IllegalStateException("La recepcion aun no fue confirmada como recibida");
        }
        if (recepcion.getEstado() == EstadoRecepcion.PROCESADO) {
            throw new IllegalStateException("Esta recepcion ya fue procesada por completo, no se puede editar");
        }

        EmbandejadoGeneral embandejado = embandejadoRepo.findByRecepcion(recepcion).orElseGet(() -> {
            EmbandejadoGeneral nuevo = new EmbandejadoGeneral();
            nuevo.setRecepcion(recepcion);
            nuevo.setFechaEmbandejado(recepcion.getFechaReporte());
            nuevo.setEstado(EstadoEmbandejado.PENDIENTE);
            return nuevo;
        });

        for (LineaGeneticaEmbandejadoRequestDTO linea : request.lineasGeneticas()) {
            for (LoteFusionadoEmbandejadoRequestDTO loteReq : linea.lotesFusionados()) {
                FusionLote fusionLote = fusionLoteRepo.findById(loteReq.idFusionLote())
                    .orElseThrow(() -> new EntityNotFoundException("FusionLote no encontrado: " + loteReq.idFusionLote()));
                if (!fusionLote.getActiva()) {
                    throw new IllegalArgumentException(
                        "La fusion " + fusionLote.getCodigoFusion() + " esta anulada y no puede embandejarse");
                }
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

                // guardamos las cantidades anteriores por categoria ANTES de pisar, para calcular el delta de stock
                Map<Long, Integer> cantidadesAnteriores = detalle.getConteos().stream()
                    .collect(Collectors.toMap(
                        c -> c.getCategoriaEmbandejado().getIdCategoriaEmbandejado(),
                        ConteoCategoriaEmbandejado::getCantidad));

                detalle.setRotosTransporte(loteReq.rotosTransporte());
                detalle.setRotosEmbandejado(loteReq.rotosEmbandejado());
                detalle.setSeleccionDescartada(loteReq.seleccionDescartada());
                detalle.setObservaciones(loteReq.observaciones());

                int totalEmbandejadoNuevo = loteReq.conteos().stream()
                    .mapToInt(ConteoCategoriaEmbandejadoRequestDTO::cantidad).sum();
                int diferenciaGuia = fusionLote.getHuevosIncubablesGuia()
                    - (loteReq.rotosTransporte() + loteReq.rotosEmbandejado() + loteReq.seleccionDescartada())
                    - totalEmbandejadoNuevo;

                if (Math.abs(diferenciaGuia) > TOLERANCIA_DIFERENCIA_GUIA) {
                    throw new IllegalArgumentException(
                        "La diferencia con la guia del lote " + fusionLote.getCodigoFusion() +
                        " (" + diferenciaGuia + " huevos) supera el limite permitido de " +
                        TOLERANCIA_DIFERENCIA_GUIA + ". Verificar el conteo con granja.");
                }

                detalle.getConteos().clear();
                Map<Long, Integer> cantidadesNuevas = new HashMap<>();
                for (ConteoCategoriaEmbandejadoRequestDTO conteoReq : loteReq.conteos()) {
                    CategoriaEmbandejado categoria = categoriaRepo.findById(conteoReq.idCategoriaEmbandejado())
                        .orElseThrow(() -> new EntityNotFoundException("Categoria no encontrada: " + conteoReq.idCategoriaEmbandejado()));
                    ConteoCategoriaEmbandejado conteo = new ConteoCategoriaEmbandejado();
                    conteo.setEmbandejadoLoteFusion(detalle);
                    conteo.setCategoriaEmbandejado(categoria);
                    conteo.setCantidad(conteoReq.cantidad());
                    detalle.getConteos().add(conteo);
                    cantidadesNuevas.put(categoria.getIdCategoriaEmbandejado(), conteoReq.cantidad());

                    // delta de stock: lo nuevo menos lo que ya estaba registrado para esta categoria
                    int anterior = cantidadesAnteriores.getOrDefault(categoria.getIdCategoriaEmbandejado(), 0);
                    int delta = conteoReq.cantidad() - anterior;
                    if (delta != 0) {
                        StockIncubable stockHoy = stockMovimiento.obtenerOCrearStockDeHoy(fusionLote, categoria);
                        stockHoy.setEmbandejadoDia(stockHoy.getEmbandejadoDia() + delta);
                        stockMovimiento.recalcularStockActual(stockHoy);
                        stockMovimiento.guardar(stockHoy);
                    }
                }

                // categorias que estaban antes y ya no vinieron en el request (se quitaron): delta negativo
                for (Map.Entry<Long, Integer> anterior : cantidadesAnteriores.entrySet()) {
                    if (!cantidadesNuevas.containsKey(anterior.getKey())) {
                        CategoriaEmbandejado categoria = categoriaRepo.findById(anterior.getKey())
                            .orElseThrow(() -> new EntityNotFoundException("Categoria no encontrada: " + anterior.getKey()));
                        StockIncubable stockHoy = stockMovimiento.obtenerOCrearStockDeHoy(fusionLote, categoria);
                        stockHoy.setEmbandejadoDia(stockHoy.getEmbandejadoDia() - anterior.getValue());
                        stockMovimiento.recalcularStockActual(stockHoy);
                        stockMovimiento.guardar(stockHoy);
                    }
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

        RecepcionReporte recepcion = embandejado.getRecepcion();
        List<FusionLote> fusionesActivas = fusionLoteRepo.findByRecepcionAndActivaTrue(recepcion);

        List<String> faltantes = fusionesActivas.stream()
            .filter(f -> embandejado.getLotesFusionados().stream()
                .noneMatch(d -> d.getFusionLote().getIdFusionLote().equals(f.getIdFusionLote())))
            .map(FusionLote::getCodigoFusion)
            .toList();

        if (!faltantes.isEmpty()) {
            throw new IllegalStateException(
                "Faltan por embandejar las siguientes fusiones: " + String.join(", ", faltantes));
        }

        embandejado.setEstado(EstadoEmbandejado.PROCESADO);
        embandejadoRepo.save(embandejado);

        embandejado.getLotesFusionados().forEach(detalle ->
                consumoHuevoService.registrarIngreso(
                        detalle.getFusionLote(), OrigenConsumo.DESCARTE_SELECCION,
                        detalle.getSeleccionDescartada(), embandejado.getFechaEmbandejado(),
                        "Descarte en selección - " + detalle.getFusionLote().getCodigoFusion()));

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
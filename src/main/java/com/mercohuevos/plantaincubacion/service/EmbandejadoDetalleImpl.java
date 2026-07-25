package com.mercohuevos.plantaincubacion.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.mercohuevos.plantaincubacion.dto.ConteoCategoriaEmbandejadoRequestDTO;
import com.mercohuevos.plantaincubacion.dto.ConteoCategoriaEmbandejadoResponseDTO;
import com.mercohuevos.plantaincubacion.dto.EmbandejadoDetalleRequestDTO;
import com.mercohuevos.plantaincubacion.dto.EmbandejadoDetalleResponseDTO;
import com.mercohuevos.plantaincubacion.enums.OrigenConsumo;
import com.mercohuevos.plantaincubacion.mapper.IConteoCategoriaEmbandejadoMapper;
import com.mercohuevos.plantaincubacion.model.CategoriaEmbandejado;
import com.mercohuevos.plantaincubacion.model.ConsumoHuevo;
import com.mercohuevos.plantaincubacion.model.ConteoCategoriaEmbandejado;
import com.mercohuevos.plantaincubacion.model.EmbandejadoDetalle;
import com.mercohuevos.plantaincubacion.model.FusionLote;
import com.mercohuevos.plantaincubacion.model.FusionLoteMiembro;
import com.mercohuevos.plantaincubacion.model.LoteOrigenReporte;
import com.mercohuevos.plantaincubacion.model.StockIncubable;
import com.mercohuevos.plantaincubacion.repository.ICategoriaEmbandejadoRepository;
import com.mercohuevos.plantaincubacion.repository.IConsumoHuevoRepository;
import com.mercohuevos.plantaincubacion.repository.IConteoCategoriaEmbandejadoRepository;
import com.mercohuevos.plantaincubacion.repository.IEmbandejadoDetalleRepository;
import com.mercohuevos.plantaincubacion.repository.IFusionLoteMiembroRepository;
import com.mercohuevos.plantaincubacion.repository.IFusionLoteRepository;
import com.mercohuevos.plantaincubacion.repository.ILoteOrigenReporteRepository;
import com.mercohuevos.plantaincubacion.repository.IStockIncubableRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmbandejadoDetalleImpl implements IEmbandejadoDetalleService {

    private final IEmbandejadoDetalleRepository embandejadoRepo;
    private final IConteoCategoriaEmbandejadoRepository conteoRepo;
    private final ILoteOrigenReporteRepository loteOrigenRepo;
    private final IFusionLoteRepository fusionLoteRepo;
    private final IFusionLoteMiembroRepository fusionMiembroRepo;
    private final ICategoriaEmbandejadoRepository categoriaRepo;
    private final IStockIncubableRepository stockRepo;
    private final IConsumoHuevoRepository consumoRepo;
    private final IConteoCategoriaEmbandejadoMapper conteoMapper;

    @Override
    public EmbandejadoDetalleResponseDTO registrar(EmbandejadoDetalleRequestDTO request) {

        LoteOrigenReporte loteOrigen = loteOrigenRepo.findByCodigoLoteGranja(request.codigoLoteGranja())
                .orElseThrow(() -> new EntityNotFoundException(
                        "No se encontro informacion de granja para el lote: " + request.codigoLoteGranja()));

        if (embandejadoRepo.findByCodigoLoteGranja(request.codigoLoteGranja()).isPresent()) {
            throw new IllegalStateException(
                    "El lote " + request.codigoLoteGranja() + " ya fue embandejado anteriormente");
        }

        FusionLote fusionLote = resolverFusionLote(loteOrigen);

        EmbandejadoDetalle detalle = new EmbandejadoDetalle();
        detalle.setRecepcion(loteOrigen.getRecepcion());
        detalle.setFusionLote(fusionLote);
        detalle.setCodigoLoteGranja(loteOrigen.getCodigoLoteGranja());
        detalle.setHuevosIncubablesGuia(loteOrigen.getHuevosIncubablesGuia());
        detalle.setHuevosComercialGuia(loteOrigen.getHuevosComercialGuia());
        detalle.setRotosTransporte(request.rotosTransporte());
        detalle.setRotosEmbandejado(request.rotosEmbandejado());
        detalle.setSeleccionDescartada(request.seleccionDescartada());
        detalle.setObservaciones(request.observaciones());

        EmbandejadoDetalle guardado = embandejadoRepo.save(detalle);

        List<ConteoCategoriaEmbandejado> conteos = request.conteos().stream()
                .map(c -> construirConteo(c, guardado))
                .toList();
        conteoRepo.saveAll(conteos);

        LocalDate fecha = loteOrigen.getRecepcion().getFechaReporte();

        registrarConsumoAutomatico(fusionLote, fecha, request.seleccionDescartada(), loteOrigen.getHuevosComercialGuia());
        actualizarStockIncubable(fusionLote, fecha, conteos);

        return construirResponseCompleto(guardado, conteos);
    }

    @Override
    public EmbandejadoDetalleResponseDTO obtenerPorId(Long id) {
        EmbandejadoDetalle detalle = embandejadoRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Embandejado no encontrado: " + id));

        List<ConteoCategoriaEmbandejado> conteos = conteoRepo.findByEmbandejadoDetalle(detalle);
        return construirResponseCompleto(detalle, conteos);
    }

    // ---------------------- métodos privados de apoyo ----------------------

    private FusionLote resolverFusionLote(LoteOrigenReporte loteOrigen) {
        String codigo = loteOrigen.getCodigoLoteGranja();

        return fusionMiembroRepo.findByCodigoLoteGranja(codigo)
                .map(FusionLoteMiembro::getFusionLote)
                .orElseGet(() -> crearFusionTrivial(loteOrigen));
    }

    private FusionLote crearFusionTrivial(LoteOrigenReporte loteOrigen) {
        FusionLote fusionLote = new FusionLote();
        fusionLote.setNombre(loteOrigen.getCodigoLoteGranja());
        fusionLote.setLineaGeneticaNombre(loteOrigen.getLineaGeneticaNombre());
        fusionLote.setFechaCreacion(LocalDate.now());
        fusionLote.setActivo(true);

        FusionLote guardada = fusionLoteRepo.save(fusionLote);

        FusionLoteMiembro miembro = new FusionLoteMiembro();
        miembro.setFusionLote(guardada);
        miembro.setCodigoLoteGranja(loteOrigen.getCodigoLoteGranja());
        fusionMiembroRepo.save(miembro);

        return guardada;
    }

    private ConteoCategoriaEmbandejado construirConteo(
            ConteoCategoriaEmbandejadoRequestDTO conteoReq, EmbandejadoDetalle detalle) {

        CategoriaEmbandejado categoria = categoriaRepo.findById(conteoReq.idCategoriaEmbandejado())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Categoria de embandejado no encontrada: " + conteoReq.idCategoriaEmbandejado()));

        ConteoCategoriaEmbandejado conteo = new ConteoCategoriaEmbandejado();
        conteo.setEmbandejadoDetalle(detalle);
        conteo.setCategoriaEmbandejado(categoria);
        conteo.setCantidad(conteoReq.cantidad());
        return conteo;
    }

    private void registrarConsumoAutomatico(
            FusionLote fusionLote, LocalDate fecha, Integer seleccionDescartada, Integer huevosComercialGuia) {

        if (seleccionDescartada != null && seleccionDescartada > 0) {
            guardarConsumo(fusionLote, fecha, OrigenConsumo.DESCARTE_SELECCION, seleccionDescartada,
                    "Descarte automatico por seleccion en embandejado");
        }

        if (huevosComercialGuia != null && huevosComercialGuia > 0) {
            guardarConsumo(fusionLote, fecha, OrigenConsumo.COMERCIAL_GRANJA, huevosComercialGuia,
                    "Huevo comercial recibido directo de granja");
        }
    }

    private void guardarConsumo(
            FusionLote fusionLote, LocalDate fecha, OrigenConsumo origen, Integer cantidad, String observacion) {

        ConsumoHuevo consumo = new ConsumoHuevo();
        consumo.setFusionLote(fusionLote);
        consumo.setFecha(fecha);
        consumo.setOrigen(origen);
        consumo.setCantidad(cantidad);
        consumo.setObservacion(observacion);
        consumoRepo.save(consumo);
    }

    private void actualizarStockIncubable(
            FusionLote fusionLote, LocalDate fecha, List<ConteoCategoriaEmbandejado> conteos) {

        for (ConteoCategoriaEmbandejado conteo : conteos) {
            CategoriaEmbandejado categoria = conteo.getCategoriaEmbandejado();

            int stockAnterior = stockRepo
                    .findTopByFusionLoteAndCategoriaEmbandejadoAndFechaLessThanOrderByFechaDesc(
                            fusionLote, categoria, fecha)
                    .map(StockIncubable::getStockActual)
                    .orElse(0);

            StockIncubable stock = stockRepo
                    .findByFusionLoteAndCategoriaEmbandejadoAndFecha(fusionLote, categoria, fecha)
                    .orElseGet(StockIncubable::new);

            stock.setFusionLote(fusionLote);
            stock.setCategoriaEmbandejado(categoria);
            stock.setFecha(fecha);
            stock.setStockDiaAnterior(stockAnterior);
            stock.setEmbandejadoDia(conteo.getCantidad());
            stock.setPasadoACarton(stock.getPasadoACarton() != null ? stock.getPasadoACarton() : 0);
            stock.setCargaIncubadora(stock.getCargaIncubadora() != null ? stock.getCargaIncubadora() : 0);
            stock.setStockActual(stockAnterior + conteo.getCantidad() - stock.getPasadoACarton() - stock.getCargaIncubadora());

            stockRepo.save(stock);
        }
    }

    private EmbandejadoDetalleResponseDTO construirResponseCompleto(
            EmbandejadoDetalle detalle, List<ConteoCategoriaEmbandejado> conteos) {

        List<ConteoCategoriaEmbandejadoResponseDTO> conteosDTO = conteos.stream()
                .map(conteoMapper::toResponseDTO)
                .toList();

        int totalEmbandejado = conteos.stream()
                .mapToInt(ConteoCategoriaEmbandejado::getCantidad)
                .sum();

        return new EmbandejadoDetalleResponseDTO(
                detalle.getIdEmbandejado(),
                detalle.getCodigoLoteGranja(),
                detalle.getFusionLote() != null ? detalle.getFusionLote().getNombre() : null,
                detalle.getHuevosIncubablesGuia(),
                detalle.getHuevosComercialGuia(),
                detalle.getRotosTransporte(),
                detalle.getRotosEmbandejado(),
                detalle.getSeleccionDescartada(),
                totalEmbandejado,
                detalle.getObservaciones(),
                conteosDTO
        );
    }
}
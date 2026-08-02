package com.mercohuevos.plantaincubacion.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.mercohuevos.plantaincubacion.dto.CategoriaStockDTO;
import com.mercohuevos.plantaincubacion.dto.LineaGeneticaStockDTO;
import com.mercohuevos.plantaincubacion.dto.LoteFusionStockDTO;
import com.mercohuevos.plantaincubacion.dto.PasarACartonRequestDTO;
import com.mercohuevos.plantaincubacion.dto.ResumenSemanalStockDTO;
import com.mercohuevos.plantaincubacion.dto.StockIncubableConsultaDTO;
import com.mercohuevos.plantaincubacion.dto.StockIncubableDTO;
import com.mercohuevos.plantaincubacion.enums.OrigenConsumo;
import com.mercohuevos.plantaincubacion.mapper.IStockIncubableMapper;
import com.mercohuevos.plantaincubacion.model.CategoriaEmbandejado;
import com.mercohuevos.plantaincubacion.model.ConsumoHuevo;
import com.mercohuevos.plantaincubacion.model.FusionLote;
import com.mercohuevos.plantaincubacion.model.StockIncubable;
import com.mercohuevos.plantaincubacion.repository.ICategoriaEmbandejadoRepository;
import com.mercohuevos.plantaincubacion.repository.IConsumoHuevoRepository;
import com.mercohuevos.plantaincubacion.repository.IFusionLoteRepository;
import com.mercohuevos.plantaincubacion.repository.IStockIncubableRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StockIncubableImpl implements IStockIncubableService {

    private final StockIncubableMovimientoService stockMovimiento;
    private final IStockIncubableRepository repository;
    private final IFusionLoteRepository fusionLoteRepo;
    private final IStockIncubableMapper mapper;
    private final IConsumoHuevoRepository consumoRepo;
    private final ICategoriaEmbandejadoRepository categoriaRepo;

    @Override
    public List<StockIncubableDTO> listarTodos() {
        return repository.findAll().stream().map(mapper::toDTO).toList();
    }

    @Override
    public List<StockIncubableDTO> listarPorFusionLote(Long idFusionLote) {
        FusionLote fusionLote = fusionLoteRepo.findById(idFusionLote)
                .orElseThrow(() -> new EntityNotFoundException("Fusion de lote no encontrada: " + idFusionLote));

        return repository.findByFusionLote(fusionLote).stream().map(mapper::toDTO).toList();
    }
    
    
    @Override
    public StockIncubableDTO pasarACarton(PasarACartonRequestDTO request) {

        FusionLote fusionLote = fusionLoteRepo.findById(request.idFusionLote())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Fusion de lote no encontrada: " + request.idFusionLote()));

        CategoriaEmbandejado categoria = categoriaRepo.findById(request.idCategoriaEmbandejado())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Categoria de embandejado no encontrada: " + request.idCategoriaEmbandejado()));

        StockIncubable stock = stockMovimiento.obtenerOCrearStockDeHoy(fusionLote, categoria);

        if (stock.getStockActual() < request.cantidad()) {
            throw new IllegalArgumentException(
                    "Stock insuficiente para pasar a carton. Disponible: " + stock.getStockActual() +
                    ", solicitado: " + request.cantidad());
        }

        stock.setPasadoACarton(stock.getPasadoACarton() + request.cantidad());
        stockMovimiento.recalcularStockActual(stock);
        StockIncubable actualizado = stockMovimiento.guardar(stock);

        ConsumoHuevo consumo = new ConsumoHuevo();
        consumo.setFusionLote(fusionLote);
        consumo.setFecha(LocalDate.now());
        consumo.setOrigen(OrigenConsumo.PASADO_A_CARTON);
        consumo.setCantidad(request.cantidad());
        consumo.setObservacion(request.observacion() != null ? request.observacion() : "Pasado a carton manual");
        consumoRepo.save(consumo);

        return mapper.toDTO(actualizado);
    }
    
    @Override
    public StockIncubableConsultaDTO consultarPorFecha(LocalDate fecha) {
    	
    	List<StockIncubable> historicoHastaFecha = repository.findByFechaLessThanEqual(fecha);

        Map<String, StockIncubable> ultimaFilaPorLoteCategoria = new HashMap<>();
        for (StockIncubable s : historicoHastaFecha) {
            String clave = s.getFusionLote().getIdFusionLote() + "-" + s.getCategoriaEmbandejado().getIdCategoriaEmbandejado();
            ultimaFilaPorLoteCategoria.merge(clave, s,
                (actual, nueva) -> nueva.getFecha().isAfter(actual.getFecha()) ? nueva : actual);
        }

        List<StockIncubable> filasDelDia = repository.findByFecha(fecha);

        Map<Long, List<StockIncubable>> porLinea = filasDelDia.stream()
            .collect(Collectors.groupingBy(s -> s.getFusionLote().getIdLineaGenetica()));

        List<LineaGeneticaStockDTO> lineasDTO = porLinea.entrySet().stream()
            .map(entry -> {
                Map<Long, List<StockIncubable>> porLote = entry.getValue().stream()
                    .collect(Collectors.groupingBy(s -> s.getFusionLote().getIdFusionLote()));

                List<LoteFusionStockDTO> lotesDTO = porLote.values().stream()
                    .map(filasLote -> {
                        FusionLote fusionLote = filasLote.get(0).getFusionLote();
                        List<CategoriaStockDTO> categoriasDTO = filasLote.stream()
                            .map(s -> new CategoriaStockDTO(
                                s.getIdStock(),
                                s.getCategoriaEmbandejado().getIdCategoriaEmbandejado(),
                                s.getCategoriaEmbandejado().getCodigoCategoria(),
                                s.getStockDiaAnterior(),
                                s.getEmbandejadoDia(),
                                s.getPasadoACarton(),
                                s.getCargaIncubadora(),
                                s.getStockActual()))
                            .toList();
                        int totalLote = categoriasDTO.stream().mapToInt(CategoriaStockDTO::stockActual).sum();
                        return new LoteFusionStockDTO(
                            fusionLote.getIdFusionLote(), fusionLote.getCodigoFusion(), categoriasDTO, totalLote);
                    })
                    .toList();

                String nombreLinea = entry.getValue().get(0).getFusionLote().getLineaGeneticaNombre();
                int totalLinea = lotesDTO.stream().mapToInt(LoteFusionStockDTO::totalLote).sum();

                return new LineaGeneticaStockDTO(entry.getKey(), nombreLinea, totalLinea, lotesDTO);
            })
            .toList();

        return new StockIncubableConsultaDTO(fecha, lineasDTO, construirResumenSemanal(fecha));
    }

    private List<ResumenSemanalStockDTO> construirResumenSemanal(LocalDate fecha) {
        LocalDate lunes = fecha.with(DayOfWeek.MONDAY);
        LocalDate domingo = fecha.with(DayOfWeek.SUNDAY);

        List<StockIncubable> filasSemana = repository.findByFechaBetween(lunes, domingo);

        Map<Long, List<StockIncubable>> porLinea = filasSemana.stream()
            .collect(Collectors.groupingBy(s -> s.getFusionLote().getIdLineaGenetica()));

        WeekFields wf = WeekFields.ISO;
        int anio = fecha.get(wf.weekBasedYear());
        int semana = fecha.get(wf.weekOfWeekBasedYear());

        return porLinea.entrySet().stream()
            .map(entry -> {
                List<StockIncubable> filas = entry.getValue();
                String nombreLinea = filas.get(0).getFusionLote().getLineaGeneticaNombre();

                int totalEmbandejado = filas.stream().mapToInt(StockIncubable::getEmbandejadoDia).sum();
                int totalCargado = filas.stream().mapToInt(StockIncubable::getCargaIncubadora).sum();
                int totalCarton = filas.stream().mapToInt(StockIncubable::getPasadoACarton).sum();

                int stockFinal = filas.stream()
                    .filter(s -> s.getFecha().equals(fecha))
                    .mapToInt(StockIncubable::getStockActual)
                    .sum();

                return new ResumenSemanalStockDTO(
                    anio, semana, entry.getKey(), nombreLinea,
                    totalEmbandejado, totalCargado, totalCarton, stockFinal);
            })
            .toList();
    }
}
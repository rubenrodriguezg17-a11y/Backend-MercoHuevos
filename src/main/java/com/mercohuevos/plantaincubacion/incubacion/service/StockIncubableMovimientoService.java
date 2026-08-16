package com.mercohuevos.plantaincubacion.incubacion.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.mercohuevos.plantaincubacion.shared.model.CategoriaEmbandejado;
import com.mercohuevos.plantaincubacion.shared.model.FusionLote;
import com.mercohuevos.plantaincubacion.incubacion.model.StockIncubable;
import com.mercohuevos.plantaincubacion.incubacion.repository.IStockIncubableRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StockIncubableMovimientoService {

    private final IStockIncubableRepository stockRepo;

    public StockIncubable obtenerOCrear(FusionLote fusionLote, CategoriaEmbandejado categoria, LocalDate fecha) {
        return stockRepo.findByFusionLoteAndCategoriaEmbandejadoAndFecha(fusionLote, categoria, fecha)
                .orElseGet(() -> {
                    int stockAnterior = stockRepo
                            .findTopByFusionLoteAndCategoriaEmbandejadoAndFechaLessThanOrderByFechaDesc(fusionLote, categoria, fecha)
                            .map(StockIncubable::getStockActual)
                            .orElse(0);

                    StockIncubable nuevo = new StockIncubable();
                    nuevo.setFusionLote(fusionLote);
                    nuevo.setCategoriaEmbandejado(categoria);
                    nuevo.setFecha(fecha);
                    nuevo.setStockDiaAnterior(stockAnterior);
                    nuevo.setEmbandejadoDia(0);
                    nuevo.setPasadoACarton(0);
                    nuevo.setCargaIncubadora(0);
                    nuevo.setStockActual(stockAnterior);
                    return stockRepo.save(nuevo);
                });
    }

    public StockIncubable obtenerOCrearStockDeHoy(FusionLote fusionLote, CategoriaEmbandejado categoria) {
        return obtenerOCrear(fusionLote, categoria, LocalDate.now());
    }

    public void recalcularStockActual(StockIncubable stock) {
        stock.setStockActual(
                stock.getStockDiaAnterior() + stock.getEmbandejadoDia()
                        - stock.getPasadoACarton() - stock.getCargaIncubadora());
    }

    public StockIncubable guardar(StockIncubable stock) {
        return stockRepo.save(stock);
    }

    public void recalcularEnCascadaDesde(StockIncubable stock) {
        List<StockIncubable> posteriores = stockRepo.findByFusionLoteAndCategoriaEmbandejadoAndFechaGreaterThanOrderByFechaAsc(
                stock.getFusionLote(), stock.getCategoriaEmbandejado(), stock.getFecha());

        int arrastre = stock.getStockActual();

        for (StockIncubable siguiente : posteriores) {
            siguiente.setStockDiaAnterior(arrastre);
            recalcularStockActual(siguiente);
            stockRepo.save(siguiente);
            arrastre = siguiente.getStockActual();
        }
    }
}
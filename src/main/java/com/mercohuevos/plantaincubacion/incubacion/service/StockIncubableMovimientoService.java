package com.mercohuevos.plantaincubacion.incubacion.service;

import java.time.LocalDate;

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

    public StockIncubable obtenerOCrearStockDeHoy(FusionLote fusionLote, CategoriaEmbandejado categoria) {
        LocalDate hoy = LocalDate.now();

        return stockRepo.findByFusionLoteAndCategoriaEmbandejadoAndFecha(fusionLote, categoria, hoy)
            .orElseGet(() -> {
                int stockAnterior = stockRepo
                    .findTopByFusionLoteAndCategoriaEmbandejadoAndFechaLessThanOrderByFechaDesc(fusionLote, categoria, hoy)
                    .map(StockIncubable::getStockActual)
                    .orElse(0);

                StockIncubable nuevo = new StockIncubable();
                nuevo.setFusionLote(fusionLote);
                nuevo.setCategoriaEmbandejado(categoria);
                nuevo.setFecha(hoy);
                nuevo.setStockDiaAnterior(stockAnterior);
                nuevo.setEmbandejadoDia(0);
                nuevo.setPasadoACarton(0);
                nuevo.setCargaIncubadora(0);
                nuevo.setStockActual(stockAnterior);
                return stockRepo.save(nuevo);
            });
    }

    public void recalcularStockActual(StockIncubable stock) {
        stock.setStockActual(
            stock.getStockDiaAnterior() + stock.getEmbandejadoDia()
            - stock.getPasadoACarton() - stock.getCargaIncubadora());
    }

    public StockIncubable guardar(StockIncubable stock) {
        return stockRepo.save(stock);
    }
}
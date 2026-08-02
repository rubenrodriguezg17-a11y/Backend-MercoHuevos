package com.mercohuevos.plantaincubacion.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mercohuevos.plantaincubacion.model.CategoriaEmbandejado;
import com.mercohuevos.plantaincubacion.model.FusionLote;
import com.mercohuevos.plantaincubacion.model.StockIncubable;

public interface IStockIncubableRepository extends JpaRepository<StockIncubable, Long> {

    Optional<StockIncubable> findByFusionLoteAndCategoriaEmbandejadoAndFecha(
            FusionLote fusionLote, CategoriaEmbandejado categoria, LocalDate fecha);

    Optional<StockIncubable> findTopByFusionLoteAndCategoriaEmbandejadoAndFechaLessThanOrderByFechaDesc(
            FusionLote fusionLote, CategoriaEmbandejado categoria, LocalDate fecha);

    List<StockIncubable> findByFusionLote(FusionLote fusionLote);

    List<StockIncubable> findByFecha(LocalDate fecha);

    List<StockIncubable> findByFechaBetween(LocalDate inicio, LocalDate fin);
    
    List<StockIncubable> findByFechaLessThanEqual(LocalDate fecha);
}
package com.mercohuevos.plantaincubacion.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mercohuevos.plantaincubacion.model.EmbandejadoDetalle;
import com.mercohuevos.plantaincubacion.model.FusionLote;
import com.mercohuevos.plantaincubacion.model.RecepcionReporte;

public interface IEmbandejadoDetalleRepository extends JpaRepository<EmbandejadoDetalle, Long> {
    boolean existsByRecepcionAndFusionLote(RecepcionReporte recepcion, FusionLote fusionLote);
}
package com.mercohuevos.plantaincubacion.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import com.mercohuevos.plantaincubacion.model.FusionLote;
import com.mercohuevos.plantaincubacion.model.FusionLoteDetalle;
import com.mercohuevos.plantaincubacion.model.LoteOrigenReporte;

public interface IFusionLoteDetalleRepository extends JpaRepository<FusionLoteDetalle, Long> {
    List<FusionLoteDetalle> 	findByFusionLote(FusionLote fusionLote);
    List<FusionLoteDetalle> 	findByFusionLoteIn(List<FusionLote> fusiones);
    boolean 				 	existsByLoteOrigenReporte(LoteOrigenReporte lote);
    void 						deleteByFusionLote(FusionLote fusionLote);
}
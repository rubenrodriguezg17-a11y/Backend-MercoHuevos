package com.mercohuevos.plantaincubacion.shared.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.mercohuevos.plantaincubacion.shared.model.FusionLote;
import com.mercohuevos.plantaincubacion.recepcion.model.RecepcionReporte;

public interface IFusionLoteRepository extends JpaRepository<FusionLote, Long> {
    List<FusionLote> 	findByRecepcionAndActivaTrue(RecepcionReporte recepcion);
    List<FusionLote> 	findByRecepcionAndActivaFalse(RecepcionReporte recepcion);
    List<FusionLote> 	findByRecepcion(RecepcionReporte recepcion);
    boolean 			existsByRecepcionAndIdLineaGeneticaAndCodigoFusion(
        RecepcionReporte recepcion, Long idLineaGenetica, String codigoFusion);
}
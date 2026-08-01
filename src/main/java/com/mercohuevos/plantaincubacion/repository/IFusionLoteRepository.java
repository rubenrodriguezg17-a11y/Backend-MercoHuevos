package com.mercohuevos.plantaincubacion.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.mercohuevos.plantaincubacion.model.FusionLote;
import com.mercohuevos.plantaincubacion.model.RecepcionReporte;

public interface IFusionLoteRepository extends JpaRepository<FusionLote, Long> {
    List<FusionLote> findByRecepcionAndActivaTrue(RecepcionReporte recepcion);
    boolean existsByCodigoFusion(String codigoFusion);
    }
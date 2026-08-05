package com.mercohuevos.plantaincubacion.incubacion.repository;

import java.util.List;
import java.util.Optional;

import com.mercohuevos.plantaincubacion.shared.model.FusionLote;
import org.springframework.data.jpa.repository.JpaRepository;

import com.mercohuevos.plantaincubacion.incubacion.model.Carga;
import com.mercohuevos.plantaincubacion.incubacion.model.CargaLote;

public interface ICargaLoteRepository extends JpaRepository<CargaLote, Long> {
    Optional<CargaLote> findByCargaAndFusionLote(Carga carga, FusionLote fusionLote);
    List<CargaLote> findByCarga(Carga carga);
    boolean existsByFusionLote(FusionLote fusionLote);
}
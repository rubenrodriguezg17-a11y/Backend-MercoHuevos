package com.mercohuevos.plantaincubacion.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mercohuevos.plantaincubacion.model.FusionLote;
import com.mercohuevos.plantaincubacion.model.FusionLoteMiembro;

public interface IFusionLoteMiembroRepository extends JpaRepository<FusionLoteMiembro, Long> {
    List<FusionLoteMiembro> findByFusionLote(FusionLote fusionLote);
    Optional<FusionLoteMiembro> findByCodigoLoteGranja(String codigoLoteGranja);
    boolean existsByCodigoLoteGranja(String codigoLoteGranja);
}
package com.mercohuevos.plantaincubacion.shared.repository;

import com.mercohuevos.plantaincubacion.shared.model.FusionLotePlantilla;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IFusionLotePlantillaRepository extends JpaRepository<FusionLotePlantilla, Long> {
    Optional<FusionLotePlantilla> findByIdLineaGeneticaAndCodigoFusion(Long idLineaGenetica, String codigoFusion);
    List<FusionLotePlantilla> findByActivaTrue();
    List<FusionLotePlantilla> findByIdLineaGeneticaInAndActivaTrue(List<Long> idsLineaGenetica);
}
package com.mercohuevos.plantaincubacion.recepcion.repository;

import com.mercohuevos.plantaincubacion.shared.model.FusionLotePlantilla;
import com.mercohuevos.plantaincubacion.recepcion.model.FusionLotePlantillaDetalle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IFusionLotePlantillaDetalleRepository extends JpaRepository<FusionLotePlantillaDetalle, Long> {
    List<FusionLotePlantillaDetalle> findByPlantilla(FusionLotePlantilla plantilla);
    List<FusionLotePlantillaDetalle> findByPlantillaIn(List<FusionLotePlantilla> plantillas);
    void deleteByPlantilla(FusionLotePlantilla plantilla);
}
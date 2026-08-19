package com.mercohuevos.plantaincubacion.incubacion.repository;

import java.util.List;

import com.mercohuevos.plantaincubacion.enums.OrigenConsumo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.mercohuevos.plantaincubacion.incubacion.model.ConsumoHuevo;
import com.mercohuevos.plantaincubacion.shared.model.FusionLote;

public interface IConsumoHuevoRepository extends JpaRepository<ConsumoHuevo, Long> {
	List<ConsumoHuevo> findByFusionLote(FusionLote fusionLote);

	@Query("SELECT c FROM ConsumoHuevo c WHERE c.cantidad > c.cantidadDescontada ORDER BY c.fecha ASC, c.idConsumo ASC")
	List<ConsumoHuevo> findConSaldoDisponibleOrdenadoPorFecha();

	@Query("SELECT c FROM ConsumoHuevo c WHERE c.cantidadDescontada > 0 ORDER BY c.fecha DESC, c.idConsumo DESC")
	List<ConsumoHuevo> findConDescuentoOrdenadoPorFechaDesc();

	@Query("SELECT COALESCE(SUM(c.cantidad - c.cantidadDescontada), 0) FROM ConsumoHuevo c")
	Integer sumSaldoTotalDisponible();

	List<ConsumoHuevo> findByFusionLoteAndOrigen(FusionLote fusionLote, OrigenConsumo origen);
}
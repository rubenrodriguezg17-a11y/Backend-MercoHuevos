package com.mercohuevos.plantaincubacion.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.mercohuevos.plantaincubacion.model.ConsumoHuevo;
import com.mercohuevos.plantaincubacion.model.FusionLote;

public interface IConsumoHuevoRepository extends JpaRepository<ConsumoHuevo, Long> {
	List<ConsumoHuevo> findByFusionLote(FusionLote fusionLote);
	
	@Query("SELECT c FROM ConsumoHuevo c WHERE c.cantidad > c.cantidadDescontada ORDER BY c.fecha ASC, c.idConsumo ASC")
	List<ConsumoHuevo> findConSaldoDisponibleOrdenadoPorFecha();

	@Query("SELECT COALESCE(SUM(c.cantidad - c.cantidadDescontada), 0) FROM ConsumoHuevo c")
	Integer sumSaldoTotalDisponible();
}
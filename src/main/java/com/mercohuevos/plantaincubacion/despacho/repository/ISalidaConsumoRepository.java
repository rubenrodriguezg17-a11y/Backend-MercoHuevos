package com.mercohuevos.plantaincubacion.despacho.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mercohuevos.plantaincubacion.despacho.model.SalidaConsumo;

import java.util.List;

public interface ISalidaConsumoRepository extends JpaRepository<SalidaConsumo, Long> {
    List<SalidaConsumo> findAllByOrderByFechaDesc();
    List<SalidaConsumo> findAllByOrderByFechaDescIdSalidaDesc();

}
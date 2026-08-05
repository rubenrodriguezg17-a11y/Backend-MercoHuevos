package com.mercohuevos.plantaincubacion.despacho.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mercohuevos.plantaincubacion.despacho.model.SalidaConsumo;

public interface ISalidaConsumoRepository extends JpaRepository<SalidaConsumo, Long> {
}
package com.mercohuevos.plantaincubacion.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mercohuevos.plantaincubacion.model.SalidaConsumo;

public interface ISalidaConsumoRepository extends JpaRepository<SalidaConsumo, Long> {
}
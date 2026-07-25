package com.mercohuevos.plantaincubacion.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mercohuevos.plantaincubacion.model.ConsumoHuevo;

public interface IConsumoHuevoRepository extends JpaRepository<ConsumoHuevo, Long> {
}
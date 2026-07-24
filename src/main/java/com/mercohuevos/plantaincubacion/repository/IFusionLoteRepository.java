package com.mercohuevos.plantaincubacion.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mercohuevos.plantaincubacion.model.FusionLote;

public interface IFusionLoteRepository extends JpaRepository<FusionLote, Long> {
}
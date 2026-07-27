package com.mercohuevos.plantaincubacion.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mercohuevos.plantaincubacion.model.Carga;
import com.mercohuevos.plantaincubacion.model.Despacho;

public interface IDespachoRepository extends JpaRepository<Despacho, Long> {
    List<Despacho> findByCarga(Carga carga);
}
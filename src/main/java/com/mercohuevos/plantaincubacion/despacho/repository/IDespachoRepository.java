package com.mercohuevos.plantaincubacion.despacho.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mercohuevos.plantaincubacion.incubacion.model.Carga;
import com.mercohuevos.plantaincubacion.despacho.model.Despacho;

public interface IDespachoRepository extends JpaRepository<Despacho, Long> {
    List<Despacho> findByCarga(Carga carga);
}
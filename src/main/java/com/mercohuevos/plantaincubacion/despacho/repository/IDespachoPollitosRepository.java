package com.mercohuevos.plantaincubacion.despacho.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mercohuevos.plantaincubacion.despacho.model.DespachoPollitos;

public interface IDespachoPollitosRepository extends JpaRepository<DespachoPollitos, Long> {
    List<DespachoPollitos> findByIdCargaOrderByFechaDespachoAsc(Long idCarga);
    Optional<DespachoPollitos> findByIdDespachoAndIdCarga(Long idDespacho, Long idCarga);
}
package com.mercohuevos.plantaincubacion.transferencia.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mercohuevos.plantaincubacion.transferencia.model.Transferencia;

public interface ITransferenciaRepository extends JpaRepository<Transferencia, Long> {
    List<Transferencia> findByIdCargaOrderByFechaTransferenciaAsc(Long idCarga);
}
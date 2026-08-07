package com.mercohuevos.plantaincubacion.vacunacion.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mercohuevos.plantaincubacion.vacunacion.model.EsquemaVacunacionCliente;

public interface IEsquemaVacunacionClienteRepository extends JpaRepository<EsquemaVacunacionCliente, Long> {
    List<EsquemaVacunacionCliente> findByCliente_IdCliente(Long idCliente);
}
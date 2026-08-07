package com.mercohuevos.plantaincubacion.vacunacion.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mercohuevos.plantaincubacion.vacunacion.model.DetalleVacunacionClienteLote;

import java.util.List;

public interface IDetalleVacunacionClienteLoteRepository extends JpaRepository<DetalleVacunacionClienteLote, Long> {
    boolean existsByIdDetalleNacimiento(Long idDetalleNacimiento);
    List<DetalleVacunacionClienteLote> findByOrdenVacunacion_IdCarga(Long idCarga);
}
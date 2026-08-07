package com.mercohuevos.plantaincubacion.vacunacion.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mercohuevos.plantaincubacion.vacunacion.model.OrdenVacunacion;

public interface IOrdenVacunacionRepository extends JpaRepository<OrdenVacunacion, Long> {
    List<OrdenVacunacion> findByIdCargaOrderByFechaVacunacionAsc(Long idCarga);
}
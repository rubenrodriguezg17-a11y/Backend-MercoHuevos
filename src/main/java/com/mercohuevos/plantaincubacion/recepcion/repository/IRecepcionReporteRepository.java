package com.mercohuevos.plantaincubacion.recepcion.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mercohuevos.plantaincubacion.recepcion.model.RecepcionReporte;

public interface IRecepcionReporteRepository extends JpaRepository<RecepcionReporte, Long> {
    boolean existsByIdReporteGranja(Long idReporteGranja);
    Optional<RecepcionReporte> findByIdReporteGranja(Long idReporteGranja);
}
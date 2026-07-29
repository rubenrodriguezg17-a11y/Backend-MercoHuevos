package com.mercohuevos.plantaincubacion.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mercohuevos.plantaincubacion.model.RecepcionReporte;

public interface IRecepcionReporteRepository extends JpaRepository<RecepcionReporte, Long> {
    boolean existsByIdReporteGranja(Long idReporteGranja);
    Optional<RecepcionReporte> findByIdReporteGranja(Long idReporteGranja);
}
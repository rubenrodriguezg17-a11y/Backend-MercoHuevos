package com.mercohuevos.granja.repository;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mercohuevos.granja.model.ReporteTraslado;

public interface IReporteTrasladoRepository extends JpaRepository<ReporteTraslado, Long> {
    long countByFechaBetween(LocalDate inicio, LocalDate fin);
    boolean existsByFecha(LocalDate fecha);
    boolean existsByFechaAndIdReporteNot(LocalDate fecha, Long idReporte);
}
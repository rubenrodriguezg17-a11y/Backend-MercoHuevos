package com.mercohuevos.granja.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mercohuevos.granja.model.ReporteTraslado;

public interface IReporteTrasladoRepository extends JpaRepository<ReporteTraslado, Long>, JpaSpecificationExecutor <ReporteTraslado>{
    
    @Query("SELECT DISTINCT r FROM ReporteTraslado r " +
           "LEFT JOIN FETCH r.detalles d " +
           "LEFT JOIN FETCH d.lote l " +
           "LEFT JOIN FETCH l.lineaGenetica g " +
           "LEFT JOIN FETCH d.conteos c " +
           "LEFT JOIN FETCH c.tipoHuevo th " +
           "WHERE r.idReporte = :idReporte")
    Optional<ReporteTraslado> findByIdWithGraph(@Param("idReporte") Long idReporte);

    long countByFechaBetween(LocalDate inicio, LocalDate fin);
    boolean existsByFecha(LocalDate fecha);
    boolean existsByFechaAndIdReporteNot(LocalDate fecha, Long idReporte);

    Page<ReporteTraslado> findByFechaBetweenOrderByFechaDesc(LocalDate inicio, LocalDate fin, Pageable pageable);
}

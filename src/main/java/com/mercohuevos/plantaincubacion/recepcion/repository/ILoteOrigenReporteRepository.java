package com.mercohuevos.plantaincubacion.recepcion.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mercohuevos.plantaincubacion.recepcion.model.RecepcionReporte;
import com.mercohuevos.plantaincubacion.recepcion.model.LoteOrigenReporte;

public interface ILoteOrigenReporteRepository extends JpaRepository<LoteOrigenReporte, Long> {
    List<LoteOrigenReporte> findByRecepcion(RecepcionReporte recepcion);
    Optional<LoteOrigenReporte> findByCodigoLoteGranja(String codigoLoteGranja);
    List<LoteOrigenReporte> findByCodigoLoteGranjaIn(List<String> codigos);
    List<LoteOrigenReporte> findByCodigoLoteGranjaInAndRecepcion_FechaReporte(
            List<String> codigos, LocalDate fechaReporte);
}
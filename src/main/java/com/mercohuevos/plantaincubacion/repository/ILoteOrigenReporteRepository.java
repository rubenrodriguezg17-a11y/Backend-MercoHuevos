package com.mercohuevos.plantaincubacion.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mercohuevos.plantaincubacion.model.RecepcionReporte;
import com.mercohuevos.plantaincubacion.model.LoteOrigenReporte;

public interface ILoteOrigenReporteRepository extends JpaRepository<LoteOrigenReporte, Long> {
    List<LoteOrigenReporte> findByRecepcion(RecepcionReporte recepcion);
    Optional<LoteOrigenReporte> findByCodigoLoteGranja(String codigoLoteGranja);
    List<LoteOrigenReporte> findByCodigoLoteGranjaIn(List<String> codigos);
}
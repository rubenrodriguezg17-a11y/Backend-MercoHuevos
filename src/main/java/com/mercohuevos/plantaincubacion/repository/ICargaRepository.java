package com.mercohuevos.plantaincubacion.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mercohuevos.plantaincubacion.enums.EstadoCarga;
import com.mercohuevos.plantaincubacion.model.Carga;
import com.mercohuevos.plantaincubacion.model.FusionLote;

public interface ICargaRepository extends JpaRepository<Carga, Long> {

    Optional<Carga> findByFusionLoteAndFechaCargaAndEstado(
            FusionLote fusionLote, LocalDate fechaCarga, EstadoCarga estado);

    boolean existsByFusionLote(FusionLote fusionLote);
}
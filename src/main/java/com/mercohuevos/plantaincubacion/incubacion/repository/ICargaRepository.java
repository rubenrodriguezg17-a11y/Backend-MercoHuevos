package com.mercohuevos.plantaincubacion.incubacion.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mercohuevos.plantaincubacion.enums.EstadoCarga;
import com.mercohuevos.plantaincubacion.incubacion.model.Carga;

public interface ICargaRepository extends JpaRepository<Carga, Long> {

    Optional<Carga> findByIdLineaGeneticaAndFechaCargaAndEstado(
            Long idLineaGenetica, LocalDate fechaCarga, EstadoCarga estado);

    List<Carga> findByEstadoAndIdLineaGenetica(EstadoCarga estado, Long idLineaGenetica);
}
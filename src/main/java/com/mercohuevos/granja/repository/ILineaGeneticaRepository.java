package com.mercohuevos.granja.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mercohuevos.granja.model.LineaGenetica;

public interface ILineaGeneticaRepository extends JpaRepository<LineaGenetica, Long> {
    List<LineaGenetica> findByActivoTrue();
}
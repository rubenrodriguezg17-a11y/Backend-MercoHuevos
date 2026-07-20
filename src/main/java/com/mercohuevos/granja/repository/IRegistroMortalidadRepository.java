package com.mercohuevos.granja.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mercohuevos.granja.model.Lote;
import com.mercohuevos.granja.model.RegistroMortalidad;

public interface IRegistroMortalidadRepository extends JpaRepository<RegistroMortalidad, Long> {

    @Query("SELECT COALESCE(SUM(r.cantidadMuertas), 0) FROM RegistroMortalidad r " +
           "WHERE r.lote = :lote AND r.fecha <= :fecha")
    Integer sumMortalidadHastaFecha(@Param("lote") Lote lote, @Param("fecha") LocalDate fecha);

    List<RegistroMortalidad> findByLote(Lote lote);

    Optional<RegistroMortalidad> findByLoteAndFecha(Lote lote, LocalDate fecha);
}
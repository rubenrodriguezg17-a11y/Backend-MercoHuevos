package com.mercohuevos.plantaincubacion.nacimiento.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mercohuevos.plantaincubacion.nacimiento.model.Nacimiento;

public interface INacimientoRepository extends JpaRepository<Nacimiento, Long> {
    Optional<Nacimiento> findByIdCarga(Long idCarga);
}
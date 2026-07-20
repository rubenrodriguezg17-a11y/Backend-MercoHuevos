package com.mercohuevos.granja.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mercohuevos.granja.model.TipoHuevo;

public interface ITipoHuevoRepository extends JpaRepository<TipoHuevo, Long> {
    List<TipoHuevo> findByActivoTrue();
}
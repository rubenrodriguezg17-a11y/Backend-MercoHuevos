package com.mercohuevos.plantaincubacion.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mercohuevos.plantaincubacion.model.ClasificacionTipoHuevo;

public interface IClasificacionTipoHuevoRepository extends JpaRepository<ClasificacionTipoHuevo, Long> {
    Optional<ClasificacionTipoHuevo> findByCodigoTipoHuevo(String codigo);
}
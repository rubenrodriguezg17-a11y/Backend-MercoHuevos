package com.mercohuevos.plantaincubacion.recepcion.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mercohuevos.plantaincubacion.recepcion.model.ClasificacionTipoHuevo;

public interface IClasificacionTipoHuevoRepository extends JpaRepository<ClasificacionTipoHuevo, Long> {
    Optional<ClasificacionTipoHuevo> findByCodigoTipoHuevo(String codigo);
}
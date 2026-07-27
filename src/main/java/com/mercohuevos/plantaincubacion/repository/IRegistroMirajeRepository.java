package com.mercohuevos.plantaincubacion.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mercohuevos.plantaincubacion.model.Carga;
import com.mercohuevos.plantaincubacion.model.RegistroMiraje;

public interface IRegistroMirajeRepository extends JpaRepository<RegistroMiraje, Long> {

    List<RegistroMiraje> findByCarga(Carga carga);

    @Query("SELECT COALESCE(SUM(r.cantidadNoFecundada), 0) FROM RegistroMiraje r WHERE r.carga = :carga")
    Integer sumNoFecundadoPorCarga(@Param("carga") Carga carga);
}
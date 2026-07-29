package com.mercohuevos.plantaincubacion.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mercohuevos.plantaincubacion.enums.EstadoCarga;
import com.mercohuevos.plantaincubacion.enums.FaseAsignacion;
import com.mercohuevos.plantaincubacion.model.AsignacionCargaMaquina;
import com.mercohuevos.plantaincubacion.model.Carga;
import com.mercohuevos.plantaincubacion.model.Maquina;

public interface IAsignacionCargaMaquinaRepository extends JpaRepository<AsignacionCargaMaquina, Long> {

    List<AsignacionCargaMaquina> findByCarga(Carga carga);

    Optional<AsignacionCargaMaquina> findByCargaAndMaquinaAndFase(
            Carga carga, Maquina maquina, FaseAsignacion fase);

    @Query("SELECT COALESCE(SUM(a.cantidadAsignada), 0) FROM AsignacionCargaMaquina a " +
           "WHERE a.maquina = :maquina AND a.fase = :fase " +
           "AND a.carga.estado <> :estadoExcluido")
    Integer sumAsignadoActivoPorMaquina(
            @Param("maquina") Maquina maquina,
            @Param("fase") FaseAsignacion fase,
            @Param("estadoExcluido") EstadoCarga estadoExcluido);
}
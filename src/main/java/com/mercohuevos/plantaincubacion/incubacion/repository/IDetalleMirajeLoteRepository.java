package com.mercohuevos.plantaincubacion.incubacion.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mercohuevos.plantaincubacion.incubacion.model.Carga;
import com.mercohuevos.plantaincubacion.incubacion.model.CargaLote;
import com.mercohuevos.plantaincubacion.incubacion.model.DetalleMirajeLote;

public interface IDetalleMirajeLoteRepository extends JpaRepository<DetalleMirajeLote, Long> {

    @Query("SELECT COALESCE(SUM(d.huevosInfertiles), 0) FROM DetalleMirajeLote d WHERE d.cargaLote = :cargaLote")
    Integer sumHuevosInfertilesPorCargaLote(@Param("cargaLote") CargaLote cargaLote);

    @Query("SELECT COALESCE(SUM(d.huevosInfertiles), 0) FROM DetalleMirajeLote d WHERE d.miraje.carga = :carga")
    Integer sumHuevosInfertilesPorCarga(@Param("carga") Carga carga);
}
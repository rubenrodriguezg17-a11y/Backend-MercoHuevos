package com.mercohuevos.plantaincubacion.transferencia.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mercohuevos.plantaincubacion.shared.model.Maquina;
import com.mercohuevos.plantaincubacion.transferencia.model.DetalleTransferenciaLote;

public interface IDetalleTransferenciaLoteRepository extends JpaRepository<DetalleTransferenciaLote, Long> {

    @Query("SELECT COALESCE(SUM(d.huevosTransferidos), 0) FROM DetalleTransferenciaLote d WHERE d.idCargaLote = :idCargaLote")
    Integer sumHuevosTransferidosPorCargaLote(@Param("idCargaLote") Long idCargaLote);

    @Query("SELECT COALESCE(SUM(d.huevosTransferidos), 0) FROM DetalleTransferenciaLote d " +
            "WHERE d.nacedoraDestino = :nacedora AND d.liberado = false")
    Integer sumHuevosTransferidosActivosPorNacedora(@Param("nacedora") Maquina nacedora);

    @Query("SELECT DISTINCT d.maquinaOrigen FROM DetalleTransferenciaLote d WHERE d.transferencia.idCarga = :idCarga")
    List<Maquina> findMaquinasOrigenPorCarga(@Param("idCarga") Long idCarga);
}
// despacho/repository/IDetalleDespachoLoteRepository.java
package com.mercohuevos.plantaincubacion.despacho.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mercohuevos.plantaincubacion.despacho.dto.TotalDespachadoDTO;
import com.mercohuevos.plantaincubacion.despacho.model.DetalleDespachoLote;

public interface IDetalleDespachoLoteRepository extends JpaRepository<DetalleDespachoLote, Long> {

    @Query("SELECT new com.mercohuevos.plantaincubacion.despacho.dto.TotalDespachadoDTO(" +
            "COALESCE(SUM(d.machos1raDespachados),0), COALESCE(SUM(d.machos2daDespachados),0), " +
            "COALESCE(SUM(d.hembras1raDespachadas),0), COALESCE(SUM(d.hembras2daDespachadas),0)) " +
            "FROM DetalleDespachoLote d WHERE d.idDetalleVacunacion = :idDetalleVacunacion")
    TotalDespachadoDTO sumarDespachadoPorDetalle(@Param("idDetalleVacunacion") Long idDetalleVacunacion);
}
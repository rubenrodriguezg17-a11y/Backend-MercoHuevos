package com.mercohuevos.plantaincubacion.recepcion.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.mercohuevos.plantaincubacion.recepcion.model.ConteoComercialLinea;
import com.mercohuevos.plantaincubacion.recepcion.model.RecepcionReporte;

public interface IConteoComercialLineaRepository extends JpaRepository<ConteoComercialLinea, Long> {
    List<ConteoComercialLinea> findByRecepcion(RecepcionReporte recepcion);
    void deleteByRecepcion(RecepcionReporte recepcion);
}
package com.mercohuevos.plantaincubacion.recepcion.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.mercohuevos.plantaincubacion.recepcion.model.EmbandejadoGeneral;
import com.mercohuevos.plantaincubacion.recepcion.model.RecepcionReporte;

public interface IEmbandejadoGeneralRepository extends JpaRepository<EmbandejadoGeneral, Long> {
    Optional<EmbandejadoGeneral> findByRecepcion(RecepcionReporte recepcion);
}
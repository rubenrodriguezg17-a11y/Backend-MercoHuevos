package com.mercohuevos.plantaincubacion.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.mercohuevos.plantaincubacion.model.EmbandejadoGeneral;
import com.mercohuevos.plantaincubacion.model.RecepcionReporte;

public interface IEmbandejadoGeneralRepository extends JpaRepository<EmbandejadoGeneral, Long> {
    Optional<EmbandejadoGeneral> findByRecepcion(RecepcionReporte recepcion);
}
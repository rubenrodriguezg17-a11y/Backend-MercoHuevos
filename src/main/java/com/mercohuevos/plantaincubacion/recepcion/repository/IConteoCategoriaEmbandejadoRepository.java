package com.mercohuevos.plantaincubacion.recepcion.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import com.mercohuevos.plantaincubacion.recepcion.model.ConteoCategoriaEmbandejado;
import com.mercohuevos.plantaincubacion.recepcion.model.EmbandejadoLoteFusion;

public interface IConteoCategoriaEmbandejadoRepository extends JpaRepository<ConteoCategoriaEmbandejado, Long> {
    List<ConteoCategoriaEmbandejado> findByEmbandejadoLoteFusion(EmbandejadoLoteFusion detalle);
}
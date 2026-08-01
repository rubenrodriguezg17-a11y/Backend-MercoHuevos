package com.mercohuevos.plantaincubacion.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import com.mercohuevos.plantaincubacion.model.ConteoCategoriaEmbandejado;
import com.mercohuevos.plantaincubacion.model.EmbandejadoLoteFusion;

public interface IConteoCategoriaEmbandejadoRepository extends JpaRepository<ConteoCategoriaEmbandejado, Long> {
    List<ConteoCategoriaEmbandejado> findByEmbandejadoLoteFusion(EmbandejadoLoteFusion detalle);
}
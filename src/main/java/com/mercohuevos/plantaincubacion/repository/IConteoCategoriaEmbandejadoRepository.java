package com.mercohuevos.plantaincubacion.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mercohuevos.plantaincubacion.model.ConteoCategoriaEmbandejado;
import com.mercohuevos.plantaincubacion.model.EmbandejadoDetalle;

public interface IConteoCategoriaEmbandejadoRepository extends JpaRepository<ConteoCategoriaEmbandejado, Long> {
    List<ConteoCategoriaEmbandejado> findByEmbandejadoDetalle(EmbandejadoDetalle detalle);
}
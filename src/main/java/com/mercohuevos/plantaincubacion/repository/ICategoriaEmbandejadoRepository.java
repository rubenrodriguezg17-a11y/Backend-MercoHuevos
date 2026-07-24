package com.mercohuevos.plantaincubacion.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mercohuevos.plantaincubacion.model.CategoriaEmbandejado;

public interface ICategoriaEmbandejadoRepository extends JpaRepository<CategoriaEmbandejado, Long> {
    List<CategoriaEmbandejado> findByActivoTrue();
}
package com.mercohuevos.plantaincubacion.shared.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mercohuevos.plantaincubacion.shared.model.CategoriaEmbandejado;

public interface ICategoriaEmbandejadoRepository extends JpaRepository<CategoriaEmbandejado, Long> {
    List<CategoriaEmbandejado> findByActivoTrue();
}
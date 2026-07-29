package com.mercohuevos.plantaincubacion.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mercohuevos.plantaincubacion.model.Carga;
import com.mercohuevos.plantaincubacion.model.CategoriaCarga;
import com.mercohuevos.plantaincubacion.model.CategoriaEmbandejado;

public interface ICategoriaCargaRepository extends JpaRepository<CategoriaCarga, Long> {
    List<CategoriaCarga> findByCarga(Carga carga);
    Optional<CategoriaCarga> findByCargaAndCategoriaEmbandejado(Carga carga, CategoriaEmbandejado categoria);
}
package com.mercohuevos.plantaincubacion.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mercohuevos.plantaincubacion.model.CategoriaDespacho;

public interface ICategoriaDespachoRepository extends JpaRepository<CategoriaDespacho, Long>{
	List<CategoriaDespacho> findByActivoTrue();
}

package com.mercohuevos.plantaincubacion.despacho.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mercohuevos.plantaincubacion.despacho.model.CategoriaDespacho;

public interface ICategoriaDespachoRepository extends JpaRepository<CategoriaDespacho, Long>{
	List<CategoriaDespacho> findByActivoTrue();
}

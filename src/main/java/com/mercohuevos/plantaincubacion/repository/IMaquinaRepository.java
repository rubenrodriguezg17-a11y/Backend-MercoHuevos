package com.mercohuevos.plantaincubacion.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mercohuevos.plantaincubacion.model.Maquina;

public interface IMaquinaRepository extends JpaRepository<Maquina, Long> {

}

package com.mercohuevos.plantaincubacion.shared.repository;

import com.mercohuevos.plantaincubacion.enums.TipoMaquina;
import org.springframework.data.jpa.repository.JpaRepository;

import com.mercohuevos.plantaincubacion.shared.model.Maquina;

import java.util.List;

public interface IMaquinaRepository extends JpaRepository<Maquina, Long> {
    List<Maquina> findByTipo(TipoMaquina tipo);
}

package com.mercohuevos.plantaincubacion.incubacion.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mercohuevos.plantaincubacion.incubacion.model.Carga;
import com.mercohuevos.plantaincubacion.incubacion.model.Miraje;

public interface IMirajeRepository extends JpaRepository<Miraje, Long> {
    List<Miraje> findByCargaOrderByFechaMirajeAsc(Carga carga);
}

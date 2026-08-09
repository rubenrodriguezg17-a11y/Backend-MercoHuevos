package com.mercohuevos.plantaincubacion.incubacion.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mercohuevos.plantaincubacion.incubacion.model.Carga;
import com.mercohuevos.plantaincubacion.incubacion.model.Miraje;

public interface IMirajeRepository extends JpaRepository<Miraje, Long> {
    List<Miraje>        findByCargaOrderByFechaMirajeAsc(Carga carga);
    boolean             existsByCarga(Carga carga);
    Optional<Miraje>    findByIdMirajeAndCarga_IdCarga(Long idMiraje, Long idCarga);
}

package com.mercohuevos.plantaincubacion.incubacion.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mercohuevos.plantaincubacion.shared.model.Maquina;
import com.mercohuevos.plantaincubacion.incubacion.model.MonitoreoAmbiental;

public interface IMonitoreoAmbientalRepository extends JpaRepository<MonitoreoAmbiental, Long> {
    List<MonitoreoAmbiental> findByMaquinaOrderByFechaHoraAsc(Maquina maquina);
}
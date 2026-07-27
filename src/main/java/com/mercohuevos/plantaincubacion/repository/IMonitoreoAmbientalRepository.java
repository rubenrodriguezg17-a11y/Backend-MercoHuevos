package com.mercohuevos.plantaincubacion.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mercohuevos.plantaincubacion.model.Maquina;
import com.mercohuevos.plantaincubacion.model.MonitoreoAmbiental;

public interface IMonitoreoAmbientalRepository extends JpaRepository<MonitoreoAmbiental, Long> {
    List<MonitoreoAmbiental> findByMaquinaOrderByFechaHoraAsc(Maquina maquina);
}
package com.mercohuevos.plantaincubacion.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mercohuevos.plantaincubacion.model.Carga;
import com.mercohuevos.plantaincubacion.model.RegistroNacimiento;

public interface IRegistroNacimientoRepository extends JpaRepository<RegistroNacimiento, Long> {
    Optional<RegistroNacimiento> findByCarga(Carga carga);
}
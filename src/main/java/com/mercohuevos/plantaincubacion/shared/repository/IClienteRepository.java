package com.mercohuevos.plantaincubacion.shared.repository;

import com.mercohuevos.plantaincubacion.shared.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IClienteRepository extends JpaRepository<Cliente,Long> {
}

package com.mercohuevos.granja.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import com.mercohuevos.granja.model.Lote;

public interface ILoteRepository extends JpaRepository<Lote, Long> {
}

package com.mercohuevos.granja.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "linea_genetica")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LineaGenetica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_linea_genetica")
    private Long idGen;

    @Column(name = "nombre_gen", nullable = false)
    private String nombreGen;

    @Column(name = "proposito_gen", nullable = false)
    private String propositoGen;

    @Column(name = "activo", nullable = false)
    private boolean activo = true;
}
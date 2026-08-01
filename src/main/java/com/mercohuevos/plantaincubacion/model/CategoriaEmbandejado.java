package com.mercohuevos.plantaincubacion.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "categoria_embandejado")
@Getter
@Setter
public class CategoriaEmbandejado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCategoriaEmbandejado;

    @Column(nullable = false, unique = true)
    private String codigoCategoria;

    @Column(nullable = false)
    private String nombreCategoria;

    @Column(nullable = false)
    private Boolean activo = true;
}
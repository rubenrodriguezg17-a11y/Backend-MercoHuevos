package com.mercohuevos.plantaincubacion.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "categoria_carga")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoriaCarga {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_categoria_carga")
    private Long idCategoriaCarga;

    @ManyToOne
    @JoinColumn(name = "id_carga", nullable = false)
    private Carga carga;

    @ManyToOne
    @JoinColumn(name = "id_categoria_embandejado", nullable = false)
    private CategoriaEmbandejado categoriaEmbandejado;

    @Column(name = "cantidad_inicial", nullable = false)
    private Integer cantidadInicial;
}
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
@Table(name = "conteo_embandejado_categoria")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConteoEmbandejadoCategoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_embandejado_categoria")
    private Long idEmbandejadoCategoria;

    @ManyToOne
    @JoinColumn(name = "id_embandejado", nullable = false)
    private EmbandejadoDetalle embandejadoDetalle;

    @ManyToOne
    @JoinColumn(name = "id_categoria_embandejado", nullable = false)
    private CategoriaEmbandejado categoriaEmbandejado;

    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;
}
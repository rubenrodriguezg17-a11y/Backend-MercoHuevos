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
@Table(name = "conteo_categoria_embandejado")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConteoCategoriaEmbandejado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_conteo")
    private Long idConteo;

    @ManyToOne
    @JoinColumn(name = "id_embandejado", nullable = false)
    private EmbandejadoDetalle embandejadoDetalle;

    @ManyToOne
    @JoinColumn(name = "id_categoria_embandejado", nullable = false)
    private CategoriaEmbandejado categoriaEmbandejado;

    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;
}
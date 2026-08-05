package com.mercohuevos.plantaincubacion.recepcion.model;

import com.mercohuevos.plantaincubacion.shared.model.CategoriaEmbandejado;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "conteo_categoria_embandejado")
@Getter
@Setter
public class ConteoCategoriaEmbandejado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idConteo;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_embandejado_lote_fusion", nullable = false)
    private EmbandejadoLoteFusion embandejadoLoteFusion;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_categoria_embandejado", nullable = false)
    private CategoriaEmbandejado categoriaEmbandejado;

    @Column(nullable = false)
    private Integer cantidad;
}
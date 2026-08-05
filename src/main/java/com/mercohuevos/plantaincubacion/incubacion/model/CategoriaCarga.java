package com.mercohuevos.plantaincubacion.incubacion.model;

import com.mercohuevos.plantaincubacion.shared.model.CategoriaEmbandejado;
import com.mercohuevos.plantaincubacion.shared.model.FusionLote;
import com.mercohuevos.plantaincubacion.shared.model.Maquina;
import jakarta.persistence.*;
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
    @JoinColumn(name = "id_fusion_lote", nullable = false)
    private FusionLote fusionLote;

    @ManyToOne
    @JoinColumn(name = "id_categoria_embandejado", nullable = false)
    private CategoriaEmbandejado categoriaEmbandejado;

    @ManyToOne
    @JoinColumn(name = "id_maquina", nullable = false)
    private Maquina maquina;

    @Column(name = "cantidad_inicial", nullable = false)
    private Integer cantidadInicial;
}
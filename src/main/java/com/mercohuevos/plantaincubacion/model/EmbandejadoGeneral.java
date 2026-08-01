package com.mercohuevos.plantaincubacion.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.mercohuevos.plantaincubacion.enums.EstadoEmbandejado;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "embandejado_general")
@Getter
@Setter
public class EmbandejadoGeneral {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idEmbandejadoGeneral;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_recepcion", nullable = false, unique = true)
    private RecepcionReporte recepcion;

    @Column(name = "fecha_embandejado", nullable = false)
    private LocalDate fechaEmbandejado;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoEmbandejado estado;

    @OneToMany(mappedBy = "embandejadoGeneral", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EmbandejadoLoteFusion> lotesFusionados = new ArrayList<>();
}
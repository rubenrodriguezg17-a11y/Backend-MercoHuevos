package com.mercohuevos.plantaincubacion.incubacion.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "miraje")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Miraje {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_miraje")
    private Long idMiraje;

    @ManyToOne
    @JoinColumn(name = "id_carga", nullable = false)
    private Carga carga;

    @Column(name = "fecha_miraje", nullable = false)
    private LocalDateTime fechaMiraje;

    @Column(name = "responsable", nullable = false)
    private String responsable;

    @OneToMany(mappedBy = "miraje", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetalleMirajeLote> detalles = new ArrayList<>();
}
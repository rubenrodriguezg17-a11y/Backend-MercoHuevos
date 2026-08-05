package com.mercohuevos.plantaincubacion.shared.model;

import com.mercohuevos.plantaincubacion.enums.EstadoMaquina;
import com.mercohuevos.plantaincubacion.enums.TipoMaquina;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "maquina")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Maquina {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_maquina")
    private Long idMaquina;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    private TipoMaquina tipo;

    @Column(name = "numero", nullable = false)
    private Integer numero;

    @Column(name = "capacidad_maxima", nullable = false)
    private Integer capacidadMaxima;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private EstadoMaquina estado = EstadoMaquina.APAGADA;
}
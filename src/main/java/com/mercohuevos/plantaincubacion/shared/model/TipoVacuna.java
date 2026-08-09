package com.mercohuevos.plantaincubacion.shared.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tipo_vacuna")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TipoVacuna {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tipo_vacuna")
    private Long idTipoVacuna;

    @Column(name = "nombre_vacuna", nullable = false)
    private String nombreVacuna;

    @Column(name = "dosis_estandar", nullable = false)
    private String dosisEstandar;

    @Column(name = "activo", nullable = false)
    private Boolean activo = true;
}
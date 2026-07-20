package com.mercohuevos.granja.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tipo_huevo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TipoHuevo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tipo_huevo")
    private Long idTipoHuevo;

    @Column(name = "codigo", nullable = false)
    private String codigo;

    @Column(name = "descripcion", nullable = false)
    private String descripcion;

    @Column(name = "es_fertil", nullable = false)
    private boolean esFertil;

    @Column(name = "activo", nullable = false)
    private boolean activo = true;
}
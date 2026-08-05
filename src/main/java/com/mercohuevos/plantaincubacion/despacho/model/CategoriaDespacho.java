package com.mercohuevos.plantaincubacion.despacho.model;

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
@Table(name = "categoria_despacho")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoriaDespacho {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_categoria_despacho")
    private Long idCategoriaDespacho;

    @Column(name = "codigo", nullable = false)
    private String codigo;          // PRIMERA, SEGUNDA, MACHO, DESCARTE...

    @Column(name = "descripcion", nullable = false)
    private String descripcion;

    @Column(name = "vendible_por_defecto", nullable = false)
    private boolean vendiblePorDefecto; 

    @Column(name = "activo", nullable = false)
    private boolean activo = true;
}
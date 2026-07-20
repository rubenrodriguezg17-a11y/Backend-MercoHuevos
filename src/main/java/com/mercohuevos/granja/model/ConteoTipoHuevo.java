package com.mercohuevos.granja.model;

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
@Table(name = "conteo_tipo_huevo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConteoTipoHuevo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_conteo")
    private Long idConteo;

    @ManyToOne
    @JoinColumn(name = "id_detalle", nullable = false)
    private DetalleLoteReporte detalleLote;

    @ManyToOne
    @JoinColumn(name = "id_tipo_huevo", nullable = false)
    private TipoHuevo tipoHuevo;

    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;
}
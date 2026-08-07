package com.mercohuevos.plantaincubacion.despacho.dto;

public record TotalDespachadoDTO(
        Integer machos1ra,
        Integer machos2da,
        Integer hembras1ra,
        Integer hembras2da
) {
    public TotalDespachadoDTO(Long machos1ra, Long machos2da, Long hembras1ra, Long hembras2da) {
        this(
                machos1ra != null ? machos1ra.intValue() : 0,
                machos2da != null ? machos2da.intValue() : 0,
                hembras1ra != null ? hembras1ra.intValue() : 0,
                hembras2da != null ? hembras2da.intValue() : 0
        );
    }

    public int total() {
        return machos1ra + machos2da + hembras1ra + hembras2da;
    }
}
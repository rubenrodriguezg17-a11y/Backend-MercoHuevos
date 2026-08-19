package com.mercohuevos.auth.annotation;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("hasAnyRole('ADMIN','ENCARGADO_GRANJA','ENCARGADO_PLANTA_INCUBACION','LOGISTICA_INCUBACION')")
public @interface RequireLecturaCatalogos {

}

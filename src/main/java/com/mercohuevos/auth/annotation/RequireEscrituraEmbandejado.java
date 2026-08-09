package com.mercohuevos.auth.annotation;
import java.lang.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("hasAnyRole('ENCARGADO_EMBANDEJADO','LOGISTICA_INCUBACION','ENCARGADO_PLANTA_INCUBACION')")
public @interface RequireEscrituraEmbandejado {}
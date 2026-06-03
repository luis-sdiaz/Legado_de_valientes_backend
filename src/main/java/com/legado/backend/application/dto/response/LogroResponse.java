package com.legado.backend.application.dto.response;

import java.time.Instant;

public class LogroResponse {

    private String nombre;
    private String descripcion;
    private String condicion;
    private boolean completado;
    private Instant fechaObtencion;

    public LogroResponse() {}

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getCondicion() { return condicion; }
    public void setCondicion(String condicion) { this.condicion = condicion; }

    public boolean isCompletado() { return completado; }
    public void setCompletado(boolean completado) { this.completado = completado; }

    public Instant getFechaObtencion() { return fechaObtencion; }
    public void setFechaObtencion(Instant fechaObtencion) { this.fechaObtencion = fechaObtencion; }
}

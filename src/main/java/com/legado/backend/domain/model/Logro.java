package com.legado.backend.domain.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "logros")
public class Logro {

    @Id
    private String id;
    private String jugadorId;
    private String nombre;
    private String descripcion;
    private String condicion;
    private boolean completado;
    private Instant fechaObtencion;

    public Logro() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getJugadorId() { return jugadorId; }
    public void setJugadorId(String jugadorId) { this.jugadorId = jugadorId; }

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

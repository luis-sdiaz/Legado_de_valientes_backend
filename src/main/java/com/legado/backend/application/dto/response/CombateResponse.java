package com.legado.backend.application.dto.response;

import com.legado.backend.infrastructure.type.EstadoCombate;

import java.time.Instant;

public class CombateResponse {

    private String id;
    private String jugador1Id;
    private String mascota1Id;
    private String jugador2Id;
    private String mascota2Id;
    private EstadoCombate estado;
    private String ganadorId;
    private String perdedorId;
    private Integer rondas;
    private Integer turnosEjecutados;
    private String resumen;
    private Instant createdAt;
    private JugadorResponse jugadorGanador;

    public CombateResponse() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJugador1Id() {
        return jugador1Id;
    }

    public void setJugador1Id(String jugador1Id) {
        this.jugador1Id = jugador1Id;
    }

    public String getMascota1Id() {
        return mascota1Id;
    }

    public void setMascota1Id(String mascota1Id) {
        this.mascota1Id = mascota1Id;
    }

    public String getJugador2Id() {
        return jugador2Id;
    }

    public void setJugador2Id(String jugador2Id) {
        this.jugador2Id = jugador2Id;
    }

    public String getMascota2Id() {
        return mascota2Id;
    }

    public void setMascota2Id(String mascota2Id) {
        this.mascota2Id = mascota2Id;
    }

    public EstadoCombate getEstado() {
        return estado;
    }

    public void setEstado(EstadoCombate estado) {
        this.estado = estado;
    }

    public String getGanadorId() {
        return ganadorId;
    }

    public void setGanadorId(String ganadorId) {
        this.ganadorId = ganadorId;
    }

    public String getPerdedorId() { return perdedorId; }
    public void setPerdedorId(String perdedorId) { this.perdedorId = perdedorId; }

    public Integer getRondas() {
        return rondas;
    }

    public void setRondas(Integer rondas) {
        this.rondas = rondas;
    }

    public Integer getTurnosEjecutados() { return turnosEjecutados; }
    public void setTurnosEjecutados(Integer turnosEjecutados) { this.turnosEjecutados = turnosEjecutados; }

    public String getResumen() { return resumen; }
    public void setResumen(String resumen) { this.resumen = resumen; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public JugadorResponse getJugadorGanador() { return jugadorGanador; }
    public void setJugadorGanador(JugadorResponse jugadorGanador) { this.jugadorGanador = jugadorGanador; }
}

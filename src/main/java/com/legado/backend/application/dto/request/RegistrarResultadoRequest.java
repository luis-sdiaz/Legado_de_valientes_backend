package com.legado.backend.application.dto.request;

public class RegistrarResultadoRequest {

    private String jugadorId;
    private String dificultad;  // "Fácil", "Medio", "Difícil"
    private String resultado;    // "ganado" o "perdido"

    public RegistrarResultadoRequest() {}

    // Getters y Setters
    public String getJugadorId() {
        return jugadorId;
    }

    public void setJugadorId(String jugadorId) {
        this.jugadorId = jugadorId;
    }

    public String getDificultad() {
        return dificultad;
    }

    public void setDificultad(String dificultad) {
        this.dificultad = dificultad;
    }

    public String getResultado() {
        return resultado;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }
}
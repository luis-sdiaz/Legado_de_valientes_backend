package com.legado.backend.application.dto.response;

// Respuesta combinada al comprar una mascota:
// incluye la mascota creada y el jugador con sus monedas actualizadas.
public class ComprarMascotaResponse {

    private MascotaResponse mascota;
    private JugadorResponse jugador;

    public ComprarMascotaResponse() {}

    public ComprarMascotaResponse(MascotaResponse mascota, JugadorResponse jugador) {
        this.mascota = mascota;
        this.jugador = jugador;
    }

    public MascotaResponse getMascota() {
        return mascota;
    }

    public void setMascota(MascotaResponse mascota) {
        this.mascota = mascota;
    }

    public JugadorResponse getJugador() {
        return jugador;
    }

    public void setJugador(JugadorResponse jugador) {
        this.jugador = jugador;
    }
}

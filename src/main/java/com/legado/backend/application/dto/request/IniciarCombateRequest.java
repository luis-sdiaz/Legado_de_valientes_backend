package com.legado.backend.application.dto.request;

import com.legado.backend.infrastructure.type.Dificultad;
import com.legado.backend.infrastructure.type.TipoMascota;

public class IniciarCombateRequest {
    private String jugadorId;
    private Dificultad dificultad;
    private TipoMascota tipoPreferido;

    public IniciarCombateRequest() {
    }

    public String getJugadorId() {
        return jugadorId;
    }

    public void setJugadorId(String jugadorId) {
        this.jugadorId = jugadorId;
    }

    public Dificultad getDificultad() {
        return dificultad;
    }

    public void setDificultad(Dificultad dificultad) {
        this.dificultad = dificultad;
    }

    public TipoMascota getTipoPreferido() {
        return tipoPreferido;
    }

    public void setTipoPreferido(TipoMascota tipoPreferido) {
        this.tipoPreferido = tipoPreferido;
    }
}


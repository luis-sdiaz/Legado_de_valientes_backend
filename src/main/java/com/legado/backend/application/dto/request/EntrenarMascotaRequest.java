package com.legado.backend.application.dto.request;

import com.legado.backend.infrastructure.type.TipoEntrenamiento;

public class EntrenarMascotaRequest {

    private TipoEntrenamiento tipo;

    public EntrenarMascotaRequest() {
    }

    public TipoEntrenamiento getTipo() {
        return tipo;
    }

    public void setTipo(TipoEntrenamiento tipo) {
        this.tipo = tipo;
    }
}

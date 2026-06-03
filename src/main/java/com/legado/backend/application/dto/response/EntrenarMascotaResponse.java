package com.legado.backend.application.dto.response;

public class EntrenarMascotaResponse {

    private MascotaResponse mascota;
    private int monedasRestantes;

    public EntrenarMascotaResponse(MascotaResponse mascota, int monedasRestantes) {
        this.mascota = mascota;
        this.monedasRestantes = monedasRestantes;
    }

    public MascotaResponse getMascota() {
        return mascota;
    }

    public void setMascota(MascotaResponse mascota) {
        this.mascota = mascota;
    }

    public int getMonedasRestantes() {
        return monedasRestantes;
    }

    public void setMonedasRestantes(int monedasRestantes) {
        this.monedasRestantes = monedasRestantes;
    }
}

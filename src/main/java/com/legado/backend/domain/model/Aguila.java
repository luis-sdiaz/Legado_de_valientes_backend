package com.legado.backend.domain.model;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "mascotas")
public class Aguila extends Mascota {

    public Aguila() {
    }

    @Override
    public void aplicarHabilidad() {
    }

    @Override
    public void entrenar() {
        Integer exp = this.getExperiencia() == null ? 0 : this.getExperiencia();
        this.setExperiencia(exp + 9);
        Integer vel = this.getVelocidad() == null ? 0 : this.getVelocidad();
        this.setVelocidad(vel + 5);
        Integer lvl = this.getNivel() == null ? 1 : this.getNivel();
        this.setNivel(lvl + 1);
    }

    @Override
    public void evolucionar() {
        Integer vel = this.getVelocidad() == null ? 0 : this.getVelocidad();
        this.setVelocidad(vel + 12);
        Integer salud = this.getSalud() == null ? 0 : this.getSalud();
        this.setSalud(salud + 15);
        Integer lvl = this.getNivel() == null ? 1 : this.getNivel();
        this.setNivel(lvl + 1);
    }
}

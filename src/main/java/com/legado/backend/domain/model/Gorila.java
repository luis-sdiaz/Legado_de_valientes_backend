package com.legado.backend.domain.model;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "mascotas")
public class Gorila extends Mascota {

    public Gorila() {
    }

    @Override
    public void aplicarHabilidad() {
    }

    @Override
    public void entrenar() {
        Integer exp = this.getExperiencia() == null ? 0 : this.getExperiencia();
        this.setExperiencia(exp + 12);
        Integer atk = this.getAtaque() == null ? 0 : this.getAtaque();
        this.setAtaque(atk + 6);
        Integer def = this.getDefensa() == null ? 0 : this.getDefensa();
        this.setDefensa(def + 4);
        Integer lvl = this.getNivel() == null ? 1 : this.getNivel();
        this.setNivel(lvl + 1);
    }

    @Override
    public void evolucionar() {
        Integer atk = this.getAtaque() == null ? 0 : this.getAtaque();
        this.setAtaque(atk + 18);
        Integer def = this.getDefensa() == null ? 0 : this.getDefensa();
        this.setDefensa(def + 12);
        Integer lvl = this.getNivel() == null ? 1 : this.getNivel();
        this.setNivel(lvl + 1);
    }
}

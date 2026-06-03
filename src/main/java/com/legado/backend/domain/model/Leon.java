package com.legado.backend.domain.model;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "mascotas")
public class Leon extends Mascota {

    public Leon() {
    }

    @Override
    public void aplicarHabilidad() {
    }

    @Override
    public void entrenar() {
        Integer exp = this.getExperiencia() == null ? 0 : this.getExperiencia();
        this.setExperiencia(exp + 8);
        Integer salud = this.getSalud() == null ? 0 : this.getSalud();
        this.setSalud(salud + 10);
        Integer lvl = this.getNivel() == null ? 1 : this.getNivel();
        this.setNivel(lvl + 1);
    }

    @Override
    public void evolucionar() {
        Integer salud = this.getSalud() == null ? 0 : this.getSalud();
        this.setSalud(salud + 25);
        Integer def = this.getDefensa() == null ? 0 : this.getDefensa();
        this.setDefensa(def + 8);
        Integer lvl = this.getNivel() == null ? 1 : this.getNivel();
        this.setNivel(lvl + 1);
    }
}

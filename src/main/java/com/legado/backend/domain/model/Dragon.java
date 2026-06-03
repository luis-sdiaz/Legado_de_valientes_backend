package com.legado.backend.domain.model;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "mascotas")
public class Dragon extends Mascota {

    private Integer temperaturaFuego;
    private Integer alcanceVuelo;

    public Dragon() {
        super();
        this.temperaturaFuego = 0;
        this.alcanceVuelo = 0;
    }

    public Integer getTemperaturaFuego() {
        return temperaturaFuego;
    }

    public void setTemperaturaFuego(Integer temperaturaFuego) {
        this.temperaturaFuego = temperaturaFuego;
    }

    public Integer getAlcanceVuelo() {
        return alcanceVuelo;
    }

    public void setAlcanceVuelo(Integer alcanceVuelo) {
        this.alcanceVuelo = alcanceVuelo;
    }

    @Override
    public void aplicarHabilidad() {
    }

    @Override
    public void entrenar() {
        Integer exp = this.getExperiencia() == null ? 0 : this.getExperiencia();
        this.setExperiencia(exp + 10);
        Integer atk = this.getAtaque() == null ? 0 : this.getAtaque();
        this.setAtaque(atk + 5);
        Integer lvl = this.getNivel() == null ? 1 : this.getNivel();
        this.setNivel(lvl + 1);
    }

    @Override
    public void evolucionar() {
        Integer atk = this.getAtaque() == null ? 0 : this.getAtaque();
        this.setAtaque(atk + 15);
        Integer salud = this.getSalud() == null ? 0 : this.getSalud();
        this.setSalud(salud + 20);
        Integer lvl = this.getNivel() == null ? 1 : this.getNivel();
        this.setNivel(lvl + 1);
        this.setTemperaturaFuego((this.temperaturaFuego == null ? 0 : this.temperaturaFuego) + 10);
        this.setAlcanceVuelo((this.alcanceVuelo == null ? 0 : this.alcanceVuelo) + 5);
    }
}

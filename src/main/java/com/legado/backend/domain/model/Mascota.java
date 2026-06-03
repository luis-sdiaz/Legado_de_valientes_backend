package com.legado.backend.domain.model;

import com.legado.backend.domain.service.Combatiente;
import com.legado.backend.domain.service.Entrenable;
import com.legado.backend.domain.service.Identificable;
import com.legado.backend.infrastructure.type.Rareza;
import com.legado.backend.infrastructure.type.TipoMascota;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "mascotas")
public abstract class Mascota implements Entrenable, Combatiente, Identificable {

    @Id
    private String id;
    private String nombre;
    private String jugadorId;
    private TipoMascota tipo;
    private Rareza rareza;
    private Integer nivel;
    private Integer experiencia;
    private Integer salud;
    private Integer saludMaximo;
    private Integer ataque;
    private Integer defensa;
    private Integer velocidad;
    private Boolean activo;

    public Mascota() {
        this.activo = false;
    }

    public Mascota(String nombre, String jugadorId, TipoMascota tipo, Rareza rareza) {
        this.nombre = nombre;
        this.jugadorId = jugadorId;
        this.tipo = tipo;
        this.rareza = rareza;
        this.activo = false;
    }

    public abstract void aplicarHabilidad();

    /**
     * Entrena la mascota. Cada subclase implementará la mejora de atributos de forma controlada.
     */
    public abstract void entrenar();

    /**
     * Evoluciona la mascota. Cada subclase implementará la transformación.
     */
    public abstract void evolucionar();

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getJugadorId() {
        return jugadorId;
    }

    public void setJugadorId(String jugadorId) {
        this.jugadorId = jugadorId;
    }

    public TipoMascota getTipo() {
        return tipo;
    }

    public void setTipo(TipoMascota tipo) {
        this.tipo = tipo;
    }

    public Rareza getRareza() {
        return rareza;
    }

    public void setRareza(Rareza rareza) {
        this.rareza = rareza;
    }

    public Integer getNivel() {
        return nivel;
    }

    public void setNivel(Integer nivel) {
        this.nivel = nivel;
    }

    public Integer getExperiencia() {
        return experiencia;
    }

    public void setExperiencia(Integer experiencia) {
        this.experiencia = experiencia;
    }

    public Integer getSalud() {
        return salud;
    }

    public void setSalud(Integer salud) {
        this.salud = salud;
    }

    public Integer getSaludMaximo() {
        return saludMaximo;
    }

    public void setSaludMaximo(Integer saludMaximo) {
        this.saludMaximo = saludMaximo;
    }

    public Integer getAtaque() {
        return ataque;
    }

    public void setAtaque(Integer ataque) {
        this.ataque = ataque;
    }

    public Integer getDefensa() {
        return defensa;
    }

    public void setDefensa(Integer defensa) {
        this.defensa = defensa;
    }

    public Integer getVelocidad() {
        return velocidad;
    }

    public void setVelocidad(Integer velocidad) {
        this.velocidad = velocidad;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    @Override
    public void atacar() {
        Integer exp = this.getExperiencia() == null ? 0 : this.getExperiencia();
        this.setExperiencia(exp + 1);
    }

    @Override
    public void defender() {
        Integer def = this.getDefensa() == null ? 0 : this.getDefensa();
        this.setDefensa(def + 1);
    }
}

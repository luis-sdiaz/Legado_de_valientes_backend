package com.legado.backend.domain.model;

import com.legado.backend.domain.service.Identificable;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "jugadores")
public class Jugador implements Identificable {

    @Id
    private String id;
    private String nombre;
    private String email;
    private Integer nivel;
    private Integer experiencia;
    private Integer monedas;
    private Integer victorias;
    private Integer derrotas;

    public Jugador() {
        this.nivel = 1;
        this.experiencia = 0;
        this.monedas = 0;
        this.victorias = 0;
        this.derrotas = 0;
    }

    public Jugador(String nombre, String email) {
        this.nombre = nombre;
        this.email = email;
    }

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public Integer getMonedas() {
        return monedas;
    }

    public void setMonedas(Integer monedas) {
        this.monedas = monedas;
    }

    public Integer getVictorias() {
        return victorias;
    }

    public void setVictorias(Integer victorias) {
        this.victorias = victorias;
    }

    public Integer getDerrotas() {
        return derrotas;
    }

    public void setDerrotas(Integer derrotas) {
        this.derrotas = derrotas;
    }
}


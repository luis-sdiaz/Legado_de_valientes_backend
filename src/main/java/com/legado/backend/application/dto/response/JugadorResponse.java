package com.legado.backend.application.dto.response;

public class JugadorResponse {

    private String id;
    private String nombre;
    private String email;
    private Integer nivel;
    private Integer experiencia;
    private Integer monedas;
    private Integer victorias;
    private Integer derrotas;
    private Integer cantidadMascotas;
    private Integer xpSiguienteNivel;

    public JugadorResponse() {
    }

    public String getId() {
        return id;
    }

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

    public Integer getCantidadMascotas() {
        return cantidadMascotas;
    }

    public void setCantidadMascotas(Integer cantidadMascotas) {
        this.cantidadMascotas = cantidadMascotas;
    }

    public Integer getXpSiguienteNivel() {
        return xpSiguienteNivel;
    }

    public void setXpSiguienteNivel(Integer xpSiguienteNivel) {
        this.xpSiguienteNivel = xpSiguienteNivel;
    }
}


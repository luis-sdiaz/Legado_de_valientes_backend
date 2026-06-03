package com.legado.backend.application.dto.request;

public class JugadorRequest {

    private String nombre;
    private String email;
    // Campo para actualizar monedas desde el frontend (entrenamiento, compras, etc.)
    private Integer monedas;

    public JugadorRequest() {
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

    public Integer getMonedas() {
        return monedas;
    }

    public void setMonedas(Integer monedas) {
        this.monedas = monedas;
    }
}


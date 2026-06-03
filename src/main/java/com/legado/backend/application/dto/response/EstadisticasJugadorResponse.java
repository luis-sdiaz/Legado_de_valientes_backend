package com.legado.backend.application.dto.response;

public class EstadisticasJugadorResponse {

    private Integer nivel;
    private Integer experiencia;
    private Integer monedas;
    private Integer cantidadMascotas;
    private Long victorias;
    private Long derrotas;
    private Long totalCombates;

    public EstadisticasJugadorResponse() {
    }

    public Integer getNivel() { return nivel; }
    public void setNivel(Integer nivel) { this.nivel = nivel; }

    public Integer getExperiencia() { return experiencia; }
    public void setExperiencia(Integer experiencia) { this.experiencia = experiencia; }

    public Integer getMonedas() { return monedas; }
    public void setMonedas(Integer monedas) { this.monedas = monedas; }

    public Integer getCantidadMascotas() { return cantidadMascotas; }
    public void setCantidadMascotas(Integer cantidadMascotas) { this.cantidadMascotas = cantidadMascotas; }

    public Long getVictorias() { return victorias; }
    public void setVictorias(Long victorias) { this.victorias = victorias; }

    public Long getDerrotas() { return derrotas; }
    public void setDerrotas(Long derrotas) { this.derrotas = derrotas; }

    public Long getTotalCombates() { return totalCombates; }
    public void setTotalCombates(Long totalCombates) { this.totalCombates = totalCombates; }
}


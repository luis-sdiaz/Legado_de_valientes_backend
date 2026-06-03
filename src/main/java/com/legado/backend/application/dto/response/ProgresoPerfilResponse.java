package com.legado.backend.application.dto.response;

public class ProgresoPerfilResponse {

    private Integer nivelActual;
    private Integer experienciaAcumulada;
    private Integer monedas;
    private Integer cantidadMascotas;
    private Long cantidadCombates;
    private Long victorias;
    private Long derrotas;
    // porcentaje entero 0..100
    private Integer progresoNivelPorcentaje;

    public ProgresoPerfilResponse() {
    }

    public Integer getNivelActual() { return nivelActual; }
    public void setNivelActual(Integer nivelActual) { this.nivelActual = nivelActual; }

    public Integer getExperienciaAcumulada() { return experienciaAcumulada; }
    public void setExperienciaAcumulada(Integer experienciaAcumulada) { this.experienciaAcumulada = experienciaAcumulada; }

    public Integer getMonedas() { return monedas; }
    public void setMonedas(Integer monedas) { this.monedas = monedas; }

    public Integer getCantidadMascotas() { return cantidadMascotas; }
    public void setCantidadMascotas(Integer cantidadMascotas) { this.cantidadMascotas = cantidadMascotas; }

    public Long getCantidadCombates() { return cantidadCombates; }
    public void setCantidadCombates(Long cantidadCombates) { this.cantidadCombates = cantidadCombates; }

    public Long getVictorias() { return victorias; }
    public void setVictorias(Long victorias) { this.victorias = victorias; }

    public Long getDerrotas() { return derrotas; }
    public void setDerrotas(Long derrotas) { this.derrotas = derrotas; }

    public Integer getProgresoNivelPorcentaje() { return progresoNivelPorcentaje; }
    public void setProgresoNivelPorcentaje(Integer progresoNivelPorcentaje) { this.progresoNivelPorcentaje = progresoNivelPorcentaje; }
}


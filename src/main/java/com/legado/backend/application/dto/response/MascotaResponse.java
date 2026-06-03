package com.legado.backend.application.dto.response;

import com.legado.backend.domain.model.Dragon;
import com.legado.backend.infrastructure.type.Rareza;
import com.legado.backend.infrastructure.type.TipoMascota;

public class MascotaResponse {

    private String id;
    private String nombre;
    private String jugadorId;
    private TipoMascota tipo;
    private Rareza rareza;
    private Integer nivel;
    private Integer experiencia;
    private Integer salud;
    private Integer ataque;
    private Integer defensa;
    private Integer velocidad;
    private String descripcion;
    private Boolean activo;
    // Dragon-specific attributes; null for other species.
    private Integer temperaturaFuego;
    private Integer alcanceVuelo;

    public MascotaResponse() {
    }

    /** Maps all fields from a Dragon, including its species-specific attributes. */
    public MascotaResponse(Dragon dragon) {
        this.id               = dragon.getId();
        this.nombre           = dragon.getNombre();
        this.jugadorId        = dragon.getJugadorId();
        this.tipo             = dragon.getTipo();
        this.rareza           = dragon.getRareza();
        this.nivel            = dragon.getNivel();
        this.experiencia      = dragon.getExperiencia();
        this.salud            = dragon.getSalud();
        this.ataque           = dragon.getAtaque();
        this.defensa          = dragon.getDefensa();
        this.velocidad        = dragon.getVelocidad();
        this.activo           = dragon.getActivo();
        this.temperaturaFuego = dragon.getTemperaturaFuego();
        this.alcanceVuelo     = dragon.getAlcanceVuelo();
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getJugadorId() { return jugadorId; }
    public void setJugadorId(String jugadorId) { this.jugadorId = jugadorId; }

    public TipoMascota getTipo() { return tipo; }
    public void setTipo(TipoMascota tipo) { this.tipo = tipo; }

    public Rareza getRareza() { return rareza; }
    public void setRareza(Rareza rareza) { this.rareza = rareza; }

    public Integer getNivel() { return nivel; }
    public void setNivel(Integer nivel) { this.nivel = nivel; }

    public Integer getExperiencia() { return experiencia; }
    public void setExperiencia(Integer experiencia) { this.experiencia = experiencia; }

    public Integer getSalud() { return salud; }
    public void setSalud(Integer salud) { this.salud = salud; }

    public Integer getAtaque() { return ataque; }
    public void setAtaque(Integer ataque) { this.ataque = ataque; }

    public Integer getDefensa() { return defensa; }
    public void setDefensa(Integer defensa) { this.defensa = defensa; }

    public Integer getVelocidad() { return velocidad; }
    public void setVelocidad(Integer velocidad) { this.velocidad = velocidad; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }

    public Integer getTemperaturaFuego() { return temperaturaFuego; }
    public void setTemperaturaFuego(Integer temperaturaFuego) { this.temperaturaFuego = temperaturaFuego; }

    public Integer getAlcanceVuelo() { return alcanceVuelo; }
    public void setAlcanceVuelo(Integer alcanceVuelo) { this.alcanceVuelo = alcanceVuelo; }
}

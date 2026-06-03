package com.legado.backend.infrastructure.type;

/**
 * Enum que representa los tipos de mascotas disponibles en el juego Legado de Valientes.
 */
public enum TipoMascota {
    DRAGON("Dragón"),
    LEON("León"),
    GORILA("Gorila"),
    AGUILA("Águila");

    private final String displayName;

    TipoMascota(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}


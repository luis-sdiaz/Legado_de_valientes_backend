package com.legado.backend.infrastructure.type;

/**
 * Enum que representa los niveles de rareza de las mascotas.
 */
public enum Rareza {
    COMUN("Común"),
    RARO("Raro"),
    ÉPICO("Épico"),
    LEGENDARIO("Legendario");

    private final String displayName;

    Rareza(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}


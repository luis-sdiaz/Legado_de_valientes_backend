package com.legado.backend.infrastructure.type;

/**
 * Enum que representa los posibles estados de un combate.
 */
public enum EstadoCombate {
    ESPERANDO("Esperando"),
    EN_PROGRESO("En Progreso"),
    FINALIZADO("Finalizado"),
    CANCELADO("Cancelado");

    private final String displayName;

    EstadoCombate(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}


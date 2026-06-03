package com.legado.backend.application.dto.response;

public class EvolucionResponse {
    public enum Status { SUCCESS, NOT_FOUND, NOT_ELIGIBLE }

    private final Status status;
    private final MascotaResponse mascota;
    private final String message;

    private EvolucionResponse(Status status, MascotaResponse mascota, String message) {
        this.status = status;
        this.mascota = mascota;
        this.message = message;
    }

    public static EvolucionResponse success(MascotaResponse mascota) {
        return new EvolucionResponse(Status.SUCCESS, mascota, null);
    }

    public static EvolucionResponse notFound() {
        return new EvolucionResponse(Status.NOT_FOUND, null, "Mascota no encontrada");
    }

    public static EvolucionResponse notEligible(String reason) {
        return new EvolucionResponse(Status.NOT_ELIGIBLE, null, reason);
    }

    public Status getStatus() { return status; }
    public MascotaResponse getMascota() { return mascota; }
    public String getMessage() { return message; }
}


package com.legado.backend.presentation.controller;

import com.legado.backend.application.dto.response.LogroResponse;
import com.legado.backend.application.service.AchievementService;
import com.legado.backend.domain.model.Logro;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/logros")
public class LogroController {

    private final AchievementService achievementService;

    public LogroController(AchievementService achievementService) {
        this.achievementService = achievementService;
    }

    @GetMapping
    public ResponseEntity<List<LogroResponse>> obtenerLogros(@RequestParam String jugadorId) {
        List<Logro> logros = achievementService.obtenerLogrosDelJugador(jugadorId);
        List<LogroResponse> responses = logros.stream()
                .map(this::toResponse)
                .toList();
        return ResponseEntity.ok(responses);
    }

    private LogroResponse toResponse(Logro logro) {
        LogroResponse r = new LogroResponse();
        r.setNombre(logro.getNombre());
        r.setDescripcion(logro.getDescripcion());
        r.setCondicion(logro.getCondicion());
        r.setCompletado(logro.isCompletado());
        r.setFechaObtencion(logro.getFechaObtencion());
        return r;
    }
}

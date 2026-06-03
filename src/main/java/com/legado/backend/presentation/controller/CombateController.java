package com.legado.backend.presentation.controller;

import com.legado.backend.application.dto.request.IniciarCombateRequest;
import com.legado.backend.application.dto.request.RegistrarResultadoRequest;
import com.legado.backend.application.dto.request.RivalRequest;
import com.legado.backend.application.dto.response.CombateResponse;
import com.legado.backend.application.dto.response.JugadorResponse;
import com.legado.backend.application.dto.response.RivalResponse;
import com.legado.backend.application.service.CombateService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = {
    "https://legado-de-valientes-frontend.vercel.app",
    "http://localhost:3000",
    "http://localhost:5173"
})
@RestController
@RequestMapping("/api/combates")
public class CombateController {

    private final CombateService combateService;

    public CombateController(CombateService combateService) {
        this.combateService = combateService;
    }

    @PostMapping
    public ResponseEntity<CombateResponse> crear(@RequestBody IniciarCombateRequest request) {
        Optional<CombateResponse> opt = combateService.iniciarCombateVsRival(request.getJugadorId(), request.getDificultad(), request.getTipoPreferido());
        return opt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CombateResponse> obtenerPorId(@PathVariable String id) {
        Optional<CombateResponse> opt = combateService.obtenerCombatePorId(id);
        return opt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/jugador/{jugadorId}")
    public ResponseEntity<List<CombateResponse>> obtenerPorJugador(@PathVariable String jugadorId) {
        List<CombateResponse> list = combateService.listarCombatesPorJugador(jugadorId);
        return ResponseEntity.ok(list);
    }

    @PostMapping("/rival")
    public ResponseEntity<RivalResponse> generarRival(@RequestBody RivalRequest request) {
        RivalResponse rival = combateService.generarRival(request.getDificultad(), request.getTipoPreferido());
        return ResponseEntity.ok(rival);
    }

    @PostMapping("/registrar-resultado")
    public ResponseEntity<JugadorResponse> registrarResultado(@RequestBody RegistrarResultadoRequest request) {
        Optional<JugadorResponse> opt = combateService.registrarResultado(
                request.getJugadorId(),
                request.getDificultad(),
                request.getResultado()
        );
        return opt.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // Alias usado por el frontend — acepta jugadorId como path variable o query param
    @GetMapping("/historial/{jugadorId}")
    public ResponseEntity<List<CombateResponse>> obtenerHistorialPath(@PathVariable String jugadorId) {
        List<CombateResponse> list = combateService.listarCombatesPorJugador(jugadorId);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/historial")
    public ResponseEntity<List<CombateResponse>> obtenerHistorialQuery(@RequestParam String jugadorId) {
        List<CombateResponse> list = combateService.listarCombatesPorJugador(jugadorId);
        return ResponseEntity.ok(list);
    }
}
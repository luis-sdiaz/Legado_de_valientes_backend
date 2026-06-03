package com.legado.backend.presentation.controller;

import com.legado.backend.application.dto.request.JugadorRequest;
import com.legado.backend.application.dto.response.EstadisticasJugadorResponse;
import com.legado.backend.application.dto.response.ProgresoPerfilResponse;
import com.legado.backend.application.dto.response.JugadorResponse;
import com.legado.backend.application.dto.response.LogroResponse;
import com.legado.backend.application.service.JugadorService;
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
@RequestMapping("/api/jugadores")
public class JugadorController {

    private final JugadorService jugadorService;

    public JugadorController(JugadorService jugadorService) {
        this.jugadorService = jugadorService;
    }

    @PostMapping
    public ResponseEntity<JugadorResponse> crear(@RequestBody JugadorRequest request) {
        JugadorResponse created = jugadorService.crearJugador(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/buscar")
    public ResponseEntity<JugadorResponse> buscarPorEmail(@RequestParam String email) {
        return jugadorService.buscarPorEmail(email)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<JugadorResponse> obtenerPorId(@PathVariable String id) {
        Optional<JugadorResponse> opt = jugadorService.obtenerJugadorPorId(id);
        return opt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<JugadorResponse>> obtenerTodos() {
        return ResponseEntity.ok(jugadorService.listarJugadores());
    }

    @PutMapping("/{id}")
    public ResponseEntity<JugadorResponse> actualizar(@PathVariable String id, @RequestBody JugadorRequest request) {
        Optional<JugadorResponse> opt = jugadorService.actualizarJugador(id, request);
        return opt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable String id) {
        boolean deleted = jugadorService.eliminarJugador(id);
        if (!deleted) return ResponseEntity.notFound().build();
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/{jugadorId}/estadisticas")
    public ResponseEntity<EstadisticasJugadorResponse> obtenerEstadisticas(@PathVariable String jugadorId) {
        return jugadorService.obtenerEstadisticasJugador(jugadorId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{jugadorId}/progreso")
    public ResponseEntity<ProgresoPerfilResponse> obtenerProgreso(@PathVariable String jugadorId) {
        return jugadorService.obtenerProgresoPerfil(jugadorId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{jugadorId}/logros")
    public ResponseEntity<List<LogroResponse>> obtenerLogros(@PathVariable String jugadorId) {
        return jugadorService.obtenerLogrosJugador(jugadorId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}

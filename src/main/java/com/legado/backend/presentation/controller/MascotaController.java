package com.legado.backend.presentation.controller;

import com.legado.backend.application.dto.request.EntrenarMascotaRequest;
import com.legado.backend.application.dto.request.MascotaRequest;
import com.legado.backend.application.dto.response.ComprarMascotaResponse;
import com.legado.backend.application.dto.response.EntrenarMascotaResponse;
import com.legado.backend.application.dto.response.EvolucionResponse;
import com.legado.backend.application.dto.response.MascotaResponse;
import com.legado.backend.application.service.MascotaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/mascotas")
public class MascotaController {

    private final MascotaService mascotaService;

    public MascotaController(MascotaService mascotaService) {
        this.mascotaService = mascotaService;
    }

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody MascotaRequest request) {
        try {
            ComprarMascotaResponse result = mascotaService.crearMascota(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<MascotaResponse> obtenerPorId(@PathVariable String id) {
        Optional<MascotaResponse> opt = mascotaService.obtenerMascotaPorId(id);
        return opt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/jugador/{jugadorId}")
    public ResponseEntity<List<MascotaResponse>> obtenerPorJugador(@PathVariable String jugadorId) {
        List<MascotaResponse> list = mascotaService.obtenerMascotasPorJugador(jugadorId);
        return ResponseEntity.ok(list);
    }

    @PostMapping("/{id}/entrenar")
    public ResponseEntity<?> entrenarMascota(
            @PathVariable String id,
            @RequestBody EntrenarMascotaRequest request,
            @RequestHeader(value = "jugadorId", required = false) String jugadorId) {
        try {
            EntrenarMascotaResponse result = mascotaService.entrenarMascota(id, jugadorId, request.getTipo());
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/{id}/evolucionar")
    public ResponseEntity<?> evolucionarMascota(@PathVariable String id) {
        EvolucionResponse result = mascotaService.evolucionarMascota(id);
        if (result.getStatus() == EvolucionResponse.Status.NOT_FOUND) {
            return ResponseEntity.notFound().build();
        } else if (result.getStatus() == EvolucionResponse.Status.NOT_ELIGIBLE) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(result.getMessage());
        }
        return ResponseEntity.ok(result.getMascota());
    }

    @PostMapping("/{id}/seleccionar")
    public ResponseEntity<MascotaResponse> seleccionarMascota(@PathVariable String id) {
        Optional<MascotaResponse> opt = mascotaService.seleccionarMascota(id);
        return opt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<MascotaResponse> actualizar(@PathVariable String id, @RequestBody MascotaRequest request) {
        Optional<MascotaResponse> opt = mascotaService.actualizarMascota(id, request);
        return opt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable String id) {
        boolean deleted = mascotaService.eliminarMascota(id);
        if (!deleted) return ResponseEntity.notFound().build();
        return ResponseEntity.noContent().build();
    }
}

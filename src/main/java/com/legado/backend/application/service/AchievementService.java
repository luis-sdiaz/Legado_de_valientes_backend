package com.legado.backend.application.service;

import com.legado.backend.domain.model.Jugador;
import com.legado.backend.domain.model.Logro;
import com.legado.backend.infrastructure.repository.CombateRepository;
import com.legado.backend.infrastructure.repository.JugadorRepository;
import com.legado.backend.infrastructure.repository.LogroRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class AchievementService {

    private static final List<LogroDefinicion> CATALOGO = List.of(
        new LogroDefinicion("NOVATO_ARENA",        "Novato de la Arena",      "Gana tu primer combate.",    "Ganar 1 combate"),
        new LogroDefinicion("GUERRERO_VETERANO",   "Guerrero Veterano",       "Gana 10 combates.",          "Ganar 10 combates"),
        new LogroDefinicion("ACAPARADOR_RIQUEZA",  "Acaparador de Riqueza",   "Acumula 1000 monedas.",      "Acumular 1000 monedas")
    );

    private final LogroRepository logroRepository;
    private final JugadorRepository jugadorRepository;
    private final CombateRepository combateRepository;

    public AchievementService(LogroRepository logroRepository,
                              JugadorRepository jugadorRepository,
                              CombateRepository combateRepository) {
        this.logroRepository = logroRepository;
        this.jugadorRepository = jugadorRepository;
        this.combateRepository = combateRepository;
    }

    /**
     * Evalúa todos los logros del catálogo para el jugador dado y persiste
     * los que se acaben de desbloquear. Llamar después de cada combate.
     */
    public void verificarLogros(String jugadorId) {
        Optional<Jugador> optJugador = jugadorRepository.findById(jugadorId);
        if (optJugador.isEmpty()) return;

        Jugador jugador = optJugador.get();
        long victorias = combateRepository.countByGanadorId(jugadorId);
        int monedas = jugador.getMonedas() == null ? 0 : jugador.getMonedas();

        for (LogroDefinicion def : CATALOGO) {
            Logro logro = logroRepository.findByJugadorIdAndNombre(jugadorId, def.nombre)
                    .orElseGet(() -> inicializarLogro(jugadorId, def));

            if (logro.isCompletado()) continue;

            if (evaluarCondicion(def.codigo, victorias, monedas)) {
                logro.setCompletado(true);
                logro.setFechaObtencion(Instant.now());
                logroRepository.save(logro);
            }
        }
    }

    /**
     * Devuelve todos los logros del jugador (desbloqueados y pendientes).
     * Inicializa en DB los que todavía no existan para ese jugador.
     */
    public List<Logro> obtenerLogrosDelJugador(String jugadorId) {
        for (LogroDefinicion def : CATALOGO) {
            logroRepository.findByJugadorIdAndNombre(jugadorId, def.nombre)
                    .orElseGet(() -> inicializarLogro(jugadorId, def));
        }
        return logroRepository.findByJugadorId(jugadorId);
    }

    private Logro inicializarLogro(String jugadorId, LogroDefinicion def) {
        Logro logro = new Logro();
        logro.setJugadorId(jugadorId);
        logro.setNombre(def.nombre);
        logro.setDescripcion(def.descripcion);
        logro.setCondicion(def.condicion);
        logro.setCompletado(false);
        return logroRepository.save(logro);
    }

    private boolean evaluarCondicion(String codigo, long victorias, int monedas) {
        return switch (codigo) {
            case "NOVATO_ARENA"       -> victorias >= 1;
            case "GUERRERO_VETERANO"  -> victorias >= 10;
            case "ACAPARADOR_RIQUEZA" -> monedas >= 1000;
            default -> false;
        };
    }

    private static class LogroDefinicion {
        final String codigo;
        final String nombre;
        final String descripcion;
        final String condicion;

        LogroDefinicion(String codigo, String nombre, String descripcion, String condicion) {
            this.codigo = codigo;
            this.nombre = nombre;
            this.descripcion = descripcion;
            this.condicion = condicion;
        }
    }
}

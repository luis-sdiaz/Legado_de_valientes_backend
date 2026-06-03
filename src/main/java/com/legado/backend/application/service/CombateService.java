package com.legado.backend.application.service;

import com.legado.backend.application.dto.response.RivalResponse;
import com.legado.backend.application.dto.response.CombateResponse;
import com.legado.backend.application.dto.response.JugadorResponse;
import com.legado.backend.domain.model.Combate;
import com.legado.backend.domain.model.Jugador;
import com.legado.backend.domain.model.Mascota;
import com.legado.backend.infrastructure.repository.CombateRepository;
import com.legado.backend.infrastructure.repository.JugadorRepository;
import com.legado.backend.infrastructure.repository.MascotaRepository;
import com.legado.backend.infrastructure.type.Dificultad;
import com.legado.backend.infrastructure.type.TipoMascota;
import com.legado.backend.infrastructure.type.EstadoCombate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class CombateService {

    private final MascotaRepository mascotaRepository;
    private final CombateRepository combateRepository;
    private final JugadorService jugadorService;
    private final JugadorRepository jugadorRepository;
    private final MascotaService mascotaService;
    private final AchievementService achievementService;

    public CombateService(MascotaRepository mascotaRepository,
                          CombateRepository combateRepository,
                          JugadorService jugadorService,
                          JugadorRepository jugadorRepository,
                          MascotaService mascotaService,
                          AchievementService achievementService) {
        this.mascotaRepository = mascotaRepository;
        this.combateRepository = combateRepository;
        this.jugadorService = jugadorService;
        this.jugadorRepository = jugadorRepository;
        this.mascotaService = mascotaService;
        this.achievementService = achievementService;
    }

    /**
     * Genera un rival en memoria según la dificultad solicitada.
     * No persiste en MongoDB.
     */
    public RivalResponse generarRival(Dificultad dificultad, TipoMascota tipoPreferido) {
        // Determinar tipo de mascota del rival
        TipoMascota tipo = tipoPreferido != null ? tipoPreferido : randomTipo();

        // Rango de nivel según dificultad
        int nivel;
        double mult;
        switch (dificultad) {
            case FACIL:
                nivel = randBetween(1, 3);
                mult = 0.9;
                break;
            case MEDIA:
                nivel = randBetween(3, 6);
                mult = 1.0;
                break;
            case DIFICIL:
            default:
                nivel = randBetween(6, 10);
                mult = 1.2;
                break;
        }

        // Stats base por tipo
        int baseSalud, baseAtaque, baseDefensa, baseVelocidad;
        switch (tipo) {
            case DRAGON:
                baseSalud = 120; baseAtaque = 15; baseDefensa = 8; baseVelocidad = 8; break;
            case LEON:
                baseSalud = 110; baseAtaque = 12; baseDefensa = 10; baseVelocidad = 9; break;
            case GORILA:
                baseSalud = 130; baseAtaque = 14; baseDefensa = 12; baseVelocidad = 6; break;
            case AGUILA:
            default:
                baseSalud = 90; baseAtaque = 10; baseDefensa = 6; baseVelocidad = 14; break;
        }

        // Escalar por nivel y dificultad
        int salud = (int) Math.round((baseSalud + nivel * 10) * mult);
        int ataque = (int) Math.round((baseAtaque + nivel * 2) * mult);
        int defensa = (int) Math.round((baseDefensa + nivel * 1) * mult);
        int velocidad = (int) Math.round((baseVelocidad + nivel * 1) * mult);

        RivalResponse r = new RivalResponse();
        r.setNombre("Rival-" + tipo.name() + "-" + randBetween(100, 999));
        r.setTipo(tipo);
        r.setNivel(nivel);
        r.setSalud(salud);
        r.setAtaque(ataque);
        r.setDefensa(defensa);
        r.setVelocidad(velocidad);
        return r;
    }

    private static int randBetween(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }

    private static TipoMascota randomTipo() {
        TipoMascota[] values = TipoMascota.values();
        int i = ThreadLocalRandom.current().nextInt(values.length);
        return values[i];
    }

    /**
     * Inicia un combate entre la mascota activa del jugador y un rival generado.
     */
    public Optional<CombateResponse> iniciarCombateVsRival(String jugadorId, Dificultad dificultad, TipoMascota tipoPreferido) {
        List<Mascota> activas = mascotaRepository.findByJugadorIdAndActivoTrue(jugadorId);
        if (activas == null || activas.isEmpty()) {
            return Optional.empty();
        }
        Mascota jugadorMascota = activas.get(0);

        RivalResponse rival = generarRival(dificultad, tipoPreferido);

        int pjSalud = jugadorMascota.getSalud() == null ? 0 : jugadorMascota.getSalud();
        int pjAtaque = jugadorMascota.getAtaque() == null ? 0 : jugadorMascota.getAtaque();
        int pjDef = jugadorMascota.getDefensa() == null ? 0 : jugadorMascota.getDefensa();
        int pjVel = jugadorMascota.getVelocidad() == null ? 0 : jugadorMascota.getVelocidad();

        int rvSalud = rival.getSalud();
        int rvAtaque = rival.getAtaque();
        int rvDef = rival.getDefensa();
        int rvVel = rival.getVelocidad();

        StringBuilder resumen = new StringBuilder();
        int turnos = 0;

        // Determinar quien inicia por velocidad
        boolean jugadorAtaca = pjVel >= rvVel;

        while (pjSalud > 0 && rvSalud > 0 && turnos < 100) {
            turnos++;
            if (jugadorAtaca) {
                int dano = Math.max(1, pjAtaque - rvDef / 2);
                rvSalud -= dano;
                resumen.append(String.format("Turn %d: Jugador (%s) inflige %d a Rival (%s). Rival vida=%d\n", turnos, jugadorMascota.getNombre(), dano, rival.getNombre(), Math.max(0, rvSalud)));
            } else {
                int dano = Math.max(1, rvAtaque - pjDef / 2);
                pjSalud -= dano;
                resumen.append(String.format("Turn %d: Rival (%s) inflige %d a Jugador (%s). Jugador vida=%d\n", turnos, rival.getNombre(), dano, jugadorMascota.getNombre(), Math.max(0, pjSalud)));
            }
            jugadorAtaca = !jugadorAtaca;
        }

        String ganadorId;
        String perdedorId;
        EstadoCombate estado = EstadoCombate.FINALIZADO;
        if (pjSalud > 0 && rvSalud <= 0) {
            ganadorId = jugadorId;
            perdedorId = "RIVAL";
        } else if (rvSalud > 0 && pjSalud <= 0) {
            ganadorId = "RIVAL";
            perdedorId = jugadorId;
        } else {
            // empate técnico
            ganadorId = null;
            perdedorId = null;
            estado = EstadoCombate.CANCELADO;
        }

        // Persistir combate (registro simple)
        Combate combate = new Combate();
        combate.setJugador1Id(jugadorId);
        combate.setMascota1Id(jugadorMascota.getId());
        combate.setJugador2Id("SYSTEM");
        combate.setMascota2Id(rival.getNombre());
        combate.setEstado(estado);
        combate.setGanadorId(ganadorId);
        combate.setRondas(turnos);
        combate.setPerdedorId(perdedorId);
        combate.setTurnosEjecutados(turnos);
        combate.setResumen(resumen.toString());
        combate.setCreatedAt(Instant.now());

        Combate saved = combateRepository.save(combate);

        CombateResponse resp = new CombateResponse();
        resp.setId(saved.getId());
        resp.setJugador1Id(saved.getJugador1Id());
        resp.setMascota1Id(saved.getMascota1Id());
        resp.setJugador2Id(saved.getJugador2Id());
        resp.setMascota2Id(saved.getMascota2Id());
        resp.setEstado(saved.getEstado());
        resp.setGanadorId(saved.getGanadorId());
        resp.setPerdedorId(perdedorId);
        resp.setRondas(saved.getRondas());
        resp.setTurnosEjecutados(turnos);
        resp.setResumen(resumen.toString());
        resp.setCreatedAt(saved.getCreatedAt());

        // Otorgar XP a la mascota del jugador según resultado del combate
        int xpMascota;
        if (ganadorId != null && ganadorId.equals(jugadorId)) {
            switch (dificultad) {
                case FACIL:  xpMascota = 5;  break;
                case MEDIA:  xpMascota = 10; break;
                case DIFICIL:
                default:     xpMascota = 20; break;
            }
        } else {
            xpMascota = 2;
        }
        mascotaService.otorgarExperienciaMascota(jugadorMascota.getId(), xpMascota);

        if (ganadorId != null && ganadorId.equals(jugadorId)) {
            int expGain;
            int coinGain;
            switch (dificultad) {
                case FACIL:
                    expGain = 10; coinGain = 5; break;
                case MEDIA:
                    expGain = 20; coinGain = 10; break;
                case DIFICIL:
                default:
                    expGain = 40; coinGain = 20; break;
            }
            Optional<JugadorResponse> updated = jugadorService.otorgarRecompensa(jugadorId, expGain, coinGain);
            updated.ifPresent(resp::setJugadorGanador);
        }

        // Verificar logros después de cada combate (monedas y victorias ya actualizadas en DB)
        achievementService.verificarLogros(jugadorId);

        return Optional.of(resp);
    }

    /**
     * Obtiene un combate por id y lo mapea a CombateResponse.
     */
    public Optional<CombateResponse> obtenerCombatePorId(String id) {
        return combateRepository.findById(id).map(saved -> {
            CombateResponse resp = new CombateResponse();
            resp.setId(saved.getId());
            resp.setJugador1Id(saved.getJugador1Id());
            resp.setMascota1Id(saved.getMascota1Id());
            resp.setJugador2Id(saved.getJugador2Id());
            resp.setMascota2Id(saved.getMascota2Id());
            resp.setEstado(saved.getEstado());
            resp.setGanadorId(saved.getGanadorId());
            resp.setPerdedorId(saved.getPerdedorId());
            resp.setRondas(saved.getRondas());
            resp.setTurnosEjecutados(saved.getTurnosEjecutados());
            resp.setResumen(saved.getResumen());
            resp.setCreatedAt(saved.getCreatedAt());
            return resp;
        });
    }

    /**
     * Registra el resultado de un combate local sin pasar por el flujo de turnos.
     * Calcula las recompensas según dificultad y resultado, actualiza victorias/derrotas
     * y aplica la lógica de subida de nivel. Devuelve el jugador actualizado.
     */
    public Optional<JugadorResponse> registrarResultado(String jugadorId, String dificultad, String resultado) {
        Optional<Jugador> optJugador = jugadorRepository.findById(jugadorId);
        if (optJugador.isEmpty()) {
            return Optional.empty();
        }

        Jugador jugador = optJugador.get();

        // Inicializar campos en caso de que sean null (jugadores viejos en la BD)
        if (jugador.getExperiencia() == null) jugador.setExperiencia(0);
        if (jugador.getMonedas()     == null) jugador.setMonedas(0);
        if (jugador.getNivel()       == null) jugador.setNivel(1);
        if (jugador.getVictorias()   == null) jugador.setVictorias(0);
        if (jugador.getDerrotas()    == null) jugador.setDerrotas(0);

        int monedasGanadas;
        int expGanada;

        if ("ganado".equalsIgnoreCase(resultado)) {
            switch (dificultad) {
                case "Fácil":
                    monedasGanadas = 50;
                    expGanada      = 30;
                    break;
                case "Medio":
                    monedasGanadas = 100;
                    expGanada      = 60;
                    break;
                case "Difícil":
                default:
                    monedasGanadas = 200;
                    expGanada      = 120;
                    break;
            }
            jugador.setVictorias(jugador.getVictorias() + 1);

        } else {
            // Recompensa de consolación por derrota
            monedasGanadas = 10;
            expGanada      = 5;
            jugador.setDerrotas(jugador.getDerrotas() + 1);
        }

        jugador.setMonedas(jugador.getMonedas() + monedasGanadas);
        int nuevaExp = jugador.getExperiencia() + expGanada;
        int nivel    = jugador.getNivel();

        // Umbral: 100 * nivelActual de XP para subir de nivel
        int umbral = 100 * nivel;
        while (nuevaExp >= umbral) {
            nuevaExp -= umbral;
            nivel++;
            umbral = 100 * nivel;
        }
        jugador.setExperiencia(nuevaExp);
        jugador.setNivel(nivel);

        Jugador guardado = jugadorRepository.save(jugador);

        boolean ganado = "ganado".equalsIgnoreCase(resultado);
        Combate historial = new Combate();
        historial.setJugador1Id(jugadorId);
        historial.setJugador2Id("SYSTEM");
        historial.setMascota2Id("Rival " + dificultad);
        historial.setEstado(EstadoCombate.FINALIZADO);
        historial.setGanadorId(ganado ? jugadorId : "SYSTEM");
        historial.setPerdedorId(ganado ? "SYSTEM" : jugadorId);
        historial.setResumen(String.format(
                "%s · Dificultad: %s · +%d monedas · +%d XP",
                ganado ? "¡Victoria!" : "Derrota", dificultad, monedasGanadas, expGanada
        ));
        historial.setCreatedAt(Instant.now());
        combateRepository.save(historial);

        // Verificar logros después de persistir el combate (victorias y monedas ya en DB)
        achievementService.verificarLogros(jugadorId);

        JugadorResponse response = new JugadorResponse();
        response.setId(guardado.getId());
        response.setNombre(guardado.getNombre());
        response.setEmail(guardado.getEmail());
        response.setNivel(guardado.getNivel());
        response.setExperiencia(guardado.getExperiencia());
        response.setMonedas(guardado.getMonedas());
        response.setVictorias(guardado.getVictorias());
        response.setDerrotas(guardado.getDerrotas());

        return Optional.of(response);
    }

    /**
     * Lista combates de un jugador, ordenados por fecha descendente.
     */
    public List<CombateResponse> listarCombatesPorJugador(String jugadorId) {
        List<Combate> combates = combateRepository.findByJugador1IdOrJugador2IdOrderByCreatedAtDesc(jugadorId, jugadorId);
        return combates.stream().map(saved -> {
            CombateResponse resp = new CombateResponse();
            resp.setId(saved.getId());
            resp.setJugador1Id(saved.getJugador1Id());
            resp.setMascota1Id(saved.getMascota1Id());
            resp.setJugador2Id(saved.getJugador2Id());
            resp.setMascota2Id(saved.getMascota2Id());
            resp.setEstado(saved.getEstado());
            resp.setGanadorId(saved.getGanadorId());
            resp.setPerdedorId(saved.getPerdedorId());
            resp.setRondas(saved.getRondas());
            resp.setTurnosEjecutados(saved.getTurnosEjecutados());
            resp.setResumen(saved.getResumen());
            resp.setCreatedAt(saved.getCreatedAt());
            return resp;
        }).toList();
    }
}

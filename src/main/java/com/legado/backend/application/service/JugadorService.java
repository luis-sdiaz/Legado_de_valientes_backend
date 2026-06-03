package com.legado.backend.application.service;

import com.legado.backend.application.dto.request.JugadorRequest;
import com.legado.backend.application.dto.response.EstadisticasJugadorResponse;
import com.legado.backend.application.dto.response.JugadorResponse;
import com.legado.backend.application.dto.response.LogroResponse;
import com.legado.backend.application.dto.response.ProgresoPerfilResponse;
import com.legado.backend.domain.model.Jugador;
import com.legado.backend.domain.model.Logro;
import com.legado.backend.infrastructure.repository.CombateRepository;
import com.legado.backend.infrastructure.repository.JugadorRepository;
import com.legado.backend.infrastructure.repository.MascotaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class JugadorService {

    private final JugadorRepository jugadorRepository;
    private final MascotaRepository mascotaRepository;
    private final CombateRepository combateRepository;
    private final AchievementService achievementService;

    public JugadorService(JugadorRepository jugadorRepository,
                          MascotaRepository mascotaRepository,
                          CombateRepository combateRepository,
                          AchievementService achievementService) {
        this.jugadorRepository = jugadorRepository;
        this.mascotaRepository = mascotaRepository;
        this.combateRepository = combateRepository;
        this.achievementService = achievementService;
    }

    /** Aplica XP y monedas al jugador, subiendo de nivel si alcanza el umbral (100 × nivel). */
    public Optional<JugadorResponse> otorgarRecompensa(String jugadorId, int experienciaGanada, int monedasGanadas) {
        return jugadorRepository.findById(jugadorId).map(jugador -> {
            if (jugador.getExperiencia() == null) jugador.setExperiencia(0);
            if (jugador.getMonedas() == null) jugador.setMonedas(0);
            if (jugador.getNivel() == null) jugador.setNivel(1);

            int nuevaExp = jugador.getExperiencia() + experienciaGanada;
            int nuevasMonedas = jugador.getMonedas() + monedasGanadas;
            int nivel = jugador.getNivel();

            int umbral = 100 * nivel;
            while (nuevaExp >= umbral) {
                nuevaExp -= umbral;
                nivel++;
                umbral = 100 * nivel;
            }

            jugador.setExperiencia(nuevaExp);
            jugador.setMonedas(nuevasMonedas);
            jugador.setNivel(nivel);

            Jugador saved = jugadorRepository.save(jugador);
            int mascotas = (int) mascotaRepository.countByJugadorId(saved.getId());
            return toJugadorResponse(saved, mascotas);
        });
    }

    /**
     * Compatibilidad: método plural esperado por CombateService.
     */
    public Optional<JugadorResponse> otorgarRecompensas(String jugadorId, int experienciaGanada, int monedasGanadas) {
        return otorgarRecompensa(jugadorId, experienciaGanada, monedasGanadas);
    }

    /**
     * Obtiene estadísticas resumidas del jugador usando su perfil y el historial
     * de combates. Devuelve Optional.empty() si el jugador no existe.
     */
    public Optional<EstadisticasJugadorResponse> obtenerEstadisticasJugador(String jugadorId) {
        Optional<Jugador> opt = jugadorRepository.findById(jugadorId);
        if (opt.isEmpty()) return Optional.empty();
        Jugador j = opt.get();

        EstadisticasJugadorResponse stats = new EstadisticasJugadorResponse();
        stats.setNivel(j.getNivel() == null ? 1 : j.getNivel());
        stats.setExperiencia(j.getExperiencia() == null ? 0 : j.getExperiencia());
        stats.setMonedas(j.getMonedas() == null ? 0 : j.getMonedas());

        long mascotas = 0L;
        try {
            mascotas = mascotaRepository.countByJugadorId(jugadorId);
        } catch (Exception e) {
            mascotas = 0L;
        }
        stats.setCantidadMascotas((int) mascotas);

        long victorias = combateRepository.countByGanadorId(jugadorId);
        long derrotas = combateRepository.countByPerdedorId(jugadorId);
        long total = combateRepository.countByJugador1IdOrJugador2Id(jugadorId, jugadorId);

        stats.setVictorias(victorias);
        stats.setDerrotas(derrotas);
        stats.setTotalCombates(total);

        return Optional.of(stats);
    }

    /**
     * Obtener progreso del perfil del jugador: combina datos del perfil con
     * estadísticas básicas y calcula el progreso hacia el siguiente nivel.
     */
    public Optional<ProgresoPerfilResponse> obtenerProgresoPerfil(String jugadorId) {
        Optional<Jugador> opt = jugadorRepository.findById(jugadorId);
        if (opt.isEmpty()) return Optional.empty();
        Jugador j = opt.get();

        ProgresoPerfilResponse p = new ProgresoPerfilResponse();
        int nivel = j.getNivel() == null ? 1 : j.getNivel();
        int experiencia = j.getExperiencia() == null ? 0 : j.getExperiencia();
        int monedas = j.getMonedas() == null ? 0 : j.getMonedas();

        p.setNivelActual(nivel);
        p.setExperienciaAcumulada(experiencia);
        p.setMonedas(monedas);

        long mascotas = 0L;
        try { mascotas = mascotaRepository.countByJugadorId(jugadorId); } catch (Exception e) { mascotas = 0L; }
        p.setCantidadMascotas((int) mascotas);

        long victorias = combateRepository.countByGanadorId(jugadorId);
        long derrotas = combateRepository.countByPerdedorId(jugadorId);
        long total = combateRepository.countByJugador1IdOrJugador2Id(jugadorId, jugadorId);

        p.setVictorias(victorias);
        p.setDerrotas(derrotas);
        p.setCantidadCombates(total);

        int umbral = 100 * nivel;
        int porcentaje = 0;
        if (umbral > 0) {
            porcentaje = (int) Math.round((double) experiencia / (double) umbral * 100.0);
            if (porcentaje < 0) porcentaje = 0;
            if (porcentaje > 100) porcentaje = 100;
        }
        p.setProgresoNivelPorcentaje(porcentaje);

        return Optional.of(p);
    }

    /**
     * Obtiene todos los logros del jugador (desbloqueados y pendientes)
     * usando el sistema persistente de AchievementService.
     */
    public Optional<List<LogroResponse>> obtenerLogrosJugador(String jugadorId) {
        if (!jugadorRepository.existsById(jugadorId)) return Optional.empty();

        List<Logro> logros = achievementService.obtenerLogrosDelJugador(jugadorId);
        List<LogroResponse> responses = logros.stream()
                .map(logro -> {
                    LogroResponse r = new LogroResponse();
                    r.setNombre(logro.getNombre());
                    r.setDescripcion(logro.getDescripcion());
                    r.setCondicion(logro.getCondicion());
                    r.setCompletado(logro.isCompletado());
                    r.setFechaObtencion(logro.getFechaObtencion());
                    return r;
                })
                .collect(Collectors.toList());
        return Optional.of(responses);
    }

    public JugadorResponse crearJugador(JugadorRequest request) {
        Jugador jugador = new Jugador();
        jugador.setNombre(request.getNombre());
        jugador.setEmail(request.getEmail());
        jugador.setNivel(1);
        jugador.setExperiencia(0);
        jugador.setMonedas(300);
        jugador.setVictorias(0);
        jugador.setDerrotas(0);
        Jugador saved = jugadorRepository.save(jugador);
        return toJugadorResponse(saved, 0);
    }

    public List<JugadorResponse> listarJugadores() {
        return jugadorRepository.findAll().stream()
                .map(saved -> toJugadorResponse(saved, (int) mascotaRepository.countByJugadorId(saved.getId())))
                .collect(Collectors.toList());
    }

    /**
     * Obtiene un jugador por su id y lo mapea a JugadorResponse.
     * Incluye el total de mascotas que tiene el jugador.
     */
    public Optional<JugadorResponse> obtenerJugadorPorId(String id) {
        return jugadorRepository.findById(id).map(saved -> {
            int mascotas = (int) mascotaRepository.countByJugadorId(saved.getId());
            return toJugadorResponse(saved, mascotas);
        });
    }

    public Optional<JugadorResponse> buscarPorEmail(String email) {
        return jugadorRepository.findByEmail(email).map(saved -> {
            int mascotas = (int) mascotaRepository.countByJugadorId(saved.getId());
            return toJugadorResponse(saved, mascotas);
        });
    }

    public Optional<JugadorResponse> actualizarJugador(String id, JugadorRequest request) {
        Optional<Jugador> opt = jugadorRepository.findById(id);
        if (opt.isEmpty()) return Optional.empty();
        Jugador jugador = opt.get();
        if (request.getNombre() != null) jugador.setNombre(request.getNombre());
        if (request.getEmail() != null) jugador.setEmail(request.getEmail());
        if (request.getMonedas() != null) jugador.setMonedas(request.getMonedas());

        Jugador saved = jugadorRepository.save(jugador);
        int mascotas = (int) mascotaRepository.countByJugadorId(saved.getId());
        return Optional.of(toJugadorResponse(saved, mascotas));
    }

    public boolean eliminarJugador(String id) {
        if (!jugadorRepository.existsById(id)) return false;
        jugadorRepository.deleteById(id);
        return true;
    }

    private JugadorResponse toJugadorResponse(Jugador saved, int cantidadMascotas) {
        int nivel = saved.getNivel() == null ? 1 : saved.getNivel();
        JugadorResponse response = new JugadorResponse();
        response.setId(saved.getId());
        response.setNombre(saved.getNombre());
        response.setEmail(saved.getEmail());
        response.setNivel(nivel);
        response.setExperiencia(saved.getExperiencia() == null ? 0 : saved.getExperiencia());
        response.setMonedas(saved.getMonedas() == null ? 0 : saved.getMonedas());
        response.setVictorias(saved.getVictorias() == null ? 0 : saved.getVictorias());
        response.setDerrotas(saved.getDerrotas() == null ? 0 : saved.getDerrotas());
        response.setCantidadMascotas(cantidadMascotas);
        response.setXpSiguienteNivel(nivel * 100);
        return response;
    }
}

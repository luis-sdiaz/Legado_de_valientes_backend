package com.legado.backend.application.service;

import com.legado.backend.application.dto.request.MascotaRequest;
import com.legado.backend.application.dto.response.ComprarMascotaResponse;
import com.legado.backend.application.dto.response.EntrenarMascotaResponse;
import com.legado.backend.application.dto.response.EvolucionResponse;
import com.legado.backend.application.dto.response.JugadorResponse;
import com.legado.backend.application.dto.response.MascotaResponse;
import com.legado.backend.domain.model.Dragon;
import com.legado.backend.domain.model.Jugador;
import com.legado.backend.domain.model.Mascota;
import com.legado.backend.infrastructure.repository.JugadorRepository;
import com.legado.backend.infrastructure.repository.MascotaRepository;
import com.legado.backend.infrastructure.type.TipoEntrenamiento;
import com.legado.backend.infrastructure.type.TipoMascota;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MascotaService {

    private static final int COSTO_MASCOTA = 300;

    private final MascotaRepository mascotaRepository;
    private final JugadorRepository jugadorRepository;

    public MascotaService(MascotaRepository mascotaRepository, JugadorRepository jugadorRepository) {
        this.mascotaRepository = mascotaRepository;
        this.jugadorRepository = jugadorRepository;
    }

    /**
     * Compra y crea una mascota para el jugador.
     * Descuenta 300 monedas antes de crear la mascota.
     * Lanza IllegalArgumentException si el jugador no existe.
     * Lanza IllegalStateException si no tiene suficientes monedas.
     */
    public ComprarMascotaResponse crearMascota(MascotaRequest request) {
        // 1. Verificar que el jugador existe
        Jugador jugador = jugadorRepository.findById(request.getJugadorId())
                .orElseThrow(() -> new IllegalArgumentException("Jugador no encontrado"));

        // 2. Verificar que tiene monedas suficientes
        int monedasActuales = jugador.getMonedas() == null ? 0 : jugador.getMonedas();
        if (monedasActuales < COSTO_MASCOTA) {
            throw new IllegalStateException("Monedas insuficientes. Necesitas " + COSTO_MASCOTA + " monedas.");
        }

        // 3. Descontar las monedas y guardar el jugador actualizado
        jugador.setMonedas(monedasActuales - COSTO_MASCOTA);
        Jugador jugadorGuardado = jugadorRepository.save(jugador);

        // 4. Crear la mascota según su tipo
        TipoMascota tipo = request.getTipo();
        Mascota mascota;
        switch (tipo) {
            case DRAGON:
                mascota = new Dragon();
                break;
            case LEON:
                mascota = new com.legado.backend.domain.model.Leon();
                break;
            case GORILA:
                mascota = new com.legado.backend.domain.model.Gorila();
                break;
            case AGUILA:
            default:
                mascota = new com.legado.backend.domain.model.Aguila();
                break;
        }

        mascota.setNombre(request.getNombre());
        mascota.setJugadorId(request.getJugadorId());
        mascota.setTipo(tipo);
        mascota.setRareza(request.getRareza());
        mascota.setNivel(1);
        mascota.setExperiencia(0);
        mascota.setSalud(100);
        mascota.setSaludMaximo(100);
        mascota.setAtaque(10);
        mascota.setDefensa(5);
        mascota.setVelocidad(10);
        mascota.setActivo(false);

        Mascota mascotaGuardada = mascotaRepository.save(mascota);

        MascotaResponse mascotaResponse = toMascotaResponse(mascotaGuardada, "");

        int totalMascotas = (int) mascotaRepository.countByJugadorId(jugadorGuardado.getId());

        JugadorResponse jugadorResponse = new JugadorResponse();
        jugadorResponse.setId(jugadorGuardado.getId());
        jugadorResponse.setNombre(jugadorGuardado.getNombre());
        jugadorResponse.setEmail(jugadorGuardado.getEmail());
        jugadorResponse.setNivel(jugadorGuardado.getNivel());
        jugadorResponse.setExperiencia(jugadorGuardado.getExperiencia());
        jugadorResponse.setMonedas(jugadorGuardado.getMonedas());
        jugadorResponse.setVictorias(jugadorGuardado.getVictorias());
        jugadorResponse.setDerrotas(jugadorGuardado.getDerrotas());
        jugadorResponse.setCantidadMascotas(totalMascotas);

        return new ComprarMascotaResponse(mascotaResponse, jugadorResponse);
    }

    public List<MascotaResponse> obtenerMascotasPorJugador(String jugadorId) {
        List<Mascota> mascotas = mascotaRepository.findByJugadorId(jugadorId);
        return mascotas.stream()
                .map(saved -> toMascotaResponse(saved, ""))
                .collect(Collectors.toList());
    }

    public Optional<MascotaResponse> obtenerMascotaPorId(String id) {
        return mascotaRepository.findById(id)
                .map(saved -> toMascotaResponse(saved, ""));
    }

    private static final int COSTO_ENTRENAMIENTO = 20;

    /**
     * Entrena una mascota con un tipo de entrenamiento específico (ATAQUE, DEFENSA o VELOCIDAD).
     * Descuenta 20 monedas del jugador y aplica el aumento de atributo correspondiente.
     * La mascota gana 5 puntos de experiencia y puede subir de nivel (umbral: nivel * 10 XP).
     *
     * Lanza IllegalArgumentException si la mascota o el jugador no existen.
     * Lanza IllegalStateException si el jugador no tiene suficientes monedas.
     */
    public EntrenarMascotaResponse entrenarMascota(String mascotaId, String jugadorIdHeader, TipoEntrenamiento tipo) {
        Mascota m = mascotaRepository.findById(mascotaId)
                .orElseThrow(() -> new IllegalArgumentException("Mascota no encontrada"));

        // Obtener jugadorId: del header si se provee, o del propietario de la mascota
        String jugadorId = (jugadorIdHeader != null && !jugadorIdHeader.isBlank())
                ? jugadorIdHeader
                : m.getJugadorId();

        Jugador jugador = jugadorRepository.findById(jugadorId)
                .orElseThrow(() -> new IllegalArgumentException("Jugador no encontrado"));

        int monedas = jugador.getMonedas() == null ? 0 : jugador.getMonedas();
        if (monedas < COSTO_ENTRENAMIENTO) {
            throw new IllegalStateException("Monedas insuficientes. Necesitas " + COSTO_ENTRENAMIENTO + " monedas.");
        }

        jugador.setMonedas(monedas - COSTO_ENTRENAMIENTO);
        jugadorRepository.save(jugador);

        switch (tipo) {
            case ATAQUE:
                m.setAtaque((m.getAtaque() == null ? 0 : m.getAtaque()) + 2);
                break;
            case DEFENSA:
                m.setDefensa((m.getDefensa() == null ? 0 : m.getDefensa()) + 2);
                break;
            case VELOCIDAD:
                m.setVelocidad((m.getVelocidad() == null ? 0 : m.getVelocidad()) + 1);
                break;
        }

        m.setExperiencia((m.getExperiencia() == null ? 0 : m.getExperiencia()) + 5);
        verificarSubidaNivel(m);

        Mascota mascotaGuardada = mascotaRepository.save(m);
        MascotaResponse resp = toMascotaResponse(mascotaGuardada, "Entrenamiento de " + tipo.name().toLowerCase() + " completado");
        return new EntrenarMascotaResponse(resp, jugador.getMonedas());
    }

    /**
     * Intenta evolucionar una mascota por id. Retorna EvolucionResult con el resultado.
     * Condición mínima: nivel >= 3 y experiencia >= 30
     */
    public EvolucionResponse evolucionarMascota(String mascotaId) {
         Optional<Mascota> opt = mascotaRepository.findById(mascotaId);
         if (opt.isEmpty()) {
             return EvolucionResponse.notFound();
         }
         Mascota m = opt.get();
         Integer nivel = m.getNivel() == null ? 0 : m.getNivel();
         Integer exp = m.getExperiencia() == null ? 0 : m.getExperiencia();
         boolean puede = nivel >= 3 && exp >= 30;
         if (!puede) {
             String reason = "Requiere nivel >= 3 y experiencia >= 30";
             return EvolucionResponse.notEligible(reason);
        }
        m.evolucionar();
        Mascota saved = mascotaRepository.save(m);
        MascotaResponse resp = toMascotaResponse(saved, "Evolucionada");
        return EvolucionResponse.success(resp);
    }

    /**
     * Selecciona una mascota para combate: marca la mascota indicada como activa y desactiva las otras del mismo jugador.
     */
    public Optional<MascotaResponse> seleccionarMascota(String mascotaId) {
        Optional<Mascota> opt = mascotaRepository.findById(mascotaId);
        if (opt.isEmpty()) {
            return Optional.empty();
        }
        Mascota target = opt.get();
        String jugadorId = target.getJugadorId();
        // Desactivar otras mascotas del jugador
        List<Mascota> otras = mascotaRepository.findByJugadorId(jugadorId);
        for (Mascota m : otras) {
            if (m.getActivo() != null && m.getActivo() && !m.getId().equals(target.getId())) {
                m.setActivo(false);
                mascotaRepository.save(m);
            }
        }
        target.setActivo(true);
        Mascota saved = mascotaRepository.save(target);
        return Optional.of(toMascotaResponse(saved, "Seleccionada para combate"));
    }

    /** Actualizar datos de la mascota, incluyendo atributos de entrenamiento */
    public Optional<MascotaResponse> actualizarMascota(String id, MascotaRequest request) {
        Optional<Mascota> opt = mascotaRepository.findById(id);
        if (opt.isEmpty()) return Optional.empty();
        Mascota mascota = opt.get();
        if (request.getNombre() != null) mascota.setNombre(request.getNombre());
        if (request.getTipo() != null) mascota.setTipo(request.getTipo());
        if (request.getRareza() != null) mascota.setRareza(request.getRareza());
        if (request.getAtaque() != null) mascota.setAtaque(request.getAtaque());
        if (request.getDefensa() != null) mascota.setDefensa(request.getDefensa());
        if (request.getExperiencia() != null) mascota.setExperiencia(request.getExperiencia());
        if (request.getNivel() != null) mascota.setNivel(request.getNivel());

        Mascota saved = mascotaRepository.save(mascota);
        return Optional.of(toMascotaResponse(saved, "Actualizada"));
    }

    /**
     * Añade XP a la mascota y verifica si sube de nivel.
     * Usado por CombateService al finalizar un combate.
     */
    public void otorgarExperienciaMascota(String mascotaId, int xp) {
        mascotaRepository.findById(mascotaId).ifPresent(m -> {
            m.setExperiencia((m.getExperiencia() == null ? 0 : m.getExperiencia()) + xp);
            verificarSubidaNivel(m);
            mascotaRepository.save(m);
        });
    }

    /**
     * Sube de nivel la mascota mientras tenga XP suficiente (umbral: nivel * 10).
     * En cada subida aplica boosts de atributos según la especie y actualiza saludMaximo.
     * Solo modifica el objeto en memoria; el llamador es responsable de persistir.
     */
    private void verificarSubidaNivel(Mascota mascota) {
        int exp = mascota.getExperiencia() == null ? 0 : mascota.getExperiencia();
        int nivel = mascota.getNivel() == null ? 1 : mascota.getNivel();

        while (exp >= nivel * 10) {
            exp -= nivel * 10;
            nivel++;

            switch (mascota.getTipo()) {
                case DRAGON:
                    mascota.setSalud((mascota.getSalud() == null ? 0 : mascota.getSalud()) + 15);
                    mascota.setSaludMaximo((mascota.getSaludMaximo() == null ? 0 : mascota.getSaludMaximo()) + 15);
                    mascota.setAtaque((mascota.getAtaque() == null ? 0 : mascota.getAtaque()) + 5);
                    mascota.setDefensa((mascota.getDefensa() == null ? 0 : mascota.getDefensa()) + 3);
                    mascota.setVelocidad((mascota.getVelocidad() == null ? 0 : mascota.getVelocidad()) + 2);
                    break;
                case LEON:
                    mascota.setSalud((mascota.getSalud() == null ? 0 : mascota.getSalud()) + 12);
                    mascota.setSaludMaximo((mascota.getSaludMaximo() == null ? 0 : mascota.getSaludMaximo()) + 12);
                    mascota.setAtaque((mascota.getAtaque() == null ? 0 : mascota.getAtaque()) + 4);
                    mascota.setDefensa((mascota.getDefensa() == null ? 0 : mascota.getDefensa()) + 4);
                    mascota.setVelocidad((mascota.getVelocidad() == null ? 0 : mascota.getVelocidad()) + 2);
                    break;
                case GORILA:
                    mascota.setSalud((mascota.getSalud() == null ? 0 : mascota.getSalud()) + 20);
                    mascota.setSaludMaximo((mascota.getSaludMaximo() == null ? 0 : mascota.getSaludMaximo()) + 20);
                    mascota.setAtaque((mascota.getAtaque() == null ? 0 : mascota.getAtaque()) + 6);
                    mascota.setDefensa((mascota.getDefensa() == null ? 0 : mascota.getDefensa()) + 2);
                    mascota.setVelocidad((mascota.getVelocidad() == null ? 0 : mascota.getVelocidad()) + 1);
                    break;
                case AGUILA:
                default:
                    mascota.setSalud((mascota.getSalud() == null ? 0 : mascota.getSalud()) + 8);
                    mascota.setSaludMaximo((mascota.getSaludMaximo() == null ? 0 : mascota.getSaludMaximo()) + 8);
                    mascota.setAtaque((mascota.getAtaque() == null ? 0 : mascota.getAtaque()) + 3);
                    mascota.setDefensa((mascota.getDefensa() == null ? 0 : mascota.getDefensa()) + 2);
                    mascota.setVelocidad((mascota.getVelocidad() == null ? 0 : mascota.getVelocidad()) + 4);
                    break;
            }
        }

        mascota.setExperiencia(exp);
        mascota.setNivel(nivel);
    }

    public boolean eliminarMascota(String id) {
        if (!mascotaRepository.existsById(id)) return false;
        mascotaRepository.deleteById(id);
        return true;
    }

    private MascotaResponse toMascotaResponse(Mascota m, String descripcion) {
        MascotaResponse resp = new MascotaResponse();
        resp.setId(m.getId());
        resp.setNombre(m.getNombre());
        resp.setJugadorId(m.getJugadorId());
        resp.setTipo(m.getTipo());
        resp.setRareza(m.getRareza());
        resp.setNivel(m.getNivel());
        resp.setExperiencia(m.getExperiencia());
        resp.setSalud(m.getSalud());
        resp.setAtaque(m.getAtaque());
        resp.setDefensa(m.getDefensa());
        resp.setVelocidad(m.getVelocidad());
        resp.setDescripcion(descripcion);
        resp.setActivo(m.getActivo());

        if (m instanceof Dragon) {
            Dragon dragon = (Dragon) m;
            resp.setTemperaturaFuego(dragon.getTemperaturaFuego());
            resp.setAlcanceVuelo(dragon.getAlcanceVuelo());
        }

        return resp;
    }
}

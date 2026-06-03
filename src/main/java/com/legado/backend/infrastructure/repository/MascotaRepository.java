package com.legado.backend.infrastructure.repository;

import com.legado.backend.domain.model.Mascota;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MascotaRepository extends MongoRepository<Mascota, String> {

    List<Mascota> findByJugadorId(String jugadorId);

    List<Mascota> findByJugadorIdAndActivoTrue(String jugadorId);
    long countByJugadorId(String jugadorId);
    long countByJugadorIdAndNivelGreaterThanEqual(String jugadorId, Integer nivel);

}

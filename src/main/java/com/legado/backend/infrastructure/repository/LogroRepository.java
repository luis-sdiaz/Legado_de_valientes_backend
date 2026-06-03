package com.legado.backend.infrastructure.repository;

import com.legado.backend.domain.model.Logro;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LogroRepository extends MongoRepository<Logro, String> {
    List<Logro> findByJugadorId(String jugadorId);
    Optional<Logro> findByJugadorIdAndNombre(String jugadorId, String nombre);
}

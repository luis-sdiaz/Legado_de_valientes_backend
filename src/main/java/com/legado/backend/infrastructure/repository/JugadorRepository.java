package com.legado.backend.infrastructure.repository;

import com.legado.backend.domain.model.Jugador;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JugadorRepository extends MongoRepository<Jugador, String> {

    Optional<Jugador> findByEmail(String email);

}

package com.legado.backend.infrastructure.repository;

import com.legado.backend.domain.model.Combate;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CombateRepository extends MongoRepository<Combate, String> {

    List<Combate> findByJugador1IdOrJugador2IdOrderByCreatedAtDesc(String jugador1Id, String jugador2Id);
    long countByGanadorId(String ganadorId);
    long countByPerdedorId(String perdedorId);
    long countByJugador1IdOrJugador2Id(String jugador1Id, String jugador2Id);

}

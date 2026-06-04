package com.sistema.sistema.repositories;

import com.sistema.sistema.entities.encuestas.PreguntaEncuesta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PreguntaEncuestaRepository extends JpaRepository<PreguntaEncuesta, Long> {
    List<PreguntaEncuesta> findAllByOrderByOrdenAsc();
}
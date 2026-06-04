package com.sistema.sistema.repositories;

import com.sistema.sistema.entities.encuestas.RespuestaPregunta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RespuestaPreguntaRepository extends JpaRepository<RespuestaPregunta, Long> {

    @Query("SELECT rp.preguntaEncuesta.enunciado, AVG(rp.calificacion) " +
           "FROM RespuestaPregunta rp " +
           "WHERE rp.respuestaEncuesta.comision.idComision = :idComision " +
           "GROUP BY rp.preguntaEncuesta.idPreguntaEncuesta, rp.preguntaEncuesta.enunciado, rp.preguntaEncuesta.orden " +
           "ORDER BY rp.preguntaEncuesta.orden")
    List<Object[]> promediosPorPregunta(@Param("idComision") Long idComision);
}
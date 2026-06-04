package com.sistema.sistema.repositories;

import com.sistema.sistema.entities.encuestas.RespuestaEncuesta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RespuestaEncuestaRepository extends JpaRepository<RespuestaEncuesta, Long> {
    boolean existsByComision_IdComisionAndHashAlumno(Long idComision, String hashAlumno);
}
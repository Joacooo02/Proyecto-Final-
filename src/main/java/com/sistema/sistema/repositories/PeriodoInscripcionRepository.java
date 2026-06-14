package com.sistema.sistema.repositories;

import com.sistema.sistema.entities.areaAcademica.PeriodoInscripcion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PeriodoInscripcionRepository extends JpaRepository<PeriodoInscripcion, Long> {
}

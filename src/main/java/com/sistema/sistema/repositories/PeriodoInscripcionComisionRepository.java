package com.sistema.sistema.repositories;

import com.sistema.sistema.entities.areaAcademica.PeriodoInscripcion;
import com.sistema.sistema.entities.areaAcademica.PeriodoInscripcionComision;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PeriodoInscripcionComisionRepository extends JpaRepository<PeriodoInscripcionComision,Long>
{
    List<PeriodoInscripcionComision> findByPeriodoIdPeriodo(Long idPeriodo);
    boolean existsByPeriodo_IdPeriodoAndComision_IdComision(Long idPeriodo, Long idComision);
}

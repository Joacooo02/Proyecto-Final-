package com.sistema.sistema.repositories;

import com.sistema.sistema.entities.areaAcademica.PlanDeEstudio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlanDeEstudioRepository extends JpaRepository<PlanDeEstudio, Long> {
}

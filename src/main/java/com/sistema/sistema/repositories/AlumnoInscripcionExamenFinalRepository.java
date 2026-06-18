package com.sistema.sistema.repositories;

import com.sistema.sistema.entities.areaAdministrativa.AlumnoInscripcionExamenFinal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlumnoInscripcionExamenFinalRepository extends JpaRepository<AlumnoInscripcionExamenFinal, Long> {
    List<AlumnoInscripcionExamenFinal> findByExamen_IdExamen(Long idExamen);
}

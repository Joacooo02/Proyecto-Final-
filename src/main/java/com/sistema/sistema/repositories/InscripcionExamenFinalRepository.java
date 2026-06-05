package com.sistema.sistema.repositories;

import com.sistema.sistema.entities.areaAdministrativa.AlumnoInscripcionExamenFinal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InscripcionExamenFinalRepository extends JpaRepository<AlumnoInscripcionExamenFinal, Long> {
}

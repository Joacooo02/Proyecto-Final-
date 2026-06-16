package com.sistema.sistema.repositories;

import com.sistema.sistema.entities.areaAcademica.AlumnoMateriaAprobada;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlumnoMateriaAprobadaRepository extends JpaRepository<AlumnoMateriaAprobada, Long> {
    boolean existsByAlumno_IdPersonaAndMateria_IdMateria(Long idAlumno, Long idMateria);
}

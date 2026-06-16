package com.sistema.sistema.repositories;

import com.sistema.sistema.entities.areaAcademica.AlumnoMateria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlumnoInscripcionMateriaRepository extends JpaRepository<AlumnoMateria, Long> {
    boolean existsByAlumnoIdPersonaAndMateriaIdMateria(Long idAlumno, Long idMateria);
}

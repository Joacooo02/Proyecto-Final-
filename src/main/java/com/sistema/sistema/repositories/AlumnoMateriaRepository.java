package com.sistema.sistema.repositories;

import com.sistema.sistema.entities.areaAcademica.AlumnoMateria;
import com.sistema.sistema.enums.EstadoMateria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AlumnoMateriaRepository extends JpaRepository<AlumnoMateria,Long> {

    List<AlumnoMateria> findByAlumno_IdPersona(Long idAlumno);
    boolean existsByAlumno_IdPersonaAndMateria_IdMateriaAndEstado(Long idAlumno, Long idMateria, EstadoMateria estadoMateria);
    Optional<AlumnoMateria> findByAlumno_IdPersonaAndMateria_IdMateriaAndEstado(Long idAlumno, Long idMateria,EstadoMateria estado);

}

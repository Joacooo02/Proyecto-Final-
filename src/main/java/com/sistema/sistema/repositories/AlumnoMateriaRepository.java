package com.sistema.sistema.repositories;

import com.sistema.sistema.entities.areaAcademica.AlumnoMateria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlumnoMateriaRepository extends JpaRepository<AlumnoMateria,Long> {

    List<AlumnoMateria> findByAlumno_IdPersona(Long idAlumno);
}

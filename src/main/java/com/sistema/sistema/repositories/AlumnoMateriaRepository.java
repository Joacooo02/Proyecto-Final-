package com.sistema.sistema.repositories;

import com.sistema.sistema.entities.areaAcademica.AlumnoMateria;
import com.sistema.sistema.entities.areaAcademica.Materia;
import com.sistema.sistema.entities.usuario.Alumno;
import com.sistema.sistema.enums.EstadoMateria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AlumnoMateriaRepository extends JpaRepository<AlumnoMateria,Long> {

    List<AlumnoMateria> findByAlumno_IdPersona(Long idAlumno);
    Optional<AlumnoMateria> findByAlumnoAndMateria(Alumno alumno, Materia materia);
    boolean existsByAlumnoAndMateria(Alumno alumno, Materia materia);
    boolean existsByAlumno_IdPersonaAndMateria_IdMateriaAndEstado(Long idAlumno, Long idMateria, EstadoMateria estadoMateria);

    boolean existsByAlumnoAndEstado(Alumno alumno, EstadoMateria estadoMateria);
    boolean existsByAlumno_IdPersonaAndEstado(Long idAlumno,EstadoMateria estadoMateria);

}

package com.sistema.sistema.repositories;

import com.sistema.sistema.entities.areaAdministrativa.AlumnoInscripcionMateria;
import com.sistema.sistema.enums.EstadoMateria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlumnoInscripcionMateriaRepository extends JpaRepository<AlumnoInscripcionMateria, Long> {
    boolean existsByAlumnoIdPersonaAndMateriaIdMateria(Long idAlumno, Long idMateria);

    boolean existsByAlumnoIdPersonaAndMateriaIdMateriaAndEstado(Long idAlumno, Long idMateria, EstadoMateria estadoMateria);

}

package com.sistema.sistema.repositories;

import com.sistema.sistema.entities.areaAdministrativa.AlumnoInscripcionComision;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlumnoInscripcionComisionRepository extends JpaRepository<AlumnoInscripcionComision, Long> {
        boolean existsByAlumno_IdPersonaAndComision_IdComision(Long idAlumno, Long idComision);
}

package com.sistema.sistema.repositories;

import com.sistema.sistema.entities.areaAdministrativa.AlumnoInscripcionComision;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlumnoInscripcionComisionRepository extends JpaRepository<AlumnoInscripcionComision, Long> {
        boolean existsByAlumnoIdPersonaAndComisionIdComision(Long idAlumno, Long idComision);
}

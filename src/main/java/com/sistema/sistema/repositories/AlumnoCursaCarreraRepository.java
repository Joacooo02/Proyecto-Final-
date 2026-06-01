package com.sistema.sistema.repositories;

import com.sistema.sistema.entities.areaAdministrativa.AlumnoCursaCarrera;
import com.sistema.sistema.entities.areaAdministrativa.AlumnoCursaCarreraId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AlumnoCursaCarreraRepository extends JpaRepository<AlumnoCursaCarrera, AlumnoCursaCarreraId> {
    Optional<AlumnoCursaCarrera> findByAlumnoIdPersona(Long idAlumno);
}

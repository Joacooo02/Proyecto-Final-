package com.sistema.sistema.repositories;

import com.sistema.sistema.entities.usuario.Alumno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AlumnoRepository extends JpaRepository<Alumno,Long>, JpaSpecificationExecutor<Alumno> {

    Optional<Alumno> findByLegajo(Long legajo);

    Optional<Alumno> findByDni(String dni);
}

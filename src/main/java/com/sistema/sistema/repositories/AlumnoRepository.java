package com.sistema.sistema.repositories;

import com.sistema.sistema.entities.usuario.Alumno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlumnoRepository extends JpaRepository<Alumno,Long>, JpaSpecificationExecutor<Alumno> {
    @Query("SELECT a FROM Alumno a JOIN a.comisiones c WHERE c.materia.id = :materiaId")
    List<Alumno> findAlumnosByMateriaId(@Param("materiaId") Long materiaId);
}

package com.sistema.sistema.repositories;

import com.sistema.sistema.entities.areaAcademica.Carrera;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarreraRepository extends JpaRepository<Carrera,Long> {
}

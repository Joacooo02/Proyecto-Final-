package com.sistema.sistema.repositories;

import com.sistema.sistema.entities.areaAcademica.Comision;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComisionRepository extends JpaRepository<Comision,Long> {
    List<Comision> findByNroComision(Integer nroComision);
}

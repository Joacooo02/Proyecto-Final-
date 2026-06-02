package com.sistema.sistema.repositories;

import com.sistema.sistema.entities.usuario.Profesor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfesorRepository extends JpaRepository<Profesor,Long>, JpaSpecificationExecutor<Profesor> {
}

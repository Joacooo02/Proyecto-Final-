package com.sistema.sistema.repositories;

import com.sistema.sistema.entities.areaAcademica.Nota;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotaRepository extends JpaRepository<Nota,Integer> {
}

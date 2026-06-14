package com.sistema.sistema.repositories;

import com.sistema.sistema.entities.ComisionHorario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComisionHorarioRepo extends JpaRepository<ComisionHorario,Long> {
}

package com.sistema.sistema.repositories;

import com.sistema.sistema.entities.usuario.Administrador;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdministradorRepository extends JpaRepository<Administrador,Integer> {
}

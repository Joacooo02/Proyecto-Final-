package com.sistema.sistema.repositories;

import com.sistema.sistema.dto.CorrelativaDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface CorrelativaRepo extends JpaRepository<CorrelativaDTO, Long> {
}

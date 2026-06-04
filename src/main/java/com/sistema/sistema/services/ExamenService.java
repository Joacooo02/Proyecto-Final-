package com.sistema.sistema.services;

import com.sistema.sistema.entities.areaAcademica.Examen;
import com.sistema.sistema.entities.areaAcademica.Materia;
import com.sistema.sistema.exceptions.MateriaInexistente;
import com.sistema.sistema.repositories.ExamenRepository;
import com.sistema.sistema.repositories.MateriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExamenService {

    private final ExamenRepository examenRepository;
    private final MateriaRepository materiaRepository;


    public Examen agregarExamen(Long idMateria, Examen examen)
    {
        Materia materia = materiaRepository.findById(idMateria).orElseThrow(() -> new MateriaInexistente("materia no encontrada"));
        examen.setMateria(materia);

        return examenRepository.save(examen);
    }
}

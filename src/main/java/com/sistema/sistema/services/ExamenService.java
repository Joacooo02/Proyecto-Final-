package com.sistema.sistema.services;

import com.sistema.sistema.Exceptions.ExamenInexistente;
import com.sistema.sistema.entities.areaAcademica.Examen;
import com.sistema.sistema.entities.areaAcademica.Materia;
import com.sistema.sistema.entities.enums.TipoExamen;
import com.sistema.sistema.exceptions.MateriaInexistente;
import com.sistema.sistema.repositories.ExamenRepository;
import com.sistema.sistema.repositories.MateriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.print.DocFlavor;
import java.time.LocalDate;
import java.util.List;

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

    public List<Examen> listarExamenes()
    {
        return examenRepository.findAll();
    }

    public Examen verExamen(Long idExamen)
    {
        return examenRepository.findById(idExamen).orElseThrow(() -> new ExamenInexistente("Examen no encontrado"));
    }

    public List<Examen> filtrarExamenPorTipo(TipoExamen tipoExamen)
    {
        return examenRepository.findByTipoExamen(tipoExamen);
    }

    public List<Examen> filtrarExamenPorMateria(String nombreMateria)
    {
        return examenRepository.findByMateriaNombre(nombreMateria);
    }

    public List<Examen> filtrarExamenPorFecha(LocalDate fechaExamen)
    {
        return examenRepository.findByFecha(fechaExamen);
    }


}

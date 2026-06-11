package com.sistema.sistema.services;

import com.sistema.sistema.entities.areaAcademica.Examen;
import com.sistema.sistema.entities.areaAcademica.Nota;
import com.sistema.sistema.dto.NotaDTO;
import com.sistema.sistema.entities.usuario.Alumno;
import com.sistema.sistema.exceptions.AlumnoInvalidoException;
import com.sistema.sistema.exceptions.ExamenInexistente;
import com.sistema.sistema.exceptions.NotaInvalidaException;

import com.sistema.sistema.mappers.NotaMapper;
import com.sistema.sistema.repositories.AlumnoRepository;
import com.sistema.sistema.repositories.ExamenRepository;
import com.sistema.sistema.repositories.NotaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;


@Service
@RequiredArgsConstructor
public class NotaService {
    private final NotaRepository notaRepo;

    private final AlumnoRepository AlumnoRepo;
    private final ExamenRepository ExamenRepo;
    private final NotaMapper notaMapper;

    public Nota registrarNota (NotaDTO notaDto){
         Alumno alm = AlumnoRepo.findById(notaDto.getIdAlumno()).orElseThrow(()-> new AlumnoInvalidoException("El alumno con id " + notaDto.getIdAlumno() + " no existe"));
         Examen exm = ExamenRepo.findById(notaDto.getIdExamn()).orElseThrow(()-> new ExamenInexistente("El examen con id " +notaDto.getIdExamn() + " no existe"));
         if(notaDto.getNota() < 0 || notaDto.getNota() > 10){
             throw new NotaInvalidaException("La nota debe estar entre 0 y 10");
         }

        Nota nota = new Nota();
        nota.setAlumno(alm);
        nota.setExamen(exm);
        nota.setNota(notaDto.getNota());
        nota.setFechaRegistro(LocalDate.now());

         notaRepo.save(nota);
         return nota;
    }
}

package com.sistema.sistema.services;

import com.sistema.sistema.entities.areaAcademica.Examen;
import com.sistema.sistema.entities.areaAcademica.Nota;
import com.sistema.sistema.entities.dto.NotaDto;
import com.sistema.sistema.entities.usuario.Alumno;
import com.sistema.sistema.exceptions.AlumnoInvalidoException;
import com.sistema.sistema.exceptions.ExamenInexistente;
import com.sistema.sistema.exceptions.NotaInvalidaException;
import com.sistema.sistema.repositories.AlumnoRepository;
import com.sistema.sistema.repositories.ExamenRepository;
import com.sistema.sistema.repositories.NotaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class NotaService {
    private final NotaRepository notaRepo;
    // Se elimina la inyección de NotaDto
    private final AlumnoRepository AlumnoRepo;
    private final ExamenRepository ExamenRepo;

    public NotaService(NotaRepository notaRepo, AlumnoRepository alumnoRepo, ExamenRepository examenRepo) {
        this.notaRepo = notaRepo;
        AlumnoRepo = alumnoRepo;
        ExamenRepo = examenRepo;
    }

    public Nota registrarNota (NotaDto notaDto){
         Alumno alm = AlumnoRepo.findById(notaDto.getIdAlumno()).orElseThrow(()-> new AlumnoInvalidoException("El alumno con id " + notaDto.getIdAlumno() + " no existe"));
         Examen exm = ExamenRepo.findById(notaDto.getIdExamn()).orElseThrow(()-> new ExamenInexistente("El examen con id " +notaDto.getIdExamn() + " no existe"));
         if(notaDto.getNota() < 0 || notaDto.getNota() > 10){
             throw new NotaInvalidaException("La nota debe estar entre 0 y 10");
         }

         Nota nota = Nota.builder()
                 .alumno(alm)
                 .examen(exm)
                 .nota(notaDto.getNota())
                 .fechaRegistro(LocalDate.now())
                 .build();

         notaRepo.save(nota);
         return nota;
    }
}

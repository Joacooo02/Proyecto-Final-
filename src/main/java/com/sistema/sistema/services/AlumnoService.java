package com.sistema.sistema.services;

import com.sistema.sistema.entities.usuario.Alumno;
import com.sistema.sistema.exceptions.EntidadNoEncontradaException;
import com.sistema.sistema.repositories.AlumnoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AlumnoService {

    private final AlumnoRepository alumnoRepository;

    public Alumno buscarAlumnoPorId(Long id){
       return alumnoRepository.findById(id)
                .orElseThrow(()-> new EntidadNoEncontradaException("Alumno con id: " +id+ " no encontrado"));
    }

    public Alumno agregarAlumno(Alumno alumno){
        return alumnoRepository.save(alumno);
    }

    public void eliminarAlumno(Long id){
        buscarAlumnoPorId(id);
        alumnoRepository.deleteById(id);
    }

    public List<Alumno> listarAlumnos(){
        return alumnoRepository.findAll();
    }

    public Alumno modificarAlumno(Long id, Alumno alumnoModificado) {

        Alumno alumnoExistente = buscarAlumnoPorId(id);

        alumnoExistente.setNombre(alumnoModificado.getNombre());
        alumnoExistente.setApellido(alumnoModificado.getApellido());
        alumnoExistente.setDni(alumnoModificado.getDni());
        alumnoExistente.setTelefono(alumnoModificado.getTelefono());
        alumnoExistente.setFechaNacimiento(alumnoModificado.getFechaNacimiento());
        alumnoExistente.setEmail(alumnoModificado.getEmail());

        alumnoExistente.setLegajo(alumnoModificado.getLegajo());
        alumnoExistente.setAnioIngreso(alumnoModificado.getAnioIngreso());
        alumnoExistente.setAnaliticoParcial(alumnoModificado.isAnaliticoParcial());
        alumnoExistente.setEsRegular(alumnoModificado.isEsRegular());
        alumnoExistente.setPlanEstudio(alumnoModificado.getPlanEstudio());
        alumnoExistente.setPromedio(alumnoModificado.getPromedio());

        return alumnoRepository.save(alumnoExistente);
    }

}

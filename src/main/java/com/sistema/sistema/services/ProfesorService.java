package com.sistema.sistema.services;

import com.sistema.sistema.entities.usuario.Profesor;
import com.sistema.sistema.exceptions.EntidadNoEncontradaException;
import com.sistema.sistema.repositories.ProfesorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProfesorService {

    private final ProfesorRepository profesorRepository;

    public Profesor buscarProfesorPorId(Long id) {
        return profesorRepository.findById(id)
                .orElseThrow(() -> new EntidadNoEncontradaException("Profesor con id: " + id + " no encontrado"));
    }

    public Profesor agregarProfesor(Profesor profesor) {
        return profesorRepository.save(profesor);
    }

    public void eliminarProfesor(Long id) {
        buscarProfesorPorId(id);
        profesorRepository.deleteById(id);
    }

    public List<Profesor> listarProfesores() {
        return profesorRepository.findAll();
    }

    public Profesor modificarProfesor(Long id, Profesor profesorModificado) {
        Profesor profesorExistente = buscarProfesorPorId(id);

        profesorExistente.setNombre(profesorModificado.getNombre());
        profesorExistente.setApellido(profesorModificado.getApellido());
        profesorExistente.setDni(profesorModificado.getDni());
        profesorExistente.setTelefono(profesorModificado.getTelefono());
        profesorExistente.setFechaNacimiento(profesorModificado.getFechaNacimiento());
        profesorExistente.setEmail(profesorModificado.getEmail());

        profesorExistente.setHorasSemanales(profesorModificado.getHorasSemanales());
        profesorExistente.setEstadoProfesor(profesorModificado.getEstadoProfesor());

        return profesorRepository.save(profesorExistente);
    }
}


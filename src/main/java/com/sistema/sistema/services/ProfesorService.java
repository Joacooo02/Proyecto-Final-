package com.sistema.sistema.services;

import com.sistema.sistema.entities.enums.EstadoProfesor;
import com.sistema.sistema.entities.usuario.Profesor;
import com.sistema.sistema.exceptions.EntidadNoEncontradaException;
import com.sistema.sistema.repositories.ProfesorRepository;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    public List<Profesor> listarProfesores(String nombre, String apellido, String dni, String email, EstadoProfesor estado) {
        return profesorRepository.findAll((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (nombre != null && !nombre.isBlank()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("nombre")), "%" + nombre.toLowerCase() + "%"));
            }
            if (apellido != null && !apellido.isBlank()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("apellido")), "%" + apellido.toLowerCase() + "%"));
            }
            if (dni != null && !dni.isBlank()) {
                predicates.add(criteriaBuilder.equal(root.get("dni"), dni));
            }
            if (email != null && !email.isBlank()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("email")), "%" + email.toLowerCase() + "%"));
            }
            if (estado != null) {
                predicates.add(criteriaBuilder.equal(root.get("estadoProfesor"), estado));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });
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


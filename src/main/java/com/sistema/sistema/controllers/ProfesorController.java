package com.sistema.sistema.controllers;

import com.sistema.sistema.dto.AlumnoDTO;
import com.sistema.sistema.dto.ComisionDTO;
import com.sistema.sistema.dto.MateriaDTO;
import com.sistema.sistema.enums.EstadoProfesor;
import com.sistema.sistema.entities.usuario.Profesor;
import com.sistema.sistema.services.ProfesorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/profesores")
@RequiredArgsConstructor
public class ProfesorController {

    private final ProfesorService profesorService;

    @GetMapping("/{id}")
    public Profesor buscarProfesorPorId(@PathVariable Long id) {
        return profesorService.buscarProfesorPorId(id);
    }

    @PostMapping
    public Profesor agregarProfesor(@RequestBody Profesor profesor) {
        return profesorService.agregarProfesor(profesor);
    }

    @DeleteMapping("/{id}")
    public void eliminarProfesor(@PathVariable Long id) {
        profesorService.eliminarProfesor(id);
    }

    @GetMapping
    public List<Profesor> listarProfesores(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String apellido,
            @RequestParam(required = false) String dni,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) EstadoProfesor estado) {


        return profesorService.listarProfesores(nombre, apellido, dni, email, estado);
    }

    @PutMapping("/{id}")
    public Profesor modificarProfesor(@PathVariable Long id, @RequestBody Profesor profesorModificado) {
        return profesorService.modificarProfesor(id, profesorModificado);
    }

    @GetMapping("/{id}/comisiones")
    public ResponseEntity<List<ComisionDTO>> listarComisiones(@PathVariable Long profesorId) {
        List<ComisionDTO> comisiones = profesorService.obtenerComisionesProfesor(profesorId);
        return ResponseEntity.ok(comisiones);
    }

    @GetMapping("/{id}/materias")
    public ResponseEntity<List<MateriaDTO>> listarMaterias(@PathVariable Long profesorId) {
        List<MateriaDTO> materias = profesorService.obtenerMateriasProfesor(profesorId);
        return ResponseEntity.ok(materias);
    }

    @GetMapping("/{materiaId}/alumnos")
    public ResponseEntity<List<AlumnoDTO>> obtenerAlumnosMateria(@PathVariable Long materiaId) {
        List<AlumnoDTO> alumnos = profesorService.obtenerAlumnosMateria(materiaId);
        return ResponseEntity.ok(alumnos);
    }
}

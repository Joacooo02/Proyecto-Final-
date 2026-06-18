package com.sistema.sistema.controllers;

import com.sistema.sistema.dto.AlumnoDTO;
import com.sistema.sistema.dto.ComisionDTO;
import com.sistema.sistema.dto.MateriaDTO;
import com.sistema.sistema.enums.EstadoProfesor;
import com.sistema.sistema.entities.usuario.Profesor;
import com.sistema.sistema.services.ProfesorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/profesores")
@RequiredArgsConstructor
public class ProfesorController {

    private final ProfesorService profesorService;

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("/{id}")
    public Profesor buscarProfesorPorId(@PathVariable Long id) {
        return profesorService.buscarProfesorPorId(id);
    }

    // Datos del profesor logueado (email tomado del token). Permite que el login del
    // front pida SOLO email+contraseña: tras autenticar, llama /me y obtiene su id
    // (idPersona). La ruta literal "/me" matchea antes que "/{id}".
    @PreAuthorize("hasAnyRole('PROFESOR', 'ADMIN')")
    @GetMapping("/me")
    public Profesor datosProfesorLogueado(org.springframework.security.core.Authentication authentication) {
        return profesorService.buscarProfesorPorEmail(authentication.getName());
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping
    public Profesor agregarProfesor(@RequestBody Profesor profesor) {
        return profesorService.agregarProfesor(profesor);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void eliminarProfesor(@PathVariable Long id) {
        profesorService.eliminarProfesor(id);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping
    public List<Profesor> listarProfesores(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String apellido,
            @RequestParam(required = false) String dni,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) EstadoProfesor estado) {


        return profesorService.listarProfesores(nombre, apellido, dni, email, estado);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PutMapping("/{id}")
    public Profesor modificarProfesor(@PathVariable Long id, @RequestBody Profesor profesorModificado) {
        return profesorService.modificarProfesor(id, profesorModificado);
    }

    @PreAuthorize("hasAnyRole('PROFESOR', 'ADMIN')")
    @GetMapping("/{id}/comisiones")
    public ResponseEntity<List<ComisionDTO>> listarComisiones(@PathVariable Long id) {
        List<ComisionDTO> comisiones = profesorService.obtenerComisionesProfesor(id);
        return ResponseEntity.ok(comisiones);
    }
    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("/{id}/materias")
    public ResponseEntity<List<MateriaDTO>> listarMaterias(@PathVariable Long id) {
        List<MateriaDTO> materias = profesorService.obtenerMateriasProfesor(id);
        return ResponseEntity.ok(materias);
    }

    @PreAuthorize("hasAnyRole('PROFESOR', 'ADMIN')")
    @GetMapping("/{materiaId}/alumnos")
    public ResponseEntity<List<AlumnoDTO>> obtenerAlumnosMateria(@PathVariable Long materiaId) {
        List<AlumnoDTO> alumnos = profesorService.obtenerAlumnosMateria(materiaId);
        return ResponseEntity.ok(alumnos);
    }

    @PreAuthorize("hasAnyRole('PROFESOR', 'ADMIN')")
    @GetMapping("/{id}/comisiones/{comisionId}/alumnos")
    public ResponseEntity<List<AlumnoDTO>> obtenerAlumnosInscriptosComision(@PathVariable Long id, @PathVariable Long comisionId) {
        List<AlumnoDTO> alumnos = profesorService.obtenerAlumnosInscriptosComision(id, comisionId);
        return ResponseEntity.ok(alumnos);
    }

    @PreAuthorize("hasAnyRole('PROFESOR', 'ADMIN')")
    @GetMapping("/{id}/examenes/{examenId}/alumnos")
    public ResponseEntity<List<AlumnoDTO>> obtenerAlumnosInscriptosExamenFinal(@PathVariable Long id, @PathVariable Long examenId) {
        List<AlumnoDTO> alumnos = profesorService.obtenerAlumnosInscriptosExamenFinal(id, examenId);
        return ResponseEntity.ok(alumnos);
    }

    @PreAuthorize("hasAnyRole('PROFESOR', 'ADMIN')")
    @GetMapping("/{id}/materias/{materiaId}/alumnos")
    public ResponseEntity<List<AlumnoDTO>> obtenerAlumnosInscriptosMateria(@PathVariable Long id, @PathVariable Long materiaId) {
        List<AlumnoDTO> alumnos = profesorService.obtenerAlumnosInscriptosMateria(id, materiaId);
        return ResponseEntity.ok(alumnos);
    }
}

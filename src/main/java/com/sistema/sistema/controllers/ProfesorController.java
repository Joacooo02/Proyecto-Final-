package com.sistema.sistema.controllers;

import com.sistema.sistema.entities.usuario.Profesor;
import com.sistema.sistema.services.ProfesorService;
import lombok.RequiredArgsConstructor;
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
    public List<Profesor> listarProfesores() {
        return profesorService.listarProfesores();
    }

    @PutMapping("/{id}")
    public Profesor modificarProfesor(@PathVariable Long id, @RequestBody Profesor profesorModificado) {
        return profesorService.modificarProfesor(id, profesorModificado);
    }
}

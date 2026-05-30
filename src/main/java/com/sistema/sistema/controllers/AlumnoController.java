package com.sistema.sistema.controllers;

import com.sistema.sistema.entities.usuario.Alumno;
import com.sistema.sistema.services.AlumnoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/alumnos")
@RequiredArgsConstructor
public class AlumnoController {

    private final AlumnoService alumnoService;

    @GetMapping("/{id}")
    public Alumno buscarAlumnoPorId(@PathVariable Long id){
        return alumnoService.buscarAlumnoPorId(id);
    }

    @PostMapping
    public Alumno agregarAlumno(@RequestBody Alumno alumno){
        return alumnoService.agregarAlumno(alumno);
    }

    @DeleteMapping("/{id}")
    public void eliminarAlumno(@PathVariable Long id){
        alumnoService.eliminarAlumno(id);
    }

    @GetMapping
    public List<Alumno> listarAlumnos(){
        return alumnoService.listarAlumnos();
    }

    @PutMapping
    public Alumno modificarAlumno(@PathVariable Long id,@RequestBody Alumno alumnoModificado){
        return alumnoService.modificarAlumno(id,alumnoModificado);
    }

}

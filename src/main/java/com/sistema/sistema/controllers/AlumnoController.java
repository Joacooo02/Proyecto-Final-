package com.sistema.sistema.controllers;

<<<<<<< HEAD
import com.sistema.sistema.entities.areaAcademica.Materia;
=======
import com.sistema.sistema.entities.dto.HistorialAcademicoDTO;
>>>>>>> 8caa415bc9c1174405bd084ea7221e6ff65faf5b
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
    public List<Alumno> listarAlumnos(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String apellido,
            @RequestParam(required = false) String dni,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) Long legajo) {

        return alumnoService.listarAlumnos(nombre, apellido, dni, email, legajo);
    }

    @PutMapping("/{id}")
    public Alumno modificarAlumno(@PathVariable Long id,@RequestBody Alumno alumnoModificado){
        return alumnoService.modificarAlumno(id,alumnoModificado);
    }
<<<<<<< HEAD
=======

    @GetMapping("/{idAlumno}/historial-academico")
    public List<HistorialAcademicoDTO> verHistorialAcademicoAlumno(@PathVariable Long idAlumno)
    {
        return alumnoService.verHistorialAcademicoAlumno(idAlumno);
    }

>>>>>>> 8caa415bc9c1174405bd084ea7221e6ff65faf5b
}

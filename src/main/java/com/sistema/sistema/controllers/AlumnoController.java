package com.sistema.sistema.controllers;

import com.sistema.sistema.dto.AlumnoDTO;
import com.sistema.sistema.dto.HistorialAcademicoDTO;
import com.sistema.sistema.dto.MateriaDTO;
import com.sistema.sistema.services.AlumnoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/alumnos")
@RequiredArgsConstructor
public class AlumnoController {

    private final AlumnoService alumnoService;

    @GetMapping("/{legajo}")
    public AlumnoDTO buscarAlumnoPorLegajo(@PathVariable Long legajo){
        return alumnoService.buscarAlumnoPorLegajo(legajo);
    }

    @PostMapping
    public AlumnoDTO agregarAlumno(@RequestBody AlumnoDTO alumno){
        return alumnoService.agregarAlumno(alumno);
    }

    @DeleteMapping("/{legajo}")
    public void eliminarAlumno(@PathVariable Long legajo){
        alumnoService.eliminarAlumno(legajo);
    }

    @GetMapping
    public List<AlumnoDTO> listarAlumnos(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String apellido,
            @RequestParam(required = false) String dni,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) Long legajo) {

        return alumnoService.listarAlumnos(nombre, apellido, dni, email, legajo);
    }

    @PutMapping("/{legajo}")
    public AlumnoDTO modificarAlumno(@PathVariable Long legajo,@RequestBody AlumnoDTO alumnoModificado){
        return alumnoService.modificarAlumno(legajo,alumnoModificado);
    }

    @GetMapping("/{legajo}/historial-academico")
    public List<HistorialAcademicoDTO> verHistorialAcademicoAlumno(@PathVariable Long legajo)
    {
        return alumnoService.verHistorialAcademicoAlumno(legajo);
    }

    @GetMapping("/{legajo}/materias")
    public List<MateriaDTO> obtenerMaterias(@PathVariable Long legajo)
    {
        return alumnoService.obtenerMaterias(legajo);
    }

}
package com.sistema.sistema.controllers;

import com.sistema.sistema.entities.areaAcademica.Materia;
import com.sistema.sistema.services.MateriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/materias")
@RequiredArgsConstructor
public class MateriaController {

    private final MateriaService materiaService;

    @GetMapping("/{id}")
    public Materia buscarMateriaPorId(@PathVariable Long id) {
        return materiaService.buscarMateriaPorId(id);
    }

    @PostMapping
    public Materia agregarMateria(@RequestBody Materia materia) {
        return materiaService.agregarMateria(materia);
    }

    @DeleteMapping("/{id}")
    public void eliminarMateria(@PathVariable Long id) {
        materiaService.eliminarMateria(id);
    }

    @GetMapping
    public List<Materia> listarMaterias() {
        return materiaService.listarMaterias();
    }

    @PutMapping("/{id}")
    public Materia modificarMateria(@PathVariable Long id, @RequestBody Materia materiaModificada) {
        return materiaService.modificarMateria(id, materiaModificada);
    }

    @GetMapping("/plan-academico/{idAlumno}")
    public List<Materia> verPlanAcademicoAlumno(@PathVariable Long idAlumno)
    {
        return materiaService.verPlanAcademicoAlumno(idAlumno);
    }
}

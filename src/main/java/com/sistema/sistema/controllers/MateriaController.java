package com.sistema.sistema.controllers;

import com.sistema.sistema.dto.MateriaDTO;
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
    public MateriaDTO buscarMateriaPorId(@PathVariable Long id)
    {
        return materiaService.buscarMateriaPorId(id);
    }

    @PostMapping
    public MateriaDTO agregarMateria(@RequestBody MateriaDTO materiaDTO)
    {
        return materiaService.agregarMateria(materiaDTO);
    }

    @DeleteMapping("/{id}")
    public void eliminarMateria(@PathVariable Long id) {
        materiaService.eliminarMateria(id);
    }

    @GetMapping
    public List<MateriaDTO> listarMaterias(@RequestParam(required = false) String nombre) {
        return materiaService.listarMaterias(nombre);
    }

    @PutMapping("/{id}")
    public MateriaDTO modificarMateria(@PathVariable Long id, @RequestBody MateriaDTO materiaDTO)
    {
        return materiaService.modificarMateria(id, materiaDTO);
    }

    @GetMapping("/plan-academico/{idAlumno}")
    public List<MateriaDTO> verPlanAcademicoAlumno(
            @PathVariable Long idAlumno)
    {
        return materiaService.verPlanAcademicoAlumno(idAlumno)
                .stream()
                .map(m -> MateriaDTO.builder()
                        .id(m.getIdMateria())
                        .idCarrera(m.getCarrera().getIdCarrera())
                        .nombre(m.getNombre())
                        .cargaHoraria(m.getCargaHoraria())
                        .cuatrimestre(m.getCuatrimestre())
                        .anioCursado(m.getAnioCursado())
                        .build())
                .toList();
    }

}

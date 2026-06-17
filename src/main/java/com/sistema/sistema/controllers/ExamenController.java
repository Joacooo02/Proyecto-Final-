package com.sistema.sistema.controllers;

import com.sistema.sistema.entities.areaAcademica.Examen;
import com.sistema.sistema.enums.TipoExamen;
import com.sistema.sistema.services.ExamenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/examenes")
@RequiredArgsConstructor
public class  ExamenController {

    private final ExamenService examenService;

    @PreAuthorize("hasAnyRole('PROFESOR', 'ADMIN')")
    @PostMapping("/materia/{idMateria}")
    public Examen agregarExamen(@PathVariable Long idMateria, @RequestBody Examen examen) {
        return examenService.agregarExamen(idMateria, examen);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping
    public List<Examen> listarExamenes() {
        return examenService.listarExamenes();
    }

    @PreAuthorize("hasAnyRole('PROFESOR', 'ADMIN')")
    @GetMapping("/{idExamen}")
    public Examen verExamen(@PathVariable Long idExamen) {
        return examenService.verExamen(idExamen);
    }

    @PreAuthorize("hasAnyRole('PROFESOR', 'ADMIN')")
    @GetMapping("/tipo/{tipoExamen}")
    public List<Examen> filtrarExamenPorTipo(@PathVariable TipoExamen tipoExamen) {
        return examenService.filtrarExamenPorTipo(tipoExamen);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("/materia/{nombreMateria}")
    public List<Examen> filtrarExamenPorIdMateria(@PathVariable String nombreMateria)
    {
        return examenService.filtrarExamenPorMateria(nombreMateria);
    }

    @PreAuthorize("hasAnyRole('PROFESOR', 'ADMIN')")
    @GetMapping("/fecha/{fecha}")
    public List<Examen> filtrarExamenPorFecha(@PathVariable LocalDate fecha)
    {
        return examenService.filtrarExamenPorFecha(fecha);
    }
}

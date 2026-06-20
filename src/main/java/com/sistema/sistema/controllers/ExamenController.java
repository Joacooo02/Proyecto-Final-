package com.sistema.sistema.controllers;

import com.sistema.sistema.entities.areaAcademica.Examen;
import com.sistema.sistema.enums.TipoExamen;
import com.sistema.sistema.services.ExamenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/examenes")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
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

    @PreAuthorize("hasAnyRole('PROFESOR', 'ADMIN', 'ALUMNO')")
    @GetMapping("/tipo/{tipoExamen}")
    public List<Examen> filtrarExamenPorTipo(@PathVariable String tipoExamen) {
        TipoExamen tipo = TipoExamen.valueOf(tipoExamen.toUpperCase());
        return examenService.filtrarExamenPorTipo(tipo);
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

    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarExamen(@PathVariable Long id)
    {
        examenService.eliminarExamen(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Examen> modificarExamen(@PathVariable Long id,@RequestParam(required = false) Long idMateria,@RequestBody Examen examen)
    {
        Examen actualizado = examenService.modificarExamen(id, idMateria, examen);
        return ResponseEntity.ok(actualizado);
    }
}

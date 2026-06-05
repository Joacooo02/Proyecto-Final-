package com.sistema.sistema.controllers;

import com.sistema.sistema.entities.areaAdministrativa.AlumnoInscripcionComision;
import com.sistema.sistema.entities.areaAdministrativa.AlumnoInscripcionMateria;
import com.sistema.sistema.services.InscripcionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/inscripcion")
public class InscripcionController {

    @Autowired
    private InscripcionService service;

    @PostMapping("/materia/{idAlumno}/{idMateria}")
    public AlumnoInscripcionMateria inscripcionMateria(@PathVariable Long idAlumno, @PathVariable Long idMateria)
    {
        return service.inscribirMateria(idAlumno, idMateria);
    }

    @PostMapping("/comision/{idAlumno}/{idComision}")
    public AlumnoInscripcionComision inscribirComision(@PathVariable Long idAlumno, @PathVariable Long idComision)
    {
        return service.inscribirComision(idAlumno, idComision);
    }

}

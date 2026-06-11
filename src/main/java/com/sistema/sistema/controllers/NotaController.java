package com.sistema.sistema.controllers;

import com.sistema.sistema.entities.areaAcademica.Nota;
import com.sistema.sistema.dto.NotaDto;
import com.sistema.sistema.services.NotaService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notas")
@RequiredArgsConstructor

public class NotaController {
    private final NotaService serv;

    @PostMapping
    public Nota registrarNota(@RequestBody NotaDto dto){
        return serv.registrarNota(dto);
    }


}

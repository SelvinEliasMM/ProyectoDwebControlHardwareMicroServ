package com.erp.hardware.controladores;

import com.erp.hardware.modelos.Equipos;
import com.erp.hardware.repositorios.EquiposRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/equipos")
@CrossOrigin(origins = "*")
public class EquipoController {

    @Autowired
    private EquiposRepository equiposRepository;

    @GetMapping
    public List<Equipos> listarEquipos() {
        return equiposRepository.findAll();
    }

    @PostMapping
    public Equipos guardarEquipo(@RequestBody Equipos equipo) {
        return equiposRepository.save(equipo);
    }
}

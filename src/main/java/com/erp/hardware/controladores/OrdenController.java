package com.erp.hardware.controladores;

import com.erp.hardware.modelos.OrdenesTrabajo;
import com.erp.hardware.repositorios.OrdenesTrabajoRepository;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController 
@RequestMapping("/api/ordenes")
@CrossOrigin(origins = "*")
public class OrdenController {

    @Autowired
    private OrdenesTrabajoRepository ordenesRepository;

    @GetMapping
    public List<OrdenesTrabajo> listarOrdenes() {
        return ordenesRepository.findAll();
    }
    @PostMapping
    public OrdenesTrabajo guardarOrden(@RequestBody OrdenesTrabajo orden) {
        if (orden.getEstadoReparacion() == null) {
            orden.setEstadoReparacion("En Diagnóstico");
        }
        if (orden.getFechaIngreso() == null) {
            orden.setFechaIngreso(new Date());
        }
        return ordenesRepository.save(orden);
    }
    
    
    @PutMapping("/{id}/estado")
    public OrdenesTrabajo actualizarEstado(@PathVariable Integer id, @RequestBody java.util.Map<String, String> body) {
        // Busca la orden, le cambia el estado y la guarda
        OrdenesTrabajo orden = ordenesRepository.findById(id).get();
        orden.setEstadoReparacion(body.get("estado"));
        return ordenesRepository.save(orden);
    }
}
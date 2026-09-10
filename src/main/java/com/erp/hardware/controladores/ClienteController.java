package com.erp.hardware.controladores;

import com.erp.hardware.modelos.Clientes;
import com.erp.hardware.repositorios.ClientesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/clientes")
@CrossOrigin(origins = "*")
public class ClienteController {

    @Autowired
    private ClientesRepository clientesRepository;

    @GetMapping
    public List<Clientes> listarClientes() {
        return clientesRepository.findAll();
    }

    @PostMapping
    public Clientes guardarCliente(@RequestBody Clientes cliente) {
        return clientesRepository.save(cliente);
    }
}
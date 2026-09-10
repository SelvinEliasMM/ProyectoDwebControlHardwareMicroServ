package com.erp.hardware.controladores;

import com.erp.hardware.modelos.Usuarios; 
import com.erp.hardware.repositorios.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping
    public List<Usuarios> listarUsuarios() {
        return usuarioRepository.findAll();
    }
    
    @PostMapping("/login")
    public Usuarios validarLogin(@RequestBody Usuarios credenciales) {
        Usuarios usuario = usuarioRepository.findByCorreoAndPasswordHash(
            credenciales.getCorreo(), 
            credenciales.getPasswordHash()
        );
        
        if (usuario != null) {
            return usuario; // Si existe, devuelve los datos
        } else {
            throw new RuntimeException("Credenciales incorrectas"); // Falla si no coincide
        }
    }
    
    
}
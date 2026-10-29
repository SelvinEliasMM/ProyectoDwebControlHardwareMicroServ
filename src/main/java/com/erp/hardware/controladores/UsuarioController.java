package com.erp.hardware.controladores;

import com.erp.hardware.modelos.BitacoraAuditoria;
import com.erp.hardware.modelos.Usuarios;
import com.erp.hardware.repositorios.BitacoraAuditoriaRepository;
import com.erp.hardware.repositorios.UsuarioRepository;
import com.erp.hardware.seguridad.JwtUtil;
import jakarta.annotation.PostConstruct;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private BitacoraAuditoriaRepository bitacoraRepository;

    @PostConstruct
    public void migrarContrasenasTextoPlano() {
        try {
            List<Usuarios> usuarios = usuarioRepository.findAll();
            for (Usuarios u : usuarios) {
                String pass = u.getPasswordHash();
                if (pass != null && !pass.startsWith("$2a$") && !pass.startsWith("$2b$") && !pass.startsWith("$2y$")) {
                    u.setPasswordHash(passwordEncoder.encode(pass));
                    usuarioRepository.save(u);
                }
            }
        } catch (Exception e) {
          
        }
    }

    // Listar usuarios 
    @GetMapping
    public List<Map<String, Object>> listarUsuarios() {
        return usuarioRepository.findAll().stream().map(u -> {
            Map<String, Object> dto = new LinkedHashMap<>();
            dto.put("idUsuario", u.getIdUsuario());
            dto.put("nombreCompleto", u.getNombreCompleto());
            dto.put("correo", u.getCorreo());
            dto.put("rol", u.getRol());
            return dto;
        }).collect(Collectors.toList());
    }

    // Endpoint de login con BCrypt + JWT
    @PostMapping("/login")
    public ResponseEntity<?> validarLogin(@RequestBody Map<String, String> credenciales) {
        String correo = credenciales.get("correo");
        String password = credenciales.get("passwordHash");
        if (password == null) {
            password = credenciales.get("password");
        }

        if (correo == null || password == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("mensaje", "Correo y contraseña son obligatorios"));
        }

        Optional<Usuarios> optUsuario = usuarioRepository.findByCorreo(correo.trim());
        if (optUsuario.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("mensaje", "Credenciales incorrectas"));
        }

        Usuarios usuario = optUsuario.get();
        boolean passValida = false;

        // Comprobación BCrypt
        if (usuario.getPasswordHash().startsWith("$2a$") || usuario.getPasswordHash().startsWith("$2b$") || usuario.getPasswordHash().startsWith("$2y$")) {
            passValida = passwordEncoder.matches(password, usuario.getPasswordHash());
        } else {
            // si estaba en texto plano comparar y actualizar inmediatamente a BCrypt
            if (password.equals(usuario.getPasswordHash())) {
                passValida = true;
                usuario.setPasswordHash(passwordEncoder.encode(password));
                usuarioRepository.save(usuario);
            }
        }

        if (!passValida) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("mensaje", "Credenciales incorrectas"));
        }

        // Generar Token JWT firmado
        String token = jwtUtil.generarToken(
                usuario.getIdUsuario(),
                usuario.getCorreo(),
                usuario.getNombreCompleto(),
                usuario.getRol()
        );

        Map<String, Object> respuesta = new LinkedHashMap<>();
        respuesta.put("token", token);
        respuesta.put("idUsuario", usuario.getIdUsuario());
        respuesta.put("nombreCompleto", usuario.getNombreCompleto());
        respuesta.put("correo", usuario.getCorreo());
        respuesta.put("rol", usuario.getRol());

        return ResponseEntity.ok(respuesta);
    }

    // Crear nuevo usuario con bcryp
    @PostMapping
    public ResponseEntity<?> crearUsuario(@RequestBody Usuarios nuevo) {
        if (nuevo.getCorreo() == null || nuevo.getPasswordHash() == null || nuevo.getNombreCompleto() == null || nuevo.getRol() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("mensaje", "Todos los campos son obligatorios"));
        }

        if (usuarioRepository.findByCorreo(nuevo.getCorreo().trim()).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("mensaje", "El correo ya se encuentra registrado"));
        }

        nuevo.setCorreo(nuevo.getCorreo().trim());
        nuevo.setPasswordHash(passwordEncoder.encode(nuevo.getPasswordHash()));
        Usuarios guardado = usuarioRepository.save(nuevo);

        // registro en bitacora
        try {
            BitacoraAuditoria bit = new BitacoraAuditoria();
            bit.setTablaAfectada("USUARIOS");
            bit.setAccionRealizada("INSERT");
            bit.setFechaHora(new Date());
            bit.setIdUsuario(new Usuarios(1));
            bit.setDatosOriginales("Nuevo registro");
            bit.setDatosNuevos("{\"id_usuario\":" + guardado.getIdUsuario() + ",\"nombre\":\"" + guardado.getNombreCompleto() + "\",\"rol\":\"" + guardado.getRol() + "\"}");
            bitacoraRepository.save(bit);
        } catch (Exception ex) {}

        Map<String, Object> respuesta = new LinkedHashMap<>();
        respuesta.put("idUsuario", guardado.getIdUsuario());
        respuesta.put("nombreCompleto", guardado.getNombreCompleto());
        respuesta.put("correo", guardado.getCorreo());
        respuesta.put("rol", guardado.getRol());

        return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
    }
}
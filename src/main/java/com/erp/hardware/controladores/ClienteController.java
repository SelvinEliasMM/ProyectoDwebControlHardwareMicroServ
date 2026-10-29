package com.erp.hardware.controladores;

import com.erp.hardware.modelos.BitacoraAuditoria;
import com.erp.hardware.modelos.Clientes;
import com.erp.hardware.modelos.Usuarios;
import com.erp.hardware.repositorios.BitacoraAuditoriaRepository;
import com.erp.hardware.repositorios.ClientesRepository;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/clientes")
@CrossOrigin(origins = "*")
public class ClienteController {

    @Autowired
    private ClientesRepository clientesRepository;

    @Autowired
    private BitacoraAuditoriaRepository bitacoraRepository;

    @GetMapping
    public List<Clientes> listarClientes() {
        return clientesRepository.findByEstadoRegistroTrue();
    }

    // CRUD de clientes con validación de obligatorios y no permitir correos duplicados
    @PostMapping
    public ResponseEntity<?> guardarCliente(@RequestBody Clientes cliente) {
        try {
            if (cliente.getNombreCliente() == null || cliente.getNombreCliente().trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("mensaje", "El nombre del cliente es obligatorio"));
            }

            if (cliente.getEmail() != null && !cliente.getEmail().trim().isEmpty()) {
                String emailNormalizado = cliente.getEmail().trim().toLowerCase();
                if (clientesRepository.findByEmail(emailNormalizado).isPresent()) {
                    return ResponseEntity.status(HttpStatus.CONFLICT)
                            .body(Map.of("mensaje", "El correo ya está registrado para otro cliente"));
                }
                cliente.setEmail(emailNormalizado);
            }

            cliente.setEstadoRegistro(true);
            cliente.setFechaCreacion(new Date());

            Clientes guardado = clientesRepository.save(cliente);

            // registro en bitacora
            try {
                BitacoraAuditoria bit = new BitacoraAuditoria();
                bit.setTablaAfectada("CLIENTES");
                bit.setAccionRealizada("INSERT");
                bit.setFechaHora(new Date());
                bit.setIdUsuario(guardado.getUsuarioCreacion() != null ? guardado.getUsuarioCreacion() : new Usuarios(1));
                bit.setDatosOriginales("Nuevo cliente");
                bit.setDatosNuevos("{\"id_cliente\":" + guardado.getIdCliente() + ",\"nombre\":\"" + guardado.getNombreCliente() + "\",\"email\":\"" + guardado.getEmail() + "\"}");
                bitacoraRepository.save(bit);
            } catch (Exception ex) {}

            return ResponseEntity.status(HttpStatus.CREATED).body(guardado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("mensaje", e.getMessage()));
        }
    }
}
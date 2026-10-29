package com.erp.hardware.controladores;

import com.erp.hardware.modelos.Facturas;
import com.erp.hardware.servicios.FacturaService;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/facturas")
@CrossOrigin(origins = "*")
public class FacturaController {

    @Autowired
    private FacturaService facturaService;

    @GetMapping
    public List<Facturas> listarFacturas() {
        return facturaService.listarFacturas();
    }

    @GetMapping("/{id}")
    public Facturas obtenerPorId(@PathVariable Integer id) {
        return facturaService.obtenerPorId(id);
    }

    @GetMapping("/orden/{idOrden}")
    public ResponseEntity<?> obtenerPorOrden(@PathVariable Integer idOrden) {
        return facturaService.obtenerPorOrden(idOrden)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // generar factura con desglose
    @PostMapping
    public ResponseEntity<?> generarFactura(@RequestBody Map<String, Object> body) {
        try {
            Integer idOrden = Integer.valueOf(body.get("idOrden").toString());
            BigDecimal manoObra = BigDecimal.ZERO;
            if (body.get("costoManoObra") != null) {
                manoObra = new BigDecimal(body.get("costoManoObra").toString());
            }

            String metodoPago = body.get("metodoPago") != null ? body.get("metodoPago").toString() : "Efectivo";
            Integer idUsuario = body.get("idUsuario") != null ? Integer.valueOf(body.get("idUsuario").toString()) : 1;

            Facturas factura = facturaService.generarFactura(idOrden, manoObra, metodoPago, idUsuario);
            return ResponseEntity.status(HttpStatus.CREATED).body(factura);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("mensaje", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("mensaje", e.getMessage()));
        }
    }
}

package com.erp.hardware.servicios;

import com.erp.hardware.modelos.DetalleRepuestosOrden;
import com.erp.hardware.modelos.Facturas;
import com.erp.hardware.modelos.OrdenesTrabajo;
import com.erp.hardware.modelos.Usuarios;
import com.erp.hardware.repositorios.DetalleRepuestosOrdenRepository;
import com.erp.hardware.repositorios.FacturasRepository;
import com.erp.hardware.repositorios.OrdenesTrabajoRepository;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FacturaService {

    @Autowired
    private FacturasRepository facturasRepository;

    @Autowired
    private OrdenesTrabajoRepository ordenesRepository;

    @Autowired
    private DetalleRepuestosOrdenRepository detalleRepository;

    public List<Facturas> listarFacturas() {
        return facturasRepository.findByEstadoRegistroTrue();
    }

    public Optional<Facturas> obtenerPorOrden(Integer idOrden) {
        OrdenesTrabajo orden = ordenesRepository.findById(idOrden)
                .orElseThrow(() -> new RuntimeException("Orden no encontrada"));
        return facturasRepository.findByIdOrden(orden);
    }

    public Facturas obtenerPorId(Integer idFactura) {
        return facturasRepository.findById(idFactura)
                .orElseThrow(() -> new RuntimeException("Factura no encontrada: " + idFactura));
    }

    @Transactional
    public Facturas generarFactura(Integer idOrden, BigDecimal costoManoObra, String metodoPago, Integer idUsuario) {
        OrdenesTrabajo orden = ordenesRepository.findById(idOrden)
                .orElseThrow(() -> new RuntimeException("Orden no encontrada: " + idOrden));

        if (facturasRepository.findByIdOrden(orden).isPresent()) {
            throw new IllegalStateException("Esta orden de trabajo ya tiene una factura emitida previamente.");
        }

        if (costoManoObra == null || costoManoObra.compareTo(BigDecimal.ZERO) < 0) {
            costoManoObra = BigDecimal.ZERO;
        }

        if (metodoPago == null || metodoPago.trim().isEmpty()) {
            metodoPago = "Efectivo";
        }

        List<DetalleRepuestosOrden> repuestosUsados = detalleRepository.findByIdOrdenOrderByFechaRegistroAsc(orden);
        BigDecimal totalRepuestos = BigDecimal.ZERO;
        for (DetalleRepuestosOrden det : repuestosUsados) {
            if (det.getSubtotal() != null) {
                totalRepuestos = totalRepuestos.add(det.getSubtotal());
            } else if (det.getPrecioUnitario() != null && det.getCantidad() != null) {
                totalRepuestos = totalRepuestos.add(det.getPrecioUnitario().multiply(BigDecimal.valueOf(det.getCantidad())));
            }
        }

        BigDecimal totalFactura = costoManoObra.add(totalRepuestos);

        String fechaFormato = new SimpleDateFormat("yyyyMMdd").format(new Date());
        String numeroFactura = "FAC-" + fechaFormato + "-ORD" + orden.getIdOrden();

        Usuarios usuarioOperacion = new Usuarios(idUsuario != null ? idUsuario : 1);

        Facturas factura = new Facturas();
        factura.setIdOrden(orden);
        factura.setNumeroFactura(numeroFactura);
        factura.setFechaEmision(new Date());
        factura.setCostoManoObra(costoManoObra);
        factura.setCostoRepuestos(totalRepuestos);
        factura.setTotal(totalFactura);
        factura.setMetodoPago(metodoPago);
        factura.setEstadoFactura("Emitida");
        factura.setFechaCreacion(new Date());
        factura.setEstadoRegistro(true);
        factura.setUsuarioCreacion(usuarioOperacion);

        Facturas facturaGuardada = facturasRepository.save(factura);

        if (!"Reparado".equalsIgnoreCase(orden.getEstadoReparacion()) && !"Entregado".equalsIgnoreCase(orden.getEstadoReparacion())) {
            orden.setEstadoReparacion("Reparado");
            orden.setFechaModificacion(new Date());
            orden.setUsuarioModificacion(usuarioOperacion);
            ordenesRepository.save(orden);
        }

        return facturaGuardada;
    }
}
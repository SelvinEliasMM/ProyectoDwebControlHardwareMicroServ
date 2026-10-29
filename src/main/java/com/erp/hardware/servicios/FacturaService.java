package com.erp.hardware.servicios;

import com.erp.hardware.modelos.BitacoraAuditoria;
import com.erp.hardware.modelos.DetalleRepuestosOrden;
import com.erp.hardware.modelos.Facturas;
import com.erp.hardware.modelos.OrdenesTrabajo;
import com.erp.hardware.modelos.Usuarios;
import com.erp.hardware.repositorios.BitacoraAuditoriaRepository;
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

    @Autowired
    private BitacoraAuditoriaRepository bitacoraRepository;

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

    // generación de factura única con cálculo automático de repuestos mas la mano de obra
    @Transactional
    public Facturas generarFactura(Integer idOrden, BigDecimal costoManoObra, String metodoPago, Integer idUsuario) {
        OrdenesTrabajo orden = ordenesRepository.findById(idOrden)
                .orElseThrow(() -> new RuntimeException("Orden no encontrada: " + idOrden));

        //  no se puede emitir más de una factura por orden
        if (facturasRepository.findByIdOrden(orden).isPresent()) {
            throw new IllegalStateException("Esta orden de trabajo ya tiene una factura emitida previamente.");
        }

        if (costoManoObra == null || costoManoObra.compareTo(BigDecimal.ZERO) < 0) {
            costoManoObra = BigDecimal.ZERO;
        }

        if (metodoPago == null || metodoPago.trim().isEmpty()) {
            metodoPago = "Efectivo";
        }

        // sum automáticamente el costo de todos los repuestos asignados a la orden
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

        if (idUsuario != null) {
            factura.setUsuarioCreacion(new Usuarios(idUsuario));
        }

        Facturas facturaGuardada = facturasRepository.save(factura);

        // registro en bitacora
        try {
            BitacoraAuditoria bit = new BitacoraAuditoria();
            bit.setTablaAfectada("FACTURAS");
            bit.setAccionRealizada("INSERT");
            bit.setFechaHora(new Date());
            bit.setIdUsuario(new Usuarios(idUsuario != null ? idUsuario : 1));
            bit.setDatosOriginales("Emisión de factura");
            bit.setDatosNuevos("{\"id_factura\":" + facturaGuardada.getIdFactura() + ",\"numero\":\"" + facturaGuardada.getNumeroFactura() + "\",\"id_orden\":" + orden.getIdOrden() + ",\"total\":" + facturaGuardada.getTotal() + "}");
            //bitacoraRepository.save(bit);
        } catch (Exception ex) {}

        // actual estado de la orden a 'Reparado' si aún no estaba en ese estado o Entregado
        if (!"Reparado".equalsIgnoreCase(orden.getEstadoReparacion()) && !"Entregado".equalsIgnoreCase(orden.getEstadoReparacion())) {
            orden.setEstadoReparacion("Reparado");
            orden.setFechaModificacion(new Date());
            if (idUsuario != null) {
                orden.setUsuarioModificacion(new Usuarios(idUsuario));
            }
            ordenesRepository.save(orden);
        }

        return facturaGuardada;
    }
}

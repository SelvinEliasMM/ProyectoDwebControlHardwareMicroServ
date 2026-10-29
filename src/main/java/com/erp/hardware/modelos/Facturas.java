package com.erp.hardware.modelos;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "Facturas")
public class Facturas implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_factura")
    private Integer idFactura;

    @JoinColumn(name = "id_orden", referencedColumnName = "id_orden", unique = true)
    @ManyToOne(optional = false)
    private OrdenesTrabajo idOrden;

    @Basic(optional = false)
    @Column(name = "numero_factura", unique = true)
    private String numeroFactura;

    @Column(name = "fecha_emision")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaEmision;

    @Basic(optional = false)
    @Column(name = "costo_mano_obra")
    private BigDecimal costoManoObra = BigDecimal.ZERO;

    @Basic(optional = false)
    @Column(name = "costo_repuestos")
    private BigDecimal costoRepuestos = BigDecimal.ZERO;

    @Basic(optional = false)
    @Column(name = "total")
    private BigDecimal total = BigDecimal.ZERO;

    @Basic(optional = false)
    @Column(name = "metodo_pago")
    private String metodoPago;

    @Column(name = "estado_factura")
    private String estadoFactura = "Emitida";

    @JoinColumn(name = "usuario_creacion", referencedColumnName = "id_usuario")
    @ManyToOne
    private Usuarios usuarioCreacion;

    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;

    @Column(name = "estado_registro")
    private Boolean estadoRegistro = true;

    public Facturas() {
    }

    public Facturas(Integer idFactura) {
        this.idFactura = idFactura;
    }

    public Integer getIdFactura() {
        return idFactura;
    }

    public void setIdFactura(Integer idFactura) {
        this.idFactura = idFactura;
    }

    public OrdenesTrabajo getIdOrden() {
        return idOrden;
    }

    public void setIdOrden(OrdenesTrabajo idOrden) {
        this.idOrden = idOrden;
    }

    public String getNumeroFactura() {
        return numeroFactura;
    }

    public void setNumeroFactura(String numeroFactura) {
        this.numeroFactura = numeroFactura;
    }

    public Date getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(Date fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public BigDecimal getCostoManoObra() {
        return costoManoObra;
    }

    public void setCostoManoObra(BigDecimal costoManoObra) {
        this.costoManoObra = costoManoObra;
    }

    public BigDecimal getCostoRepuestos() {
        return costoRepuestos;
    }

    public void setCostoRepuestos(BigDecimal costoRepuestos) {
        this.costoRepuestos = costoRepuestos;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

    public String getEstadoFactura() {
        return estadoFactura;
    }

    public void setEstadoFactura(String estadoFactura) {
        this.estadoFactura = estadoFactura;
    }

    public Usuarios getUsuarioCreacion() {
        return usuarioCreacion;
    }

    public void setUsuarioCreacion(Usuarios usuarioCreacion) {
        this.usuarioCreacion = usuarioCreacion;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Boolean getEstadoRegistro() {
        return estadoRegistro;
    }

    public void setEstadoRegistro(Boolean estadoRegistro) {
        this.estadoRegistro = estadoRegistro;
    }

    @com.fasterxml.jackson.annotation.JsonProperty("totalFactura")
    public BigDecimal getTotalFactura() {
        return this.total;
    }

    @com.fasterxml.jackson.annotation.JsonProperty("totalFactura")
    public void setTotalFactura(BigDecimal totalFactura) {
        if (totalFactura != null) {
            this.total = totalFactura;
        }
    }
}

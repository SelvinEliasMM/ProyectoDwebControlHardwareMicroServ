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
@Table(name = "Repuestos")
public class Repuestos implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_repuesto")
    private Integer idRepuesto;

    @Basic(optional = false)
    @Column(name = "codigo_repuesto", unique = true)
    private String codigoRepuesto;

    @Basic(optional = false)
    @Column(name = "nombre_repuesto")
    private String nombreRepuesto;

    @Column(name = "descripcion")
    private String descripcion;

    @Basic(optional = false)
    @Column(name = "precio_costo")
    private BigDecimal precioCosto;

    @Basic(optional = false)
    @Column(name = "precio_venta")
    private BigDecimal precioVenta;

    @Basic(optional = false)
    @Column(name = "stock")
    private Integer stock = 0;

    @Basic(optional = false)
    @Column(name = "stock_minimo")
    private Integer stockMinimo = 3;

    @JoinColumn(name = "usuario_creacion", referencedColumnName = "id_usuario")
    @ManyToOne
    private Usuarios usuarioCreacion;

    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;

    @JoinColumn(name = "usuario_modificacion", referencedColumnName = "id_usuario")
    @ManyToOne
    private Usuarios usuarioModificacion;

    @Column(name = "fecha_modificacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaModificacion;

    @Column(name = "estado_registro")
    private Boolean estadoRegistro = true;

    public Repuestos() {
    }

    public Repuestos(Integer idRepuesto) {
        this.idRepuesto = idRepuesto;
    }

    public Integer getIdRepuesto() {
        return idRepuesto;
    }

    public void setIdRepuesto(Integer idRepuesto) {
        this.idRepuesto = idRepuesto;
    }

    public String getCodigoRepuesto() {
        return codigoRepuesto;
    }

    public void setCodigoRepuesto(String codigoRepuesto) {
        this.codigoRepuesto = codigoRepuesto;
    }

    public String getNombreRepuesto() {
        return nombreRepuesto;
    }

    public void setNombreRepuesto(String nombreRepuesto) {
        this.nombreRepuesto = nombreRepuesto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigDecimal getPrecioCosto() {
        return precioCosto;
    }

    public void setPrecioCosto(BigDecimal precioCosto) {
        this.precioCosto = precioCosto;
    }

    public BigDecimal getPrecioVenta() {
        return precioVenta;
    }

    public void setPrecioVenta(BigDecimal precioVenta) {
        this.precioVenta = precioVenta;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Integer getStockMinimo() {
        return stockMinimo;
    }

    public void setStockMinimo(Integer stockMinimo) {
        this.stockMinimo = stockMinimo;
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

    public Usuarios getUsuarioModificacion() {
        return usuarioModificacion;
    }

    public void setUsuarioModificacion(Usuarios usuarioModificacion) {
        this.usuarioModificacion = usuarioModificacion;
    }

    public Date getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(Date fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public Boolean getEstadoRegistro() {
        return estadoRegistro;
    }

    public void setEstadoRegistro(Boolean estadoRegistro) {
        this.estadoRegistro = estadoRegistro;
    }

    @com.fasterxml.jackson.annotation.JsonProperty("stockActual")
    public Integer getStockActual() {
        return this.stock;
    }

    @com.fasterxml.jackson.annotation.JsonProperty("stockActual")
    public void setStockActual(Integer stockActual) {
        if (stockActual != null) {
            this.stock = stockActual;
        }
    }

    @com.fasterxml.jackson.annotation.JsonProperty("precioUnitario")
    public BigDecimal getPrecioUnitario() {
        return this.precioVenta;
    }

    @com.fasterxml.jackson.annotation.JsonProperty("precioUnitario")
    public void setPrecioUnitario(BigDecimal precioUnitario) {
        if (precioUnitario != null) {
            this.precioVenta = precioUnitario;
            if (this.precioCosto == null) {
                this.precioCosto = precioUnitario;
            }
        }
    }
}

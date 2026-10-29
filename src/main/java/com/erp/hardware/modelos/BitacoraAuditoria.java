/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.erp.hardware.modelos;

import java.io.Serializable;
import java.util.Date;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

/**
 *
 * @author selvi
 */
@Entity
@Table(name = "Bitacora_Auditoria")
@NamedQueries({
    @NamedQuery(name = "BitacoraAuditoria.findAll", query = "SELECT b FROM BitacoraAuditoria b"),
    @NamedQuery(name = "BitacoraAuditoria.findByIdBitacora", query = "SELECT b FROM BitacoraAuditoria b WHERE b.idBitacora = :idBitacora"),
    @NamedQuery(name = "BitacoraAuditoria.findByTablaAfectada", query = "SELECT b FROM BitacoraAuditoria b WHERE b.tablaAfectada = :tablaAfectada"),
    @NamedQuery(name = "BitacoraAuditoria.findByAccionRealizada", query = "SELECT b FROM BitacoraAuditoria b WHERE b.accionRealizada = :accionRealizada"),
    @NamedQuery(name = "BitacoraAuditoria.findByFechaHora", query = "SELECT b FROM BitacoraAuditoria b WHERE b.fechaHora = :fechaHora"),
    @NamedQuery(name = "BitacoraAuditoria.findByDatosOriginales", query = "SELECT b FROM BitacoraAuditoria b WHERE b.datosOriginales = :datosOriginales"),
    @NamedQuery(name = "BitacoraAuditoria.findByDatosNuevos", query = "SELECT b FROM BitacoraAuditoria b WHERE b.datosNuevos = :datosNuevos")})
public class BitacoraAuditoria implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_bitacora")
    private Integer idBitacora;
    @Column(name = "tabla_afectada")
    private String tablaAfectada;
    @Column(name = "accion_realizada")
    private String accionRealizada;
    @Column(name = "fecha_hora")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaHora;
    @Column(name = "datos_originales")
    private String datosOriginales;
    @Column(name = "datos_nuevos")
    private String datosNuevos;
    @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario")
    @ManyToOne
    private Usuarios idUsuario;

    public BitacoraAuditoria() {
    }

    public BitacoraAuditoria(Integer idBitacora) {
        this.idBitacora = idBitacora;
    }

    public Integer getIdBitacora() {
        return idBitacora;
    }

    public void setIdBitacora(Integer idBitacora) {
        this.idBitacora = idBitacora;
    }

    public String getTablaAfectada() {
        return tablaAfectada;
    }

    public void setTablaAfectada(String tablaAfectada) {
        this.tablaAfectada = tablaAfectada;
    }

    public String getAccionRealizada() {
        return accionRealizada;
    }

    public void setAccionRealizada(String accionRealizada) {
        this.accionRealizada = accionRealizada;
    }

    public Date getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(Date fechaHora) {
        this.fechaHora = fechaHora;
    }

    public String getDatosOriginales() {
        return datosOriginales;
    }

    public void setDatosOriginales(String datosOriginales) {
        this.datosOriginales = datosOriginales;
    }

    public String getDatosNuevos() {
        return datosNuevos;
    }

    public void setDatosNuevos(String datosNuevos) {
        this.datosNuevos = datosNuevos;
    }

    public Usuarios getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Usuarios idUsuario) {
        this.idUsuario = idUsuario;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idBitacora != null ? idBitacora.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof BitacoraAuditoria)) {
            return false;
        }
        BitacoraAuditoria other = (BitacoraAuditoria) object;
        if ((this.idBitacora == null && other.idBitacora != null) || (this.idBitacora != null && !this.idBitacora.equals(other.idBitacora))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.erp.hardware.modelos.BitacoraAuditoria[ idBitacora=" + idBitacora + " ]";
    }
    
}

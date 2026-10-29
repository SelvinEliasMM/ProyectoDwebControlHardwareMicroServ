/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.erp.hardware.modelos;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

/**
 *
 * @author selvi
 */
@Entity
@Table(name = "Equipos")
@NamedQueries({
    @NamedQuery(name = "Equipos.findAll", query = "SELECT e FROM Equipos e"),
    @NamedQuery(name = "Equipos.findByIdEquipo", query = "SELECT e FROM Equipos e WHERE e.idEquipo = :idEquipo"),
    @NamedQuery(name = "Equipos.findByTipoEquipo", query = "SELECT e FROM Equipos e WHERE e.tipoEquipo = :tipoEquipo"),
    @NamedQuery(name = "Equipos.findByMarcaModelo", query = "SELECT e FROM Equipos e WHERE e.marcaModelo = :marcaModelo"),
    @NamedQuery(name = "Equipos.findByNumeroSerie", query = "SELECT e FROM Equipos e WHERE e.numeroSerie = :numeroSerie"),
    @NamedQuery(name = "Equipos.findByCaracteristicasHardware", query = "SELECT e FROM Equipos e WHERE e.caracteristicasHardware = :caracteristicasHardware"),
    @NamedQuery(name = "Equipos.findByFechaCreacion", query = "SELECT e FROM Equipos e WHERE e.fechaCreacion = :fechaCreacion"),
    @NamedQuery(name = "Equipos.findByFechaModificacion", query = "SELECT e FROM Equipos e WHERE e.fechaModificacion = :fechaModificacion"),
    @NamedQuery(name = "Equipos.findByEstadoRegistro", query = "SELECT e FROM Equipos e WHERE e.estadoRegistro = :estadoRegistro")})
public class Equipos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_equipo")
    private Integer idEquipo;
    @Column(name = "tipo_equipo")
    private String tipoEquipo;
    @Column(name = "marca_modelo")
    private String marcaModelo;
    @Column(name = "numero_serie")
    private String numeroSerie;
    @Column(name = "caracteristicas_hardware")
    private String caracteristicasHardware;
    @Column(name = "fecha_creacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaCreacion;
    @Column(name = "fecha_modificacion")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaModificacion;
    @Column(name = "estado_registro")
    private Boolean estadoRegistro;
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idEquipo")
    private List<OrdenesTrabajo> ordenesTrabajoList;
    @JoinColumn(name = "id_cliente", referencedColumnName = "id_cliente")
    @ManyToOne(optional = false)
    private Clientes idCliente;
    @JoinColumn(name = "usuario_creacion", referencedColumnName = "id_usuario")
    @ManyToOne
    private Usuarios usuarioCreacion;
    @JoinColumn(name = "usuario_modificacion", referencedColumnName = "id_usuario")
    @ManyToOne
    private Usuarios usuarioModificacion;

    public Equipos() {
    }

    public Equipos(Integer idEquipo) {
        this.idEquipo = idEquipo;
    }

    public Integer getIdEquipo() {
        return idEquipo;
    }

    public void setIdEquipo(Integer idEquipo) {
        this.idEquipo = idEquipo;
    }

    public String getTipoEquipo() {
        return tipoEquipo;
    }

    public void setTipoEquipo(String tipoEquipo) {
        this.tipoEquipo = tipoEquipo;
    }

    public String getMarcaModelo() {
        return marcaModelo;
    }

    public void setMarcaModelo(String marcaModelo) {
        this.marcaModelo = marcaModelo;
    }

    public String getNumeroSerie() {
        return numeroSerie;
    }

    public void setNumeroSerie(String numeroSerie) {
        this.numeroSerie = numeroSerie;
    }

    public String getCaracteristicasHardware() {
        return caracteristicasHardware;
    }

    public void setCaracteristicasHardware(String caracteristicasHardware) {
        this.caracteristicasHardware = caracteristicasHardware;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
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

    public List<OrdenesTrabajo> getOrdenesTrabajoList() {
        return ordenesTrabajoList;
    }

    public void setOrdenesTrabajoList(List<OrdenesTrabajo> ordenesTrabajoList) {
        this.ordenesTrabajoList = ordenesTrabajoList;
    }

    public Clientes getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Clientes idCliente) {
        this.idCliente = idCliente;
    }

    public Usuarios getUsuarioCreacion() {
        return usuarioCreacion;
    }

    public void setUsuarioCreacion(Usuarios usuarioCreacion) {
        this.usuarioCreacion = usuarioCreacion;
    }

    public Usuarios getUsuarioModificacion() {
        return usuarioModificacion;
    }

    public void setUsuarioModificacion(Usuarios usuarioModificacion) {
        this.usuarioModificacion = usuarioModificacion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idEquipo != null ? idEquipo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Equipos)) {
            return false;
        }
        Equipos other = (Equipos) object;
        if ((this.idEquipo == null && other.idEquipo != null) || (this.idEquipo != null && !this.idEquipo.equals(other.idEquipo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.erp.hardware.modelos.Equipos[ idEquipo=" + idEquipo + " ]";
    }
    
}

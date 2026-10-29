/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.erp.hardware.modelos;

import java.io.Serializable;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

/**
 *
 * @author selvi
 */
@Entity
@Table(name = "Usuarios")
@NamedQueries({
    @NamedQuery(name = "Usuarios.findAll", query = "SELECT u FROM Usuarios u"),
    @NamedQuery(name = "Usuarios.findByIdUsuario", query = "SELECT u FROM Usuarios u WHERE u.idUsuario = :idUsuario"),
    @NamedQuery(name = "Usuarios.findByNombreCompleto", query = "SELECT u FROM Usuarios u WHERE u.nombreCompleto = :nombreCompleto"),
    @NamedQuery(name = "Usuarios.findByCorreo", query = "SELECT u FROM Usuarios u WHERE u.correo = :correo"),
    @NamedQuery(name = "Usuarios.findByPasswordHash", query = "SELECT u FROM Usuarios u WHERE u.passwordHash = :passwordHash"),
    @NamedQuery(name = "Usuarios.findByRol", query = "SELECT u FROM Usuarios u WHERE u.rol = :rol")})
public class Usuarios implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_usuario")
    private Integer idUsuario;
    @Basic(optional = false)
    @Column(name = "nombre_completo")
    private String nombreCompleto;
    @Basic(optional = false)
    @Column(name = "correo")
    private String correo;
    @Basic(optional = false)
    @Column(name = "password_hash")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String passwordHash;
    @Basic(optional = false)
    @Column(name = "rol")
    private String rol;
    @JsonIgnore
    @OneToMany(mappedBy = "usuarioCreacion")
    private List<OrdenesTrabajo> ordenesTrabajoList;
    @JsonIgnore
    @OneToMany(mappedBy = "usuarioModificacion")
    private List<OrdenesTrabajo> ordenesTrabajoList1;
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idUsuarioTecnico")
    private List<OrdenesTrabajo> ordenesTrabajoList2;
    @JsonIgnore
    @OneToMany(mappedBy = "usuarioCreacion")
    private List<Clientes> clientesList;
    @JsonIgnore
    @OneToMany(mappedBy = "usuarioModificacion")
    private List<Clientes> clientesList1;
    @JsonIgnore
    @OneToMany(mappedBy = "usuarioCreacion")
    private List<Equipos> equiposList;
    @JsonIgnore
    @OneToMany(mappedBy = "usuarioModificacion")
    private List<Equipos> equiposList1;
    @JsonIgnore
    @OneToMany(mappedBy = "idUsuario")
    private List<BitacoraAuditoria> bitacoraAuditoriaList;

    public Usuarios() {
    }

    public Usuarios(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Usuarios(Integer idUsuario, String nombreCompleto, String correo, String passwordHash, String rol) {
        this.idUsuario = idUsuario;
        this.nombreCompleto = nombreCompleto;
        this.correo = correo;
        this.passwordHash = passwordHash;
        this.rol = rol;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public List<OrdenesTrabajo> getOrdenesTrabajoList() {
        return ordenesTrabajoList;
    }

    public void setOrdenesTrabajoList(List<OrdenesTrabajo> ordenesTrabajoList) {
        this.ordenesTrabajoList = ordenesTrabajoList;
    }

    public List<OrdenesTrabajo> getOrdenesTrabajoList1() {
        return ordenesTrabajoList1;
    }

    public void setOrdenesTrabajoList1(List<OrdenesTrabajo> ordenesTrabajoList1) {
        this.ordenesTrabajoList1 = ordenesTrabajoList1;
    }

    public List<OrdenesTrabajo> getOrdenesTrabajoList2() {
        return ordenesTrabajoList2;
    }

    public void setOrdenesTrabajoList2(List<OrdenesTrabajo> ordenesTrabajoList2) {
        this.ordenesTrabajoList2 = ordenesTrabajoList2;
    }

    public List<Clientes> getClientesList() {
        return clientesList;
    }

    public void setClientesList(List<Clientes> clientesList) {
        this.clientesList = clientesList;
    }

    public List<Clientes> getClientesList1() {
        return clientesList1;
    }

    public void setClientesList1(List<Clientes> clientesList1) {
        this.clientesList1 = clientesList1;
    }

    public List<Equipos> getEquiposList() {
        return equiposList;
    }

    public void setEquiposList(List<Equipos> equiposList) {
        this.equiposList = equiposList;
    }

    public List<Equipos> getEquiposList1() {
        return equiposList1;
    }

    public void setEquiposList1(List<Equipos> equiposList1) {
        this.equiposList1 = equiposList1;
    }

    public List<BitacoraAuditoria> getBitacoraAuditoriaList() {
        return bitacoraAuditoriaList;
    }

    public void setBitacoraAuditoriaList(List<BitacoraAuditoria> bitacoraAuditoriaList) {
        this.bitacoraAuditoriaList = bitacoraAuditoriaList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idUsuario != null ? idUsuario.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Usuarios)) {
            return false;
        }
        Usuarios other = (Usuarios) object;
        if ((this.idUsuario == null && other.idUsuario != null) || (this.idUsuario != null && !this.idUsuario.equals(other.idUsuario))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.erp.hardware.modelos.Usuarios[ idUsuario=" + idUsuario + " ]";
    }
    
}

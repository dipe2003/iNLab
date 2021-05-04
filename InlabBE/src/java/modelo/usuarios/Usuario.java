/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.usuarios;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 *
 * @author dipe2
 */
@Entity
public class Usuario implements Serializable, Comparable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String nombre;

    private Permiso tipo;
    @Temporal(TemporalType.DATE)
    private Date fechaObsoleto;

    @OneToOne(orphanRemoval = true, mappedBy = "usuarioCredencial", cascade = CascadeType.ALL)
    private Credencial credencialUsuario;

    public Usuario() {
    }

    public Usuario(String nombre, Permiso tipo) {
        this.nombre = nombre;
        this.tipo = tipo;
    }

    //<editor-fold desc="Getters">
    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public Permiso getTipo() {
        return tipo;
    }

    public Date getFechaObsoleto() {
        return fechaObsoleto;
    }

    public Credencial getCredencialUsuario() {
        return credencialUsuario;
    }

    //</editor-fold>
    
    //<editor-fold desc="Setters">
    public void setId(Long id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setTipo(Permiso tipo) {
        this.tipo = tipo;
    }

    public void setFechaObsoleto(Date fechaObsoleto) {
        this.fechaObsoleto = fechaObsoleto;
    }

    public void setCredencialUsuario(Credencial credencialUsuario) {
        this.credencialUsuario = credencialUsuario;
    }
    
    //</editor-fold>
    
    public boolean EsVigente(){
        return this.fechaObsoleto == null;
    }
    
    public boolean isVigente(){
         return this.fechaObsoleto == null;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Usuario)) {
            return false;
        }
        Usuario other = (Usuario) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelo.usuarios.Usuario[ id=" + id + " ]";
    }

    @Override
    public int compareTo(Object o) {
       return this.nombre.compareToIgnoreCase(((Usuario)o).nombre);
    }

}

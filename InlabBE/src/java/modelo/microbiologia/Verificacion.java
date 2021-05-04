/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.microbiologia;

import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import modelo.usuarios.Usuario;
import modelo.muestreo.Muestreo;

/**
 *
 * @author dipe2
 */
@Entity
public class Verificacion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String observaciones;

    @ManyToOne
    private Usuario usuarioVerificador;

    private EstadoVerificacion estadoVerificacion;

    @OneToOne(cascade = CascadeType.ALL)
    private Muestreo muestreo;

    /*
        Constructores
     */
    public Verificacion() {
    }

    public Verificacion(Usuario verificador, EstadoVerificacion estado, String observaciones, Muestreo muestreo) {
        this.usuarioVerificador = verificador;
        this.estadoVerificacion = estado;
        this.observaciones = observaciones;
        this.muestreo = muestreo;
    }

    //<editor-fold desc="Getters">
    public Long getId() {
        return id;
    }

    public String getObservaciones() {
        return this.observaciones;
    }

    public Usuario getUsuarioVerificador() {
        return this.usuarioVerificador;
    }

    public EstadoVerificacion getResultado() {
        return this.estadoVerificacion;
    }

    public Muestreo getMuestreo() {
        return this.muestreo;
    }
    //</editor-fold>
    
    //<editor-fold desc="Setters">
    public void setId(Long id) {
        this.id = id;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public void setUsuarioVerificador(Usuario verificador) {
        this.usuarioVerificador = verificador;
    }

    public void setResultado(EstadoVerificacion resultado) {
        this.estadoVerificacion = resultado;
    }

    public void setMuestreo(Muestreo muestreo) {
        this.muestreo = muestreo;
    }
    //</editor-fold>
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Verificacion)) {
            return false;
        }
        Verificacion other = (Verificacion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "microbiologia.Verificacion[ id=" + id + " ]";
    }

}

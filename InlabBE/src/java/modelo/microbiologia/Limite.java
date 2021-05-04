/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.microbiologia;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 *
 * @author dipe2
 */
@Entity
public abstract class Limite implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Temporal(TemporalType.DATE)
    private Date fechaObsoleto;

    @ManyToOne
    private Requisito requisito;

    @Transient
    private TipoLimite tipoLimite;

    /*
        Constructores
     */
    public Limite() {
    }

    public Limite(Requisito requisito, TipoLimite tipo) {
        this.requisito = requisito;
        this.tipoLimite = tipo;
    }

    //<editor-fold desc="Getters">
    public Long getId() {
        return id;
    }

    public Date getFechaObsoleto() {
        return fechaObsoleto;
    }

    public Requisito getRequisito() {
        return requisito;
    }

    public TipoLimite getTipoLimite() {
        return this.tipoLimite;
    }
    //</editor-fold>

    //<editor-fold desc="Setters">
    public void setId(Long id) {
        this.id = id;
    }

    public void setFechaObsoleto(Date fechaObsoleto) {
        this.fechaObsoleto = fechaObsoleto;
    }

    public void setRequisito(Requisito requisito) {
        this.requisito = requisito;
    }

    public void setTipoLimite(TipoLimite tipo) {
        this.tipoLimite = tipo;
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
        if (!(object instanceof Limite)) {
            return false;
        }
        Limite other = (Limite) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "microbiologia.Limite[ id=" + id + " ]";
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.microbiologia;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

/**
 *
 * @author dipe2
 */
@Entity
public abstract class Resultado implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @OneToOne(mappedBy = "resultado")
    private Analisis analisis;

    /*
        Constructores
     */
    public Resultado() {
    }

    public Resultado(Analisis analisis) {
        this.analisis = analisis;
    }

    //<editor-fold desc="Getters">
    public Long getId() {
        return id;
    }
    
    public Analisis getAnalisis() {
        return this.analisis;
    }
    //</editor-fold>
    
    //<editor-fold desc="Setters">
    public void setId(Long id) {
        this.id = id;
    }
    
    public void setAnalisis(Analisis analisis) {
        this.analisis = analisis;
    }
    //</editor-fold>
    
    /*
        Metodos
     */
    public abstract boolean isAceptable();

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Resultado)) {
            return false;
        }
        Resultado other = (Resultado) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "microbiologia.Resultado[ id=" + id + " ]";
    }
    
}

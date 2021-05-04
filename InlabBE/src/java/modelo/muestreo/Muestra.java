/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.muestreo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import modelo.usuarios.Usuario;

/**
 *
 * @author dipe2
 */
@Entity
public abstract class Muestra implements Serializable, Comparable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @ManyToOne
    private EspecificacionMuestra especificacionMuestra;
       
    @OneToMany(mappedBy = "muestra", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Muestreo> muestreos;

    /*
        Constructores
     */
    public Muestra() {  
        this.muestreos = new ArrayList<>();  
    }
    
    public Muestra(EspecificacionMuestra especificacion) {
        this.muestreos = new ArrayList<>();
        this.especificacionMuestra = especificacion;
    }

    //<editor-fold desc="Getters">
    public Long getId() {
        return id;
    }
    
    public List<Muestreo> getMuestreos(){
        return this.muestreos;
    }
    
    public EspecificacionMuestra getEspecificacionMuestra() {
        return especificacionMuestra;
    }
       
   //</editor-fold>
    
    //<editor-fold desc="Setters">
    public void setId(Long id) {
        this.id = id;
    }
    
    public void setMuestreos(List<Muestreo> muestreos){
        this.muestreos = muestreos;
    }

    public void setEspecificacionMuestra(EspecificacionMuestra especificacionMuestra) {
        this.especificacionMuestra = especificacionMuestra;
    }
    //</editor-fold>
    
    //<editor-fold desc="Muestreos">
    public Muestreo AgregarMuestreo(Date fecha, String observaciones, 
            boolean esRepeticion, Almacenamiento almacenamiento, Usuario monitor){
        Muestreo muestreo = new Muestreo(fecha, observaciones, esRepeticion, almacenamiento,
        monitor, this);
        this.muestreos.add(muestreo);
        return muestreo;
    }
    
    public void RemoverMuestreo(Muestreo muestreo){
        this.muestreos.forEach((Muestreo item)->{
            if(Objects.equals(item.getId(), muestreo.getId())){
                this.muestreos.remove(item);
            }
        });
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
        if (!(object instanceof Muestra)) {
            return false;
        }
        Muestra other = (Muestra) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "muestreo.Muestra[ id=" + id + " ]";
    }
    
    @Override
    public int compareTo(Object o){
        return this.especificacionMuestra.getDenominacion().compareToIgnoreCase(((Muestra)o).especificacionMuestra.getDenominacion());
    }
    
}

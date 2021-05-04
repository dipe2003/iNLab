/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.microbiologia;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import modelo.muestreo.Destino;

/**
 *
 * @author dipe2
 */
@Entity
public class Ensayo implements Serializable, Comparable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    private String denominacion;
    
    @OneToMany(mappedBy = "ensayo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Requisito> requisitos;
    
    /*
        Constructores
    */
    public Ensayo(){
        this.requisitos =  new ArrayList<>();
    }
    
    public Ensayo(String denominacion){
        this.requisitos =  new ArrayList<>();
        this.denominacion = denominacion;
    }

    //<editor-fold desc="Getters">
    public Long getId() {
        return id;
    }
    
     public String getDenominacion() {
        return denominacion;
    }
    
    public List<Requisito> getRequisitos(){
        return this.requisitos;
    }

    //</editor-fold>
    
    //<editor-fold desc="Setters">
    public void setId(Long id) {
        this.id = id;
    }

    public void setDenominacion(String denominacion) {
        this.denominacion = denominacion;
    }

    public void setRequisitos(List<Requisito> requisitos) {
        this.requisitos = requisitos;
    }
    
    //</editor-fold>
    
  //<editor-fold desc="Metodos">
    public Requisito AregarRequisito(Destino destino){
        Requisito requisito = new Requisito(this, destino);
        this.requisitos.add(requisito);
        destino.AgregarRequisito(requisito);
        return requisito;
    }
    
    public void EliminarRequisito(Requisito requisito){
        this.requisitos.forEach((item)->{
            if(Objects.equals(item.getId(), requisito.getId())){
                item.getDestino().ReomverRequisito(requisito);
                this.requisitos.remove(item);
            }
        });
    }
    
    public Requisito FindRequisito(Long idRequisito){
        return this.requisitos.stream()
                .filter(r->Objects.equals(r.getId(), idRequisito))
                .findFirst()
                .get();
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
        if (!(object instanceof Ensayo)) {
            return false;
        }
        Ensayo other = (Ensayo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "microbiologia.Ensayo[ id=" + id + " ]";
    }

    @Override
    public int compareTo(Object o) {
        return this.denominacion.compareToIgnoreCase(((Ensayo)o).denominacion);
    }
    
}

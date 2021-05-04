/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.muestreo;

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
import modelo.microbiologia.Requisito;

/**
 *
 * @author dipe2
 */
@Entity
public class Destino implements Serializable, Comparable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String Denominacion;

    @OneToMany(mappedBy = "destino", cascade = CascadeType.ALL)
    private List<Requisito> requisitos;

    @OneToMany(mappedBy = "destino")
    private List<EspecificacionMuestra> especificacionesMuestras;

    /*
        Constructores
     */
    public Destino() {
        this.requisitos = new ArrayList<>();
        this.especificacionesMuestras = new ArrayList<>();
    }

    public Destino(String nombre) {
        this.requisitos = new ArrayList<>();
        this.Denominacion = nombre;
        this.especificacionesMuestras = new ArrayList<>();
    }

    //<editor-fold desc="Getters">
    public Long getId() {
        return id;
    }

    public String getDenominacion() {
        return Denominacion;
    }

    public List<Requisito> getRequisitos() {
        return this.requisitos;
    }

    public List<EspecificacionMuestra> getEspecificacionesMuestras() {
        return especificacionesMuestras;
    }

    //</editor-fold>
    //<editor-fold desc="Setter">
    public void setId(Long id) {
        this.id = id;
    }

    public void setDenominacion(String Denominacion) {
        this.Denominacion = Denominacion;
    }

    public void setRequisitos(List<Requisito> requisitos) {
        this.requisitos = requisitos;
    }

    public void setEspecificacionesMuestras(List<EspecificacionMuestra> especificacionesMuestras) {
        this.especificacionesMuestras = especificacionesMuestras;
    }

    //</editor-fold>
    
    
    public void AgregarRequisito(Requisito requisito) {
        this.requisitos.add(requisito);
    }

    public void ReomverRequisito(Requisito requisito) {
        this.requisitos.forEach((Requisito item) -> {
            if (Objects.equals(item.getId(), requisito.getId())) {
                this.requisitos.remove(item);
            }
        });
    }

    public Requisito FindRequisito(Long idRequisito) {
        return this.requisitos.stream()
                .filter(r -> Objects.equals(r.getId(), idRequisito))
                .findFirst()
                .get();
    }

    public void AgregarEspecificacionMuestra(EspecificacionMuestra especificacionMuestra) {
        this.especificacionesMuestras.add(especificacionMuestra);
    }

    public void ReomverEspecificacionMuestra(EspecificacionMuestra especificacionMuestra) {
        this.especificacionesMuestras.forEach((EspecificacionMuestra item) -> {
            if (Objects.equals(item.getId(), especificacionMuestra.getId())) {
                this.requisitos.remove(item);
            }
        });
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
        if (!(object instanceof Destino)) {
            return false;
        }
        Destino other = (Destino) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "muestreo.Destino[ id=" + id + " ]";
    }

    @Override
    public int compareTo(Object o) {
        return this.Denominacion.compareTo(((Destino)o).Denominacion);
    }

}

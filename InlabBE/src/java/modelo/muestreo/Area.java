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
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author dipe2
 */
@Entity
public class Area implements Serializable, Comparable{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String nombre;
    @Temporal(TemporalType.DATE)
    private Date fechaObsoleto;
    private boolean esProductiva;

    @OneToMany(mappedBy = "area", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EspecificacionMuestra> especificacionesMuestra;

    /*
        Constructores
     */
    public Area() {
        this.especificacionesMuestra = new ArrayList<>();
    }

    public Area(String nombre, boolean esProductiva) {
        this.especificacionesMuestra = new ArrayList<>();
        this.nombre = nombre;
        this.esProductiva = esProductiva;
    }

    //<editor-fold desc="Getters">
    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public Date getFechaObsoleto() {
        return fechaObsoleto;
    }

    public boolean isEsProductiva() {
        return esProductiva;
    }

    public List<EspecificacionMuestra> getEspecificacionesMuestra() {
        return this.especificacionesMuestra;
    }

    //</editor-fold>
    
    //<editor-fold desc="Setters">


    public void setId(Long id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setFechaObsoleto(Date fechaObsoleto) {
        this.fechaObsoleto = fechaObsoleto;
    }

    public void setEsProductiva(boolean esProductiva) {
        this.esProductiva = esProductiva;
    }

    public void setEspecificacionesMuestra(List<EspecificacionMuestra> muestras) {
        this.especificacionesMuestra = muestras;
    }

    //</editor-fold>
    
    /*
        Metodos
     */
    public void AgregarEspecificacionMuestra(EspecificacionMuestra muestra) {
        this.especificacionesMuestra.add(muestra);
    }

    public void RemoverEspecificacionMuestra(Muestra muestra) {
        this.especificacionesMuestra.forEach((item) -> {
            if (Objects.equals(item.getId(), muestra.getId())) {
                especificacionesMuestra.remove(item);
            }
        });
    }
    public boolean isEsVigente() {
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
        if (!(object instanceof Area)) {
            return false;
        }
        Area other = (Area) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "muestreo.Area[ id=" + id + " ]";
    }

    @Override
    public int compareTo(Object o) {
        return this.nombre.compareToIgnoreCase(((Area)o).nombre);
    }

}

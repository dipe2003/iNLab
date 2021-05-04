/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.microbiologia;

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
import modelo.muestreo.Muestreo;
import modelo.usuarios.Usuario;

/**
 *
 * @author dipe2
 */
@Entity
public class Laboratorio implements Serializable, Comparable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String nombre;
    private String detalles;
    private boolean esExterno;

    @OneToMany(mappedBy = "laboratorio", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Analisis> analisisLaboratorio;

    /*
        Constructores
     */
    public Laboratorio() {
        this.analisisLaboratorio = new ArrayList<>();
    }

    public Laboratorio(String nombre, String detalles, boolean esExterno) {
        this.analisisLaboratorio = new ArrayList<>();
        this.nombre = nombre;
        this.esExterno = esExterno;
        this.detalles = detalles;
    }

    //<editor-fold desc="Getters">
    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDetalles() {
        return detalles;
    }

    public boolean isEsExterno() {
        return esExterno;
    }

    public List<Analisis> getAnalisisLaboratorio() {
        return this.analisisLaboratorio;
    }

    //</editor-fold>
    //<editor-fold desc="Setters">
    public void setId(Long id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setDetalles(String detalles) {
        this.detalles = detalles;
    }

    public void setEsExterno(boolean esExterno) {
        this.esExterno = esExterno;
    }

    public void setAnalisisLaboratorio(List<Analisis> analisisLaboratorio) {
        this.analisisLaboratorio = analisisLaboratorio;
    }

    //</editor-fold>
    /*
        Metodos
     */
    /**
     * Agrega un nuevo analisis al muestreo.El requisito sobre el que se hace el
     * analisi debe estar asociado a la muestra.
     *
     * @param requisito
     * @param fechaAnalisis
     * @param observacionesAnalisis
     * @param analista
     * @param muestreo
     * @return
     * @throws Exception
     */
    public Analisis AgregarAnalisis(Requisito requisito, Date fechaAnalisis, String observacionesAnalisis,
            Usuario analista, Muestreo muestreo) throws Exception {
        if (muestreo.getMuestra().getEspecificacionMuestra().getDestino().FindRequisito(requisito.getId()) == null) {
            throw new Exception("El requisito no esta asociado a la muestra.");
        }
        Analisis test = new Analisis(requisito, muestreo, fechaAnalisis, observacionesAnalisis, analista, this);
        this.analisisLaboratorio.add(test);
        return test;
    }

    public Analisis FindAnalisis(Long idAnalisis) {
        return this.analisisLaboratorio.stream()
                .filter(a -> Objects.equals(a.getId(), idAnalisis))
                .findFirst()
                .get();
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
        if (!(object instanceof Laboratorio)) {
            return false;
        }
        Laboratorio other = (Laboratorio) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "microbiologia.Laboratorio[ id=" + id + " ]";
    }

    @Override
    public int compareTo(Object o) {
        return this.nombre.compareToIgnoreCase(((Laboratorio)o).nombre);
    }

}

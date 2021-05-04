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
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import modelo.microbiologia.Analisis;
import modelo.microbiologia.EstadoVerificacion;
import modelo.microbiologia.Verificacion;
import modelo.usuarios.Usuario;

/**
 *
 * @author dipe2
 */
@Entity
public class Muestreo implements Serializable, Comparable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Temporal(TemporalType.DATE)
    private Date fechaMuestreo;
    private String observaciones;
    private boolean esRepeticion;

    @ManyToOne
    private Usuario usuarioMonitor;

    private Almacenamiento almacenamiento;

    @ManyToOne(cascade = CascadeType.ALL)
    private Muestra muestra;

    @OneToMany(mappedBy = "muestreo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Analisis> analisis;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private Verificacion verificacion;
    
    @Transient
    private boolean habilitado;

    /*
        Constructores
     */
    public Muestreo() {
        this.analisis = new ArrayList<>();
        this.habilitado = this.calcularHabilitado();
    }

    public Muestreo(Date fecha, String observaciones, boolean esRepeticion,
            Almacenamiento almacenamiento, Usuario monitor, Muestra muestra) {
        this.fechaMuestreo = fecha;
        this.observaciones = observaciones;
        this.esRepeticion = esRepeticion;
        this.almacenamiento = almacenamiento;
        this.usuarioMonitor = monitor;
        this.muestra = muestra;
        this.analisis = new ArrayList<>();
    }

    //<editor-fold desc="Getters">
    public Long getId() {
        return id;
    }

    public Date getFechaMuestreo() {
        return fechaMuestreo;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public Usuario getUsuarioMonitor() {
        return usuarioMonitor;
    }

    public Almacenamiento getAlmacenamiento() {
        return almacenamiento;
    }

    public Muestra getMuestra() {
        return muestra;
    }

    public boolean isEsRepeticion() {
        return this.esRepeticion;
    }

    public List<Analisis> getAnalisis() {
        return this.analisis;
    }

    public Verificacion getVerificacion() {
        return this.verificacion;
    }

    public boolean isHabilitado() {
        return this.calcularHabilitado();
    }
    
    
    //</editor-fold>

    //<editor-fold desc="Setters">
    public void setId(Long id) {
        this.id = id;
    }

    public void setFechaMuestreo(Date fechaMuestreo) {
        this.fechaMuestreo = fechaMuestreo;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public void setUsuarioMonitor(Usuario usuarioMonitor) {
        this.usuarioMonitor = usuarioMonitor;
    }

    public void setAlmacenamiento(Almacenamiento almacenamiento) {
        this.almacenamiento = almacenamiento;
    }

    public void setMuestra(Muestra muestra) {
        this.muestra = muestra;
    }

    public void setEsRepeticion(boolean esRepeticion) {
        this.esRepeticion = esRepeticion;
    }

    public void setAnalisis(List<Analisis> analisis) {
        this.analisis = analisis;
    }

    public void setVerificacion(Verificacion verificacion) {
        this.verificacion = verificacion;
    }
    

    //</editor-fold>
    //<editor-fold desc="Metodos Analisis">
    public void AgregarAnalisis(Analisis analisis) throws Exception {
        this.analisis.add(analisis);
        if (analisis != null && analisis.getMuestreo() != null && !analisis.getMuestreo().getId().equals(this.id)) {
            analisis.setMuestreo(this);
        }
    }

    public Analisis FindAnalisis(Long idAnalisis) {
        return this.getAnalisis().stream()
                .filter(a -> Objects.equals(a.getId(), idAnalisis))
                .findFirst()
                .get();
    }

  /**
   * Compara la cantidad de requisitos y analisis.
   * @return True si el numero de analisis es igual al de requisitos.
   */
    public boolean EstaAnalizado() {
        return this.muestra.getEspecificacionMuestra().getDestino().getRequisitos().size() == this.analisis.size();
    }

    //</editor-fold>
    //<editor-fold desc="Metodos Verificacion">
    public Verificacion AgregarVerificacion(Usuario verificador, EstadoVerificacion estado, String observaciones) {
        this.verificacion = new Verificacion(verificador, estado, observaciones, this);
        return this.verificacion;
    }

    private boolean calcularHabilitado() {
        if (this.verificacion == null) {
            return false;
        }
        if (this.verificacion.getResultado() == EstadoVerificacion.Incorrecto){
            return false;
        }
        return this.analisis.stream()
                    .allMatch(a -> a.getResultado().isAceptable());
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
        if (!(object instanceof Muestreo)) {
            return false;
        }
        Muestreo other = (Muestreo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "muestreo.Muestreo[ id=" + id + " ]";
    }

    @Override
    public int compareTo(Object o) {
        return -1*this.fechaMuestreo.compareTo(((Muestreo)o).getFechaMuestreo());
    }

}

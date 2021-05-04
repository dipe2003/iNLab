/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.microbiologia;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import modelo.usuarios.Usuario;
import modelo.muestreo.Muestreo;

/**
 *
 * @author dipe2
 */
@Entity
public class Analisis implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @Temporal(TemporalType.DATE)
    private Date fechaAnalisis;
    private String observaciones;
    
    @ManyToOne
    private Laboratorio laboratorio;

    @ManyToOne
    private Usuario usuarioAnalista;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private Resultado resultado;

    @ManyToOne
    private Requisito requisito;

    @ManyToOne
    private Muestreo muestreo;

    /*
        Constructores
     */
    public Analisis() {
    }

    public Analisis(Requisito requisito, Muestreo muestreo, Date fecha, String observaciones,
            Usuario analista, Laboratorio laboratorio) {
        this.muestreo = muestreo;
        this.fechaAnalisis = fecha;
        this.observaciones = observaciones;
        this.usuarioAnalista = analista;
        this.laboratorio=laboratorio;
        this.requisito=requisito;
    }

    //<editor-fold desc="Getters">
    public Long getId() {
        return id;
    }
    
    public Date getFechaAnalisis() {
        return this.fechaAnalisis;
    }

    public String getObservaciones() {
        return this.observaciones;
    }

    public Usuario getUsuarioAnalista() {
        return this.usuarioAnalista;
    }    

    public Resultado getResultado() {
        return resultado;
    }

    public Requisito getRequisito() {
        return requisito;
    }

    public Muestreo getMuestreo() {
        return muestreo;
    }

    public Laboratorio getLaboratorio() {
        return laboratorio;
    }
    
    //</editor-fold>
    //<editor-fold desc="Setters">
    public void setId(Long id) {
        this.id = id;
    }
    
    public void setFechaAnalisis(Date fecha) {
        this.fechaAnalisis = fecha;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public void setUsuarioAnalista(Usuario usuario) {
        this.usuarioAnalista = usuario;
    }
   

    public void setResultado(Resultado resultado) {
        this.resultado = resultado;
    }

    public void setRequisito(Requisito requisito) {
        this.requisito = requisito;
        if(requisito != null) requisito.AgregarAnalisis(this);
    }

    public void setMuestreo(Muestreo muestreo) {
        this.muestreo = muestreo;
    }

    public void setLaboratorio(Laboratorio laboratorio) {
        this.laboratorio = laboratorio;
    }

    
//</editor-fold>
    /*
        Metodos
     */
    
    /**
     * Crea un resultado de Busqueda.
     *
     * @param valor
     * @return
     * @throws Exception
     */
    public Resultado AgregarResultadoBusqueda(ValorDeteccion valor) throws Exception {
        if (esResultadoBusqueda()) {
            this.resultado = new ResultadoBusqueda(this, valor);
            return this.resultado;
        }
        throw new Exception("El requisito no corresponde a Busqueda.");
    }

    /**
     * Crea un resultado de Recuento.
     *
     * @param valor
     * @return
     * @throws Exception
     */
    public Resultado AgregarResultadoRecuento(float valor) throws Exception {
        if (!esResultadoBusqueda()) {
            this.resultado = new ResultadoRecuento(this, valor);
            return this.resultado;
        }
        throw new Exception("El requisito no corresponde a Recuento.");
    }

    private boolean esResultadoBusqueda() {
        return requisito.getLimiteVigente().getClass() == Busqueda.class;
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
        if (!(object instanceof Analisis)) {
            return false;
        }
        Analisis other = (Analisis) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "microbiologia.Analisis[ id=" + id + " ]";
    }
    
}

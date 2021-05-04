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
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import modelo.muestreo.Destino;

/**
 *
 * @author dipe2
 */
@Entity
public class Requisito implements Serializable, Comparable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    private Ensayo ensayo;

    @OneToMany(mappedBy = "requisito", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Limite> limites;

    @OneToMany(mappedBy = "requisito", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Analisis> analisisRequisito;

    @ManyToOne
    private Destino destino;

    /*
        Constructores
     */
    public Requisito() {
        this.limites = new ArrayList<>();
    }

    public Requisito(Ensayo ensayo, Destino destino) {
        this.ensayo = ensayo;
        this.destino = destino;
        this.limites = new ArrayList<>();
        this.analisisRequisito = new ArrayList<>();
    }

    //<editor-fold desc="Getters">
    public Long getId() {
        return id;
    }

    public Ensayo getEnsayo() {
        return ensayo;
    }

    public List<Limite> getLimites() {
        return this.limites;
    }

    public Destino getDestino() {
        return this.destino;
    }

    public List<Analisis> getAnalisisRequisito() {
        return analisisRequisito;
    }

    //</editor-fold>
    //<editor-fold desc="Setters">
    public void setId(Long id) {
        this.id = id;
    }

    public void setEnsayo(Ensayo ensayo) {
        this.ensayo = ensayo;
    }

    public void setLimites(List<Limite> limites) {
        this.limites = limites;
    }

    public void setDestino(Destino destino) {
        this.destino = destino;
    }

    public void setAnalisisRequisito(List<Analisis> analisisRequisito) {
        this.analisisRequisito = analisisRequisito;
    }
    //</editor-fold>

    /*
        Metodos
     */
    /**
     * Crea un nuevo limite de recuento y lo agrega a la lista de
     * limites.Cualquier limite existente queda obsoleto. El valor margina debe
     * ser menor que el valor inaceptable.
     *
     * @param marginal
     * @param inaceptable
     * @return
     * @throws java.lang.Exception
     */
    public Limite AgregarRecuento(float marginal, float inaceptable) throws Exception {
        if (!limites.isEmpty() && ContieneLimiteBusqueda()) {
            throw new Exception("No coincide el tipo de limite.");
        }
        if (marginal >= inaceptable) {
            throw new Exception("El valor marginal no puede ser mayor o igual al inaceptable");
        }
        Limite limite = new Recuento(marginal, inaceptable, this);
        DejarLimitesObsoletos();
        limites.add(limite);
        return limite;
    }

    /**
     * Crea un nuevo limite de busqueda y lo agrega a la lista de limites.
     * Cualquier limite existente queda obsoleto. Los valores inaceptable y
     * aceptable deben ser diferentes.
     *
     * @param valorInaceptable
     * @param valorAceptable
     * @return
     * @throws Exception
     */
    public Limite AgregarBusqueda(ValorDeteccion valorInaceptable, ValorDeteccion valorAceptable) throws Exception {
        if (!limites.isEmpty() && !ContieneLimiteBusqueda()) {
            throw new Exception("No coincide el tipo de limite.");
        }
        if (valorInaceptable == valorAceptable) {
            throw new Exception("Los valores aceptables e inaceptables no pueden ser iguales.");
        }
        Limite limite = new Busqueda(valorInaceptable, valorAceptable, this);
        DejarLimitesObsoletos();
        limites.add(limite);
        return limite;
    }

    private void DejarLimitesObsoletos() {
        limites.forEach((limite) -> {
            if (limite.getFechaObsoleto() != null) {
                limite.setFechaObsoleto(new Date());
            }
        });
    }

    public Limite getLimiteVigente() {
        return this.limites.stream()
                .filter(l -> l.getFechaObsoleto() == null)
                .findFirst()
                .orElse(null);
    }

    /**
     * Comprueba si hay limites de tipo busqueda.
     *
     * @return
     * @throws Exception
     */
    private boolean ContieneLimiteBusqueda() {
        return this.limites.stream()
                .anyMatch(l -> l.getClass().getSimpleName().equals("Busqueda"));
    }

    public void AgregarAnalisis(Analisis analisis) {
        this.analisisRequisito.add(analisis);
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
        if (!(object instanceof Requisito)) {
            return false;
        }
        Requisito other = (Requisito) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "microbiologia.Requisito[ id=" + id + " ]";
    }

    @Override
    public int compareTo(Object o) {
        return this.ensayo.compareTo(((Requisito)o).getEnsayo());
    }

}

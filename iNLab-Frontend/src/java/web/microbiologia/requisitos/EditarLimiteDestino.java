/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.microbiologia.requisitos;

import controladores.microbiologia.ControladorMicrobiologia;
import java.io.IOException;
import java.io.Serializable;
import javax.annotation.PostConstruct;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;

import javax.inject.Named;
import modelo.microbiologia.Busqueda;
import modelo.microbiologia.Ensayo;
import modelo.microbiologia.Limite;
import modelo.microbiologia.Recuento;
import modelo.microbiologia.Requisito;
import modelo.microbiologia.TipoLimite;
import modelo.microbiologia.ValorDeteccion;

/**
 *
 * @author dipe2
 */
@ViewScoped
@Named
public class EditarLimiteDestino implements Serializable {

    private ControladorMicrobiologia controladorMicrobiologia;
    private ExternalContext context;

    //<editor-fold desc="TipoRequisito">
    private String tipoSeleccionado;

    public String getTipoSeleccionado() {
        return tipoSeleccionado;
    }

    public void setTipoSeleccionado(String tipoSeleccionado) {
        this.tipoSeleccionado = tipoSeleccionado;
    }

    //</editor-fold>    
    //<editor-fold desc="Requisito">
    private Requisito requisito;

    public Requisito getRequisito() {
        return requisito;
    }

    public void setRequisito(Requisito requisito) {
        this.requisito = requisito;
    }

    //</editor-fold>
    //<editor-fold desc="Ensayo">
    private Ensayo ensayo;

    public Ensayo getEnsayo() {
        return ensayo;
    }

    public void setEnsayo(Ensayo ensayo) {
        this.ensayo = ensayo;
    }
    //</editor-fold>
    //<editor-fold desc="Valores de Limite Recuento">
    private float valorMarginal;
    private float valorInaceptable;

    public float getValorMarginal() {
        return valorMarginal;
    }

    public void setValorMarginal(float valorMarginal) {
        this.valorMarginal = valorMarginal;
    }

    public float getValorInaceptable() {
        return valorInaceptable;
    }

    public void setValorInaceptable(float valorInaceptable) {
        this.valorInaceptable = valorInaceptable;
    }

    //</editor-fold> 
    //<editor-fold desc="Valores de Limite Busqueda">
    private ValorDeteccion[] valoresBusquedaInaceptables = ValorDeteccion.values();
    private ValorDeteccion valorBusquedaInaceptableSeleccionado;

    private ValorDeteccion[] valoresBusquedaAceptables = ValorDeteccion.values();
    private ValorDeteccion valorBusquedaAceptableSeleccionado;

    public ValorDeteccion[] getValoresBusquedaInaceptables() {
        return valoresBusquedaInaceptables;
    }

    public void setValoresBusquedaInaceptables(ValorDeteccion[] valoresBusquedaInaceptables) {
        this.valoresBusquedaInaceptables = valoresBusquedaInaceptables;
    }

    public ValorDeteccion getValorBusquedaInaceptableSeleccionado() {
        return valorBusquedaInaceptableSeleccionado;
    }

    public void setValorBusquedaInaceptableSeleccionado(ValorDeteccion valorBusquedaInaceptableSeleccionado) {
        this.valorBusquedaInaceptableSeleccionado = valorBusquedaInaceptableSeleccionado;
    }

    public ValorDeteccion[] getValoresBusquedaAceptables() {
        return valoresBusquedaAceptables;
    }

    public void setValoresBusquedaAceptables(ValorDeteccion[] valoresBusquedaAceptables) {
        this.valoresBusquedaAceptables = valoresBusquedaAceptables;
    }

    public ValorDeteccion getValorBusquedaAceptableSeleccionado() {
        return valorBusquedaAceptableSeleccionado;
    }

    public void setValorBusquedaAceptableSeleccionado(ValorDeteccion valorBusquedaAceptableSeleccionado) {
        this.valorBusquedaAceptableSeleccionado = valorBusquedaAceptableSeleccionado;
    }

    //</editor-fold>    
    public void guardar() throws IOException {
        if ("Recuento".equals(tipoSeleccionado)) {
            if (controladorMicrobiologia.ActualizarLimiteRecuento(ensayo.getId(), requisito.getId(), valorMarginal, valorInaceptable) > 0) {
                redirigir();
            }
        } else {
            if (controladorMicrobiologia.ActualizarLimiteBusqueda(ensayo.getId(), requisito.getId(), valorBusquedaInaceptableSeleccionado, valorBusquedaAceptableSeleccionado) > 0) {
                redirigir();
            }
        }
    }

    private void redirigir() throws IOException {
        context.redirect(context.getRequestContextPath() + "/destinos/listadestinos.xhtml");
        FacesContext.getCurrentInstance().renderResponse();
        FacesContext.getCurrentInstance().responseComplete();
    }

    @PostConstruct
    public void init() {
        try {
            context = FacesContext.getCurrentInstance().getExternalContext();
            Long idEnsayo = 0L;
            Long idRequisito = 0L;

            idRequisito = Long.valueOf(context.getRequestParameterMap().get("idreq"));
            idEnsayo = Long.valueOf(context.getRequestParameterMap().get("idens"));
            if (idEnsayo != 0 && idRequisito != 0) {
                controladorMicrobiologia = new ControladorMicrobiologia();
                ensayo = controladorMicrobiologia.ObtenerEnsayo(idEnsayo);
                requisito = ensayo.FindRequisito(idRequisito);

                Limite limite = requisito.getLimiteVigente();
                tipoSeleccionado = limite.getTipoLimite().toString();
                if (limite.getTipoLimite() == TipoLimite.Busqueda) {
                    valorBusquedaAceptableSeleccionado = ((Busqueda) limite).getValorBusquedaAceptable();
                    valorBusquedaInaceptableSeleccionado = ((Busqueda) limite).getValorBusquedaInaceptable();
                } else {
                    valorInaceptable = ((Recuento) limite).getValorRecuentoInaceptable();
                    valorMarginal = ((Recuento) limite).getValorRecuentoMarginal();
                }
            }
        } catch (Exception ex) {
            //TODO: redirigir a error
        }
    }

}

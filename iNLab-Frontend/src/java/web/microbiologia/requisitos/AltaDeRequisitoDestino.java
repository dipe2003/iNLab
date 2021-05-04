/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.microbiologia.requisitos;

import controladores.microbiologia.ControladorDestinos;
import controladores.microbiologia.ControladorMicrobiologia;
import java.io.IOException;
import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import javax.annotation.PostConstruct;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;

import javax.inject.Named;
import modelo.microbiologia.Ensayo;
import modelo.microbiologia.Requisito;
import modelo.muestreo.Destino;

/**
 *
 * @author dipe2
 */
@ViewScoped
@Named
public class AltaDeRequisitoDestino implements Serializable {

    private ControladorMicrobiologia controladorMicrobiologia;
    private ExternalContext context;

    //<editor-fold desc="Destino">
    private Destino destino;

    public Destino getDestino() {
        return destino;
    }

    public void setDestino(Destino destino) {
        this.destino = destino;
    }

    //</editor-fold>    
    //<editor-fold desc="Ensayos">
    private Long ensayoSeleccionado;
    private List<Ensayo> ensayos;

    public Long getEnsayoSeleccionado() {
        return ensayoSeleccionado;
    }

    public void setEnsayoSeleccionado(Long ensayoSeleccionado) {
        this.ensayoSeleccionado = ensayoSeleccionado;
    }

    public List<Ensayo> getEnsayos() {
        return ensayos;
    }

    public void setEnsayos(List<Ensayo> ensayos) {
        this.ensayos = ensayos;
    }

    //</editor-fold>
    public void darAltaRequisito() throws IOException {
        if (controladorMicrobiologia.CrearRequisito(ensayoSeleccionado, destino.getId()) > 0) {
            if (context.isResponseCommitted()) {
                context = FacesContext.getCurrentInstance().getExternalContext();
            }
            context.redirect(context.getRequestContextPath() + "/destinos/listadestinos.xhtml");
            FacesContext.getCurrentInstance().renderResponse();
            FacesContext.getCurrentInstance().responseComplete();
        }
    }

    @PostConstruct
    public void init() {
        try {
            context = FacesContext.getCurrentInstance().getExternalContext();
            Long idDestino = 0L;
            idDestino = Long.valueOf(context.getRequestParameterMap().get("iddestino"));
            destino = new ControladorDestinos().ObtenerDestino(idDestino);

            controladorMicrobiologia = new ControladorMicrobiologia();
            ensayos = controladorMicrobiologia.ListarEnsayos();

            // quitar de la lista los ensayos que ya esten agregados.
            Iterator it = ensayos.iterator();
            Ensayo en = (Ensayo) it.next();
            do {
                for (Requisito requisito : destino.getRequisitos()) {
                    if (en.getId().equals(requisito.getEnsayo().getId())) {
                        it.remove();
                    }
                }
                en = (Ensayo) it.next();
            } while (it.hasNext());
            //dejar el primer ensayo de la lista seleccionado
            ensayoSeleccionado = ensayos.get(0).getId();
            // TODO: eliminar los ensayos que ya estan agregados.
        } catch (Exception ex) {
            //TODO: redirigir a error
        }
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.microbiologia.requisitos;

import controladores.microbiologia.ControladorMicrobiologia;
import controladores.muestreos.ControladorEspecificacionMuestras;
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
import modelo.muestreo.TipoMuestra;

/**
 *
 * @author dipe2
 */
@ViewScoped
@Named
public class AltaDeRequisito implements Serializable {

    private ControladorMicrobiologia controladorMicrobiologia;
    private ControladorEspecificacionMuestras controladorEspecificacionMuestras;
    private ExternalContext context;

    //<editor-fold desc="Destino">
    private Destino destinoEspec;

    public Destino getDestinoEspec() {
        return destinoEspec;
    }

    public void setDestinoEspec(Destino destinoEspec) {
        this.destinoEspec = destinoEspec;
    }    
    
    //</editor-fold>    
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

    private TipoMuestra tipoMuestra;

    public void darAltaRequisito() throws IOException {
        if (controladorMicrobiologia.CrearRequisito(ensayoSeleccionado, destinoEspec.getId()) > 0) {
            context.redirect(context.getRequestContextPath() + "/muestras/listaespecificacionesmuestras.xhtml?tipomuestra=Todas");
            FacesContext.getCurrentInstance().renderResponse();
            FacesContext.getCurrentInstance().responseComplete();
        }
    }

    @PostConstruct
    public void init() {
        controladorMicrobiologia = new ControladorMicrobiologia();
        ensayos = controladorMicrobiologia.ListarEnsayos();
        
        try {
            context = FacesContext.getCurrentInstance().getExternalContext();
            Long idEsp = 0L;            
            idEsp = Long.valueOf(context.getRequestParameterMap().get("idespec"));
            controladorEspecificacionMuestras = new ControladorEspecificacionMuestras();
            destinoEspec = controladorEspecificacionMuestras.ObtenerEspecificacionMuestra(idEsp).getDestino();
            // quitar de la lista los ensayos que ya esten agregados.
            Iterator it = ensayos.iterator();
            Ensayo en = (Ensayo) it.next();            
            do{
                for(Requisito requisito : destinoEspec.getRequisitos()){
                    if(en.getId().equals(requisito.getEnsayo().getId())){
                        it.remove();                        
                    }
                }
                en = (Ensayo) it.next(); 
            }while(it.hasNext());
        } catch (Exception ex) {
            //TODO: redirigir a error
        }
    }

}

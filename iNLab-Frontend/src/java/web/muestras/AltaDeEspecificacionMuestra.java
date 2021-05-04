/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.muestras;

import controladores.muestreos.ControladorAreas;
import controladores.microbiologia.ControladorDestinos;
import controladores.muestreos.ControladorEspecificacionMuestras;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;

import javax.inject.Named;
import modelo.muestreo.Area;
import modelo.muestreo.Destino;
import modelo.muestreo.TipoMuestra;

/**
 *
 * @author dipe2
 */
@ViewScoped
@Named
public class AltaDeEspecificacionMuestra implements Serializable {

    private ControladorAreas controladorAreas;
    private ControladorEspecificacionMuestras controladorEspecificacionMuestras;
    private ControladorDestinos controladorDestinos;
    private ExternalContext context;

    //<editor-fold desc="Muestra Padre">
    private Long areaSeleccionada;
    private List<Area> areas;

    private List<Destino> destinos;
    private Long destinoSeleccionado;

    private String denominacion;
    private String identificacion;

    private TipoMuestra[] tiposMuestra = TipoMuestra.values();
    private TipoMuestra tipoMuestraSeleccionada;

    public Long getAreaSeleccionada() {
        return areaSeleccionada;
    }

    public void setAreaSeleccionada(Long areaSeleccionada) {
        this.areaSeleccionada = areaSeleccionada;
    }

    public List<Area> getAreas() {
        return areas;
    }

    public void setAreas(List<Area> areas) {
        this.areas = areas;
    }

    public List<Destino> getDestinos() {
        return destinos;
    }

    public void setDestinos(List<Destino> destinos) {
        this.destinos = destinos;
    }

    public Long getDestinoSeleccionado() {
        return destinoSeleccionado;
    }

    public void setDestinoSeleccionado(Long destinoSeleccionado) {
        this.destinoSeleccionado = destinoSeleccionado;
    }

    public String getDenominacion() {
        return denominacion;
    }

    public void setDenominacion(String denominacion) {
        this.denominacion = denominacion;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    public TipoMuestra getTipoMuestraSeleccionada() {
        return tipoMuestraSeleccionada;
    }

    public void setTipoMuestraSeleccionada(TipoMuestra tipoMuestraSeleccionada) {
        this.tipoMuestraSeleccionada = tipoMuestraSeleccionada;
    }

    public TipoMuestra[] getTiposMuestra() {
        return tiposMuestra;
    }

    public void setTiposMuestra(TipoMuestra[] tiposMuestra) {
        this.tiposMuestra = tiposMuestra;
    }

    //</editor-fold>    
    public void darAltaMuestra() throws IOException {
        if (controladorEspecificacionMuestras.CrearEspecificacionMuestra(denominacion, identificacion, areaSeleccionada, tipoMuestraSeleccionada, destinoSeleccionado) > 0) {
            context.redirect("listaespecificacionesmuestras.xhtml?tipomuestra="+tipoMuestraSeleccionada);
            FacesContext.getCurrentInstance().renderResponse();
            FacesContext.getCurrentInstance().responseComplete();
        }
    }

    @PostConstruct
    public void init() {
        context = FacesContext.getCurrentInstance().getExternalContext();
        controladorDestinos = new ControladorDestinos();
        controladorAreas = new ControladorAreas();
        controladorEspecificacionMuestras = new ControladorEspecificacionMuestras();
        areas = controladorAreas.ListarAreas(true);
        areaSeleccionada = areas.get(0).getId();
        
        destinos = controladorDestinos.ListarDestinos();
        destinoSeleccionado = destinos.get(0).getId();
        
        tipoMuestraSeleccionada = tiposMuestra[0];
    }

}

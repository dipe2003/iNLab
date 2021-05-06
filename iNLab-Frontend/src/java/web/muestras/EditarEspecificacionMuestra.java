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
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;

import javax.inject.Named;
import modelo.muestreo.Area;
import modelo.muestreo.Destino;
import modelo.muestreo.EspecificacionMuestra;
import modelo.muestreo.TipoMuestra;
import web.helpers.MensajesWeb;

/**
 *
 * @author dipe2
 */
@ViewScoped
@Named
public class EditarEspecificacionMuestra implements Serializable {

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

    private TipoMuestra tipoMuestraSeleccionada;

    private EspecificacionMuestra especificacionEditando;

    //<editor-fold desc="Getters">
    public Long getAreaSeleccionada() {
        return areaSeleccionada;
    }

    public List<Area> getAreas() {
        return areas;
    }

    public List<Destino> getDestinos() {
        return destinos;
    }

    public Long getDestinoSeleccionado() {
        return destinoSeleccionado;
    }

    public String getDenominacion() {
        return denominacion;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public TipoMuestra getTipoMuestraSeleccionada() {
        return tipoMuestraSeleccionada;
    }

    public EspecificacionMuestra getEspecificacionEditando() {
        return especificacionEditando;
    }
    //</editor-fold>

    //<editor-fold desc="Setters">
    public void setAreaSeleccionada(Long areaSeleccionada) {
        this.areaSeleccionada = areaSeleccionada;
    }

    public void setAreas(List<Area> areas) {
        this.areas = areas;
    }

    public void setDestinos(List<Destino> destinos) {
        this.destinos = destinos;
    }

    public void setDestinoSeleccionado(Long destinoSeleccionado) {
        this.destinoSeleccionado = destinoSeleccionado;
    }

    public void setDenominacion(String denominacion) {
        this.denominacion = denominacion;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    public void setTipoMuestraSeleccionada(TipoMuestra tipoMuestraSeleccionada) {
        this.tipoMuestraSeleccionada = tipoMuestraSeleccionada;
    }

    public void setEspecificacionEditando(EspecificacionMuestra especificacionEditando) {
        this.especificacionEditando = especificacionEditando;
    }
    //</editor-fold>
    //</editor-fold>    

    public void guardar() throws IOException {
        if (cambioDenominacion() || cambioIdentificacion() || !especificacionEditando.getArea().getId().equals(areaSeleccionada)
                || !especificacionEditando.getDestino().getId().equals(destinoSeleccionado)) {
            if (controladorEspecificacionMuestras.ActualizarEspecificacionMuestra(especificacionEditando.getId(), denominacion, identificacion, areaSeleccionada, destinoSeleccionado) > 0) {
                redirigir();           
            }else{
                MensajesWeb.MostrarError("form-editar-especificacion-muestra:mensajes-vista", "No se dio guardaron los cambios:", "Contacta con el administrador.");
            }
        }       
    }

    private boolean cambioDenominacion() {
        if (especificacionEditando.getDenominacion() == null && denominacion != null) {
            return true;
        }
        if (especificacionEditando.getDenominacion() != null && denominacion != null) {
            if (!especificacionEditando.getDenominacion().equalsIgnoreCase(denominacion)) {
                return true;
            }
        }
        return false;
    }

    private boolean cambioIdentificacion() {
        if (especificacionEditando.getIdentificacion() == null && identificacion != null) {
            return true;
        }
        if (especificacionEditando.getIdentificacion() != null && identificacion != null) {
            if (!especificacionEditando.getIdentificacion().equalsIgnoreCase(identificacion)) {
                return true;
            }
        }
        return false;
    }

    public void darBaja() throws IOException {
        if (controladorEspecificacionMuestras.DarDeBajaEspecificacionMuestra(especificacionEditando.getId(), new Date()) > 0) {
           redirigir();
        }else {
            MensajesWeb.MostrarError("form-editar-especificacion-muestra:mensajes-vista", "No se dio Baja:", "Contacta con el administrador.");
        }
    }

    public void restaurar() throws IOException {
        if (controladorEspecificacionMuestras.DarDeAltaEspecificacionMuestra(especificacionEditando.getId()) > 0) {
            redirigir();
        }else {
            MensajesWeb.MostrarError("form-editar-especificacion-muestra:mensajes-vista", "No se dio Alta:", "Contacta con el administrador.");
        }
    }
    
    private void redirigir() throws IOException{
        context.redirect(context.getRequestContextPath() + "/muestras/listaespecificacionesmuestras.xhtml?tipomuestra=" + tipoMuestraSeleccionada);
        FacesContext.getCurrentInstance().renderResponse();
        FacesContext.getCurrentInstance().responseComplete();
    }

    @PostConstruct
    public void init() {
        try {
            context = FacesContext.getCurrentInstance().getExternalContext();
            Long idEsp = 0L;
            idEsp = Long.valueOf(context.getRequestParameterMap().get("idespec"));

            controladorDestinos = new ControladorDestinos();
            controladorAreas = new ControladorAreas();
            controladorEspecificacionMuestras = new ControladorEspecificacionMuestras();
            areas = controladorAreas.ListarAreas(true);
            destinos = controladorDestinos.ListarDestinos();

            especificacionEditando = controladorEspecificacionMuestras.ObtenerEspecificacionMuestra(idEsp);
            destinoSeleccionado = especificacionEditando.getDestino().getId();
            areaSeleccionada = especificacionEditando.getArea().getId();

            denominacion = especificacionEditando.getDenominacion();
            identificacion = especificacionEditando.getIdentificacion();
            tipoMuestraSeleccionada = especificacionEditando.getTipoMuestra();

        } catch (Exception ex) {
            //TODO: redirigir a error
        }
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.microbiologia.analisis;

import controladores.microbiologia.ControladorMicrobiologia;
import controladores.muestreos.ControladorMuestreos;
import java.io.IOException;
import java.io.Serializable;
import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;

import javax.inject.Named;
import modelo.microbiologia.EstadoVerificacion;
import modelo.muestreo.Muestreo;
import web.helpers.MensajesWeb;
import web.sesion.SesionDeUsuario;

/**
 *
 * @author dipe2
 */
@ViewScoped
@Named
@ManagedBean
public class AltaDeVerificacion implements Serializable {

    private ControladorMicrobiologia controladorMicrobiologia;
    private ControladorMuestreos controladorMuestreos;
    
    private ExternalContext context;

    @Inject
    private SesionDeUsuario sesion;

    //<editor-fold desc="Verificacion">
    private String observaciones;
    private EstadoVerificacion estadoVerificacion;

    private EstadoVerificacion[] estadosVerificacion = EstadoVerificacion.values();

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public EstadoVerificacion getEstadoVerificacion() {
        return estadoVerificacion;
    }

    public void setEstadoVerificacion(EstadoVerificacion estadoVerificacion) {
        this.estadoVerificacion = estadoVerificacion;
    }

    public EstadoVerificacion[] getEstadosVerificacion() {
        return estadosVerificacion;
    }

    public void setEstadosVerificacion(EstadoVerificacion[] estadosVerificacion) {
        this.estadosVerificacion = estadosVerificacion;
    }

    //</editor-fold>
    //<editor-fold desc="Datos del Muestreo">
    private Muestreo muestreo;

    public Muestreo getMuestreo() {
        return muestreo;
    }

    public void setMuestreo(Muestreo muestreo) {
        this.muestreo = muestreo;
    }
    //</editor-fold>

    public void darAltaVerificacion() throws IOException {
        if (controladorMicrobiologia.AgregarVerificacion(muestreo.getId(), sesion.getUsuarioLogueado().getId(), observaciones, estadoVerificacion) > 0) {
            context.redirect(context.getRequestContextPath() + "/muestreos/listamuestreosconresultados.xhtml?tipomuestra="
                    + muestreo.getMuestra().getEspecificacionMuestra().getTipoMuestra());
            FacesContext.getCurrentInstance().renderResponse();
            FacesContext.getCurrentInstance().responseComplete();
        } else {
             MensajesWeb.MostrarError("form-alta-verificacion:mensajes-vista", "No se pudo guardar.", "Verifica los datos ingresados o contacta con el administrador.");
        }
    }

    @PostConstruct
    public void init() {
        try {
            context = FacesContext.getCurrentInstance().getExternalContext();
            Long idMuestreo = 0L;            
            idMuestreo = Long.valueOf(context.getRequestParameterMap().get("idmuestreo"));
            if (idMuestreo != 0) {
                controladorMicrobiologia = new ControladorMicrobiologia();
                controladorMuestreos = new ControladorMuestreos();
                muestreo = controladorMuestreos.ObtenerMuestreo(idMuestreo);
                estadoVerificacion = estadosVerificacion[0];

                // solo si el usuario logueado es analista.
                // de lo contrario se debe seleccionar el analista de la lista.
//                if (sesion.getUsuarioLogueado().getTipo() == Permiso.Analista) {
//                    idUsuarioSeleccionado = sesion.getUsuarioLogueado().getId();
//                }
            }
        } catch (Exception ex) {
            //TODO: redirigir a error
        }
    }
}

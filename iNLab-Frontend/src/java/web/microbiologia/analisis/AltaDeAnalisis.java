/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.microbiologia.analisis;

import controladores.microbiologia.ControladorAnalisis;
import controladores.microbiologia.ControladorLaboratorios;
import controladores.muestreos.ControladorMuestreos;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;

import javax.inject.Named;
import modelo.microbiologia.Laboratorio;
import modelo.microbiologia.Requisito;
import modelo.muestreo.Muestreo;
import modelo.usuarios.Usuario;

/**
 *
 * @author dipe2
 */
@ViewScoped
@Named
public class AltaDeAnalisis implements Serializable {

    private ControladorMuestreos controladorMuestreos;
    private ControladorAnalisis controladorAnalisis;

    private ExternalContext context;

    //<editor-fold desc="Analisis">  
    private Date fechaAnalisis;
    private String observaciones;

    private Usuario usuarioAnalista;
    private List<Usuario> usuariosAnalistas;

    private Muestreo muestreoSeleccionado;

    private List<Laboratorio> laboratorios;
    private Long laboratorioSeleccionado;

    public Date getFechaAnalisis() {
        return fechaAnalisis;
    }

    public void setFechaAnalisis(Date fechaAnalisis) {
        this.fechaAnalisis = fechaAnalisis;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Usuario getUsuarioAnalista() {
        return usuarioAnalista;
    }

    public void setUsuarioAnalista(Usuario usuarioAnalista) {
        this.usuarioAnalista = usuarioAnalista;
    }

    public List<Usuario> getUsuariosAnalistas() {
        return usuariosAnalistas;
    }

    public void setUsuariosAnalistas(List<Usuario> usuariosAnalistas) {
        this.usuariosAnalistas = usuariosAnalistas;
    }

    public Muestreo getMuestreoSeleccionado() {
        return muestreoSeleccionado;
    }

    public void setMuestreoSeleccionado(Muestreo muestreoSeleccionado) {
        this.muestreoSeleccionado = muestreoSeleccionado;
    }

    public List<Laboratorio> getLaboratorios() {
        return laboratorios;
    }

    public void setLaboratorios(List<Laboratorio> laboratorios) {
        this.laboratorios = laboratorios;
    }

    public Long getLaboratorioSeleccionado() {
        return laboratorioSeleccionado;
    }

    public void setLaboratorioSeleccionado(Long laboratorioSeleccionado) {
        this.laboratorioSeleccionado = laboratorioSeleccionado;
    }

    //</editor-fold>
    //<editor-fold desc="Requisitos">
    private Requisito requisitoSeleccionado;

    private String tipoSeleccionado;

    public String getTipoSeleccionado() {
        return tipoSeleccionado;
    }

    public void setTipoSeleccionado(String tipoSeleccionado) {
        this.tipoSeleccionado = tipoSeleccionado;
    }
    //</editor-fold>

    public void darAltaAnalisis() throws IOException {
        if (controladorAnalisis.CrearAnalisis(requisitoSeleccionado.getId(), muestreoSeleccionado.getId(), fechaAnalisis,
                observaciones, usuarioAnalista.getId(), laboratorioSeleccionado) > 0) {
            redirigir();
        }
        //TODO: agregar opcion si no se crea
    }

    private void redirigir() throws IOException {
        context.redirect(context.getRequestContextPath() + "/muestreos/listamuestreosconresultados.xhtml");
        FacesContext.getCurrentInstance().renderResponse();
        FacesContext.getCurrentInstance().responseComplete();
    }

    @PostConstruct
    public void init() {
        try {
            context = FacesContext.getCurrentInstance().getExternalContext();
            Long idMuestreo = 0L;
            idMuestreo = Long.valueOf(context.getRequestParameterMap().get("idmuestreo"));
            if (idMuestreo != 0) {
                controladorAnalisis = new ControladorAnalisis();
                muestreoSeleccionado = controladorMuestreos.ObtenerMuestreo(idMuestreo);
                laboratorios = new ControladorLaboratorios().ListarLaboratorios();
            }
        } catch (Exception ex) {
            //TODO: redirigir a error
        }
    }

}

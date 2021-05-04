/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.muestras;

import controladores.muestreos.ControladorEspecificacionMuestras;
import controladores.muestreos.ControladorMuestras;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorListener;
import javax.faces.view.ViewScoped;

import javax.inject.Named;
import modelo.muestreo.EspecificacionMuestra;
import modelo.muestreo.TipoMuestra;

/**
 *
 * @author dipe2
 */
@ViewScoped
@Named
public class AltaDeMuestra implements Serializable {

    private ControladorMuestras controladorMuestras;
    private ControladorEspecificacionMuestras controladorEspecificacionMuestras;
    private ExternalContext context;

    //<editor-fold desc="Muestra Padre">
    private Long idEspecificacionSeleccionada;
    private List<EspecificacionMuestra> especificacionesDisponibles;
    private List<EspecificacionMuestra> especificacionesFiltradas;

    private TipoMuestra[] tiposMuestra = TipoMuestra.values();
    private TipoMuestra tipoMuestraSeleccionada;

    public Long getIdEspecificacionSeleccionada() {
        return idEspecificacionSeleccionada;
    }

    public void setIdEspecificacionSeleccionada(Long idEspecificacionSeleccionada) {
        this.idEspecificacionSeleccionada = idEspecificacionSeleccionada;
    }

    public List<EspecificacionMuestra> getEspecificacionesFiltradas() {
        return especificacionesFiltradas;
    }

    public void setEspecificacionesFiltradas(List<EspecificacionMuestra> especificacionesFiltradas) {
        this.especificacionesFiltradas = especificacionesFiltradas;
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
    //<editor-fold desc="Producto">
    private Date fechaOrigen;
    private Date fechaProduccion;
    private String lote;

    public Date getFechaOrigen() {
        return fechaOrigen;
    }

    public void setFechaOrigen(Date fechaOrigen) {
        this.fechaOrigen = fechaOrigen;
    }

    public Date getFechaProduccion() {
        return fechaProduccion;
    }

    public void setFechaProduccion(Date fechaProduccion) {
        this.fechaProduccion = fechaProduccion;
    }

    public String getLote() {
        return lote;
    }

    public void setLote(String lote) {
        this.lote = lote;
    }

    //</editor-fold>
    //<editor-fold desc="Operario">
    private int padron;
    private String nombre;

    public int getPadron() {
        return padron;
    }

    public void setPadron(int padron) {
        this.padron = padron;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    //</editor-fold>
    //<editor-fold desc="Ambiente">
    private boolean enContactoProducto;

    public boolean isEnContactoProducto() {
        return enContactoProducto;
    }

    public void setEnContactoProducto(boolean enContactoProducto) {
        this.enContactoProducto = enContactoProducto;
    }

    //</editor-fold>
    public void darAltaMuestra() throws IOException {
        switch (tipoMuestraSeleccionada) {
            case Producto:
                if (DarAltaMuestraProducto()) {
                    context.redirect(context.getRequestContextPath() +"/muestras/listamuestrasproducto.xhtml");
                }

            case Ambiente:
                if (DarAltaMuestraAmbiente()) {
                    context.redirect(context.getRequestContextPath() +"/muestras/listamuestrasambiente.xhtml");
                }
                
            case Operario:
                if (DarAltaMuestraOperario()) {
                    context.redirect(context.getRequestContextPath() +"/muestras/listamuestrasoperario.xhtml");
                }
                
            case Otra:
                if (DarAltaMuestraOtra()) {
                    context.redirect(context.getRequestContextPath() + "/muestras/listamuestrasotra.xhtml");
                }
                
        }
        // TODO: se deberia mostrar un mensaje
        context.redirect("../index.xhtml");

    }

    public boolean DarAltaMuestraProducto() {
        if (lote.isEmpty()) {
            if (controladorMuestras.CrearMuestraProducto(idEspecificacionSeleccionada, fechaOrigen, fechaProduccion) > 0) {
                return true;
            }
        } else {
            if (controladorMuestras.CrearMuestraProducto(idEspecificacionSeleccionada, fechaOrigen, fechaProduccion, lote) > 0) {
                return true;
            }
        }
        return false;
    }

    public boolean DarAltaMuestraOperario() {
        if (controladorMuestras.CrearMuestraOperario(idEspecificacionSeleccionada, nombre, padron) > 0) {
            return true;
        }
        return false;
    }

    public boolean DarAltaMuestraAmbiente() {
        if (controladorMuestras.CrearMuestraAmbiente(idEspecificacionSeleccionada, enContactoProducto) > 0) {
            return true;
        }
        return false;
    }

    public boolean DarAltaMuestraOtra() {
        if (controladorMuestras.CrearMuestraOtra(idEspecificacionSeleccionada) > 0) {
            return true;
        }
        return false;
    }

    public void FiltrarEspecificaciones(AjaxBehaviorListener event) {
        especificacionesFiltradas = especificacionesDisponibles.stream()
                .filter(esp -> esp.getTipoMuestra() == tipoMuestraSeleccionada)
                .collect(Collectors.toList());
    }

    @PostConstruct
    public void init() {
        context = FacesContext.getCurrentInstance().getExternalContext();
        controladorMuestras = new ControladorMuestras();
        controladorEspecificacionMuestras = new ControladorEspecificacionMuestras();
        especificacionesDisponibles = controladorEspecificacionMuestras.ListarEspecificacionMuestras(true);
        tipoMuestraSeleccionada = TipoMuestra.Producto;
        FiltrarEspecificaciones(null);
    }

}

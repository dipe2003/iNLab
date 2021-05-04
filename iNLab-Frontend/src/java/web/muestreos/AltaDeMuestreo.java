/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.muestreos;

import controladores.muestreos.ControladorAreas;
import controladores.muestreos.ControladorMuestreos;
import controladores.usuario.ControladorUsuarios;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;

import javax.faces.view.ViewScoped;

import javax.inject.Named;
import modelo.muestreo.Almacenamiento;
import modelo.muestreo.Area;
import modelo.muestreo.EspecificacionMuestra;
import modelo.muestreo.TipoMuestra;
import modelo.usuarios.Usuario;
import web.helpers.MensajesWeb;

/**
 *
 * @author dipe2
 */
@ViewScoped
@Named
public class AltaDeMuestreo implements Serializable {

    private ControladorMuestreos controladorMuestreos;
    private ControladorAreas controladorAreas;
    private ControladorUsuarios controladorUsuarios;
    private ExternalContext context;

    //<editor-fold desc="Muestreo">  
    private Date fechaMuestreo;
    private boolean esRepeticion;
    private String observaciones;

    private Almacenamiento[] almacenamientos = Almacenamiento.values();
    private Almacenamiento almacenamientoSeleccionado;

    public Almacenamiento[] getAlmacenamientos() {
        return almacenamientos;
    }

    public void setAlmacenamientos(Almacenamiento[] almacenamientos) {
        this.almacenamientos = almacenamientos;
    }

    public Almacenamiento getAlmacenamientoSeleccionado() {
        return almacenamientoSeleccionado;
    }

    public void setAlmacenamientoSeleccionado(Almacenamiento almacenamientoSeleccionado) {
        this.almacenamientoSeleccionado = almacenamientoSeleccionado;
    }

    public Date getFechaMuestreo() {
        return fechaMuestreo;
    }

    public void setFechaMuestreo(Date fechaMuestreo) {
        this.fechaMuestreo = fechaMuestreo;
    }

    public boolean isEsRepeticion() {
        return esRepeticion;
    }

    public void setEsRepeticion(boolean esRepeticion) {
        this.esRepeticion = esRepeticion;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    //</editor-fold>
    //<editor-fold desc="Especificacion de Muestra">
    private TipoMuestra tipoMuestraSeleccionado;
    private List<EspecificacionMuestra> especificacionesMuestra;
    private Long idEspecificacionMuestraSeleccionada;

    public List<EspecificacionMuestra> getEspecificacionesMuestra() {
        return especificacionesMuestra;
    }

    public void setEspecificacionesMuestra(List<EspecificacionMuestra> especificacionesMuestra) {
        this.especificacionesMuestra = especificacionesMuestra;
    }

    public Long getIdEspecificacionMuestraSeleccionada() {
        return idEspecificacionMuestraSeleccionada;
    }

    public void setIdEspecificacionMuestraSeleccionada(Long idEspecificacionMuestraSeleccionada) {
        this.idEspecificacionMuestraSeleccionada = idEspecificacionMuestraSeleccionada;
    }

    public void filtrarMuestraArea(AjaxBehaviorEvent event) {
        areaSeleccionada = areas.stream().filter(a -> Objects.equals(a.getId(), idAreaSeleccionada)).findFirst().get();
        especificacionesMuestra = areaSeleccionada.getEspecificacionesMuestra().stream()
                .filter(esp -> esp.getTipoMuestra() == tipoMuestraSeleccionado)
                .collect(Collectors.toList());
        if (!especificacionesMuestra.isEmpty()) {
            idEspecificacionMuestraSeleccionada = especificacionesMuestra.get(0).getId();
        }
    }

    public TipoMuestra getTipoMuestraSeleccionado() {
        return tipoMuestraSeleccionado;
    }

    //</editor-fold>
    //<editor-fold desc="Muestras">
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
    //</editor-fold>
    //<editor-fold desc="Areas">
    private List<Area> areas;
    private Area areaSeleccionada;
    private Long idAreaSeleccionada;

    public List<Area> getAreas() {
        return areas;
    }

    public void setAreas(List<Area> areas) {
        this.areas = areas;
    }

    public Area getAreaSeleccionada() {
        return areaSeleccionada;
    }

    public void setAreaSeleccionada(Area areaSeleccionada) {
        this.areaSeleccionada = areaSeleccionada;
    }

    public Long getIdAreaSeleccionada() {
        return idAreaSeleccionada;
    }

    public void setIdAreaSeleccionada(Long idAreaSeleccionada) {
        this.idAreaSeleccionada = idAreaSeleccionada;
    }

    //</editor-fold>
    //<editor-fold desc="Usuarios">
    private List<Usuario> usuarios;
    private Usuario usuarioSeleccionado;
    private Long idUsuarioSeleccionado;

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

    public Usuario getUsuarioSeleccionado() {
        return usuarioSeleccionado;
    }

    public void setUsuarioSeleccionado(Usuario usuarioSeleccionado) {
        this.usuarioSeleccionado = usuarioSeleccionado;
    }

    public Long getIdUsuarioSeleccionado() {
        return idUsuarioSeleccionado;
    }

    public void setIdUsuarioSeleccionado(Long idUsuarioSeleccionado) {
        this.idUsuarioSeleccionado = idUsuarioSeleccionado;
    }

    //</editor-fold>
    public void darAltaMuestreo() throws IOException {
        Long idMuestreo = 0L;
        switch (tipoMuestraSeleccionado) {
            case Producto:
                if (lote == null || lote.isEmpty()) {
                    if (comprobarDatosMuestreo() && comprobarDatosMuestreoProducto()) {
                        idMuestreo = controladorMuestreos.CrearMuestreoProducto(fechaMuestreo, observaciones, esRepeticion, almacenamientoSeleccionado,
                                idUsuarioSeleccionado, idEspecificacionMuestraSeleccionada, fechaOrigen, fechaProduccion);
                    }
                    break;
                }
                if (comprobarDatosMuestreo() && comprobarDatosMuestreoProducto()) {
                    idMuestreo = controladorMuestreos.CrearMuestreoProducto(fechaMuestreo, observaciones, esRepeticion, almacenamientoSeleccionado,
                            idUsuarioSeleccionado, idEspecificacionMuestraSeleccionada, fechaOrigen, fechaProduccion, lote);
                }
                break;

            case Ambiente:
                if (comprobarDatosMuestreo()) {
                    idMuestreo = controladorMuestreos.CrearMuestreoAmbiente(fechaMuestreo, observaciones, esRepeticion, almacenamientoSeleccionado,
                            idUsuarioSeleccionado, idEspecificacionMuestraSeleccionada, enContactoProducto);
                }
                break;

            case Operario:
                if (comprobarDatosMuestreo() && comprobarDatosMuestreoOperario()) {
                    idMuestreo = controladorMuestreos.CrearMuestreoOperario(fechaMuestreo, observaciones, esRepeticion, almacenamientoSeleccionado,
                            idUsuarioSeleccionado, idEspecificacionMuestraSeleccionada, nombre, padron);
                }
                break;

            default:
                if (comprobarDatosMuestreo()) {
                    idMuestreo = controladorMuestreos.CrearMuestreoOtraMuestra(fechaMuestreo, observaciones, esRepeticion, almacenamientoSeleccionado,
                            idUsuarioSeleccionado, idEspecificacionMuestraSeleccionada);
                }
                break;
        }
        if (idMuestreo > 0) {
            redirigir();
        }else{
             MensajesWeb.MostrarError("form-alta-muestreo:botonAltaMuestreo", "Error: ", "El muestreo no se pudo crear, verifica los datos ingresados o contacta con el administrador.");
        }
    }

    private boolean comprobarDatosMuestreo() {
        boolean ok = true;
        if (fechaMuestreo == null) {
            MensajesWeb.MostrarError("form-alta-muestreo:fecha-muestreo", "Error: ", "La fecha de muestreo es un dato obligatorio.");
            ok = false;
        }

        if (idUsuarioSeleccionado == null || idUsuarioSeleccionado.equals(0)) {
            MensajesWeb.MostrarError("form-alta-muestreo:select-usuario", "Error: ", "No se selecciono Monitor.");
            ok = false;
        }

        if (idEspecificacionMuestraSeleccionada == null || idEspecificacionMuestraSeleccionada.equals(0)) {
            MensajesWeb.MostrarError("form-alta-muestreo:select-especificacion", "Error: ", "No se selecciono Muestra.");
            ok = false;
        }
        return ok;
    }

    private boolean comprobarDatosMuestreoProducto() {
        boolean ok = true;

        if (fechaOrigen == null) {
            MensajesWeb.MostrarError("form-alta-muestreo:fecha-origen", "Error: ", "La fecha de Faena es un dato obligatorio.");
            ok = false;
        }

        if (fechaProduccion == null) {
            MensajesWeb.MostrarError("form-alta-muestreo:fecha-produccion", "Error: ", "La fecha de Produccion es un dato obligatorio.");
            ok = false;
        }

        if (fechaMuestreo != null && fechaOrigen != null && fechaProduccion != null) {
            if (fechaMuestreo.before(fechaOrigen) || fechaMuestreo.before(fechaProduccion)) {
                MensajesWeb.MostrarError("form-alta-muestreo:fecha-muestreo", "Error: ", "La fecha de muestreo no puede ser posterior a la fecha del producto.");
                ok = false;
            }
        }

        return ok;
    }

    private boolean comprobarDatosMuestreoOperario() {
        boolean ok = true;
        if (nombre.isEmpty() || nombre == null) {
            MensajesWeb.MostrarError("form-alta-muestreo:input-nombre-operario", "Error: ", "El nombre del operario muestreado es un dato obligatorio.");
            ok = false;
        }

        if (padron < 1 ) {
            MensajesWeb.MostrarError("form-alta-muestreo:input-padron-operario", "Error: ", "El paron o identificacion del operario muestreado es un dato obligatorio.");
            ok = false;
        }
        return ok;
    }

    private void redirigir() throws IOException {
        context.redirect(context.getRequestContextPath() + "/muestreos/listamuestreosconresultados.xhtml?tipomuestra=" + tipoMuestraSeleccionado);
        FacesContext.getCurrentInstance().renderResponse();
        FacesContext.getCurrentInstance().responseComplete();
    }

    @PostConstruct
    public void init() {
        try {
            context = FacesContext.getCurrentInstance().getExternalContext();
            tipoMuestraSeleccionado = TipoMuestra.valueOf(context.getRequestParameterMap().get("tipomuestra"));
            controladorMuestreos = new ControladorMuestreos();
            controladorAreas = new ControladorAreas();
            areas = controladorAreas.ListarAreas(true);
            
            controladorUsuarios = new ControladorUsuarios();
            usuarios = controladorUsuarios.ListarUsuarios(true);
            
            especificacionesMuestra = new ArrayList<>();
            almacenamientoSeleccionado = almacenamientos[0];
            
        } catch (Exception ex) {
            //TODO: redirigir a error
        }
    }

}

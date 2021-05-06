/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.muestreos;

import controladores.microbiologia.ControladorAnalisis;
import controladores.muestreos.ControladorAreas;
import controladores.muestreos.ControladorMuestreos;
import controladores.usuario.ControladorUsuarios;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;

import javax.faces.view.ViewScoped;

import javax.inject.Named;
import modelo.microbiologia.Analisis;
import modelo.microbiologia.Busqueda;
import modelo.microbiologia.Recuento;
import modelo.microbiologia.Requisito;
import modelo.microbiologia.ResultadoBusqueda;
import modelo.microbiologia.ResultadoRecuento;

import modelo.muestreo.Almacenamiento;
import modelo.muestreo.Area;
import modelo.muestreo.EspecificacionMuestra;
import modelo.muestreo.MuestraAmbiente;
import modelo.muestreo.MuestraOperario;
import modelo.muestreo.MuestraProducto;
import modelo.muestreo.Muestreo;
import modelo.muestreo.TipoMuestra;
import modelo.usuarios.Usuario;
import revision.controladores.muestreos.ControladorRevMuestreos;
import revision.controladores.muestreos.ControladorRevResultados;
import revision.modelo.microbiologia.ValorDeteccion;
import web.helpers.MensajesWeb;

/**
 *
 * @author dipe2
 */
@ViewScoped
@Named
public class EditarMuestreo implements Serializable {

    private ControladorMuestreos controladorMuestreos;
    private ControladorAreas controladorAreas;
    private ControladorUsuarios controladorUsuarios;
    private ControladorRevMuestreos controladorRevMuestreos;
    private ControladorRevResultados controladorRevResultados;
    private ControladorAnalisis controladorAnalisis;

    private FacesContext facesContext = FacesContext.getCurrentInstance();
    private ExternalContext context = facesContext.getExternalContext();

    //<editor-fold desc="Muestreo Editado">
    private Muestreo muestreo;

    public Muestreo getMuestreo() {
        return muestreo;
    }

    public void setMuestreo(Muestreo muestreo) {
        this.muestreo = muestreo;
    }

    private boolean esInicial = true;
    //</editor-fold>
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
    private EspecificacionMuestra especificacionSeleccionada;

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
        especificacionSeleccionada = especificacionesMuestra.stream()
                .filter(esp -> esp.getId().equals(idEspecificacionMuestraSeleccionada))
                .findFirst()
                .get();
    }

    public void filtrarMuestraArea(AjaxBehaviorEvent event) {
        areaSeleccionada = areas.stream()
                .filter(a -> Objects.equals(a.getId(), idAreaSeleccionada))
                .findFirst()
                .get();
        especificacionesMuestra = areaSeleccionada.getEspecificacionesMuestra().stream()
                .filter(esp -> esp.getTipoMuestra() == tipoMuestraSeleccionado)
                .collect(Collectors.toList());
        if (!esInicial) {
            if (!especificacionesMuestra.isEmpty()) {
                idEspecificacionMuestraSeleccionada = especificacionesMuestra.get(0).getId();
                especificacionSeleccionada = especificacionesMuestra.get(0);
            }
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
    public void guardar() {
        try {
            if (datosCambiados()) {
                switch (tipoMuestraSeleccionado) {
                    case Producto:
                        if (lote == null || lote.isEmpty()) {
                            if (comprobarDatosMuestreo() && comprobarDatosMuestreoProducto()) {
                                controladorMuestreos.ActualizarMuestreoProducto(muestreo.getId(), fechaMuestreo, observaciones, esRepeticion, almacenamientoSeleccionado,
                                        idUsuarioSeleccionado, fechaOrigen, fechaProduccion);
                                envioRevisiones(muestreo);
                                redirigir();
                            }
                        } else {
                            if (comprobarDatosMuestreo() && comprobarDatosMuestreoProducto()) {
                                controladorMuestreos.ActualizarMuestreoProducto(muestreo.getId(), fechaMuestreo, observaciones, esRepeticion, almacenamientoSeleccionado,
                                        idUsuarioSeleccionado, fechaOrigen, fechaProduccion, lote);
                                envioRevisiones(muestreo);
                                redirigir();
                            }
                        }
                        break;

                    case Ambiente:
                        if (comprobarDatosMuestreo()) {
                            controladorMuestreos.ActualizarMuestreoAmbiente(muestreo.getId(), fechaMuestreo, observaciones, esRepeticion, almacenamientoSeleccionado,
                                    idUsuarioSeleccionado, enContactoProducto);
                            envioRevisiones(muestreo);
                            redirigir();
                        }
                        break;

                    case Operario:
                        if (comprobarDatosMuestreo() && comprobarDatosMuestreoOperario()) {
                            controladorMuestreos.ActualizarMuestreoOperario(muestreo.getId(), fechaMuestreo, observaciones, esRepeticion, almacenamientoSeleccionado,
                                    idUsuarioSeleccionado, nombre, padron);
                            envioRevisiones(muestreo);
                            redirigir();
                        }
                        break;

                    default:
                        if (comprobarDatosMuestreo()) {
                            controladorMuestreos.ActualizarMuestreo(muestreo.getId(), fechaMuestreo, observaciones, esRepeticion, almacenamientoSeleccionado,
                                    idUsuarioSeleccionado);
                            envioRevisiones(muestreo);
                            redirigir();
                        }
                        break;
                }
            }
        } catch (Exception e) {
            System.out.println("Error al guaradar ensayo y resultado en revision: " + e.getMessage());
            MensajesWeb.MostrarError("form-editar-muestreo:mensajes-vista", "Error: ", "El muestreo no se pudo crear, verifica los datos ingresados o contacta con el administrador.");
        }
    }

    private void envioRevisiones(Muestreo muestreo) {
        if (!muestreo.getMuestra().getEspecificacionMuestra().getId().equals(idEspecificacionMuestraSeleccionada)) {
            // si el destino no cambio los analisis son los mismos, de lo contrario se deben verificar
            if (!especificacionSeleccionada.getDestino().getId().equals(muestreo.getMuestra().getEspecificacionMuestra().getDestino().getId())) {
                // guardar los analisis en caso que los realizados no apliquen a la nueva especificacion
                List<Analisis> analisisGuardados = muestreo.getAnalisis();
                List<Analisis> analisisNoCoincidentes = tratarAnalisis(analisisGuardados);

                for (Analisis a : analisisGuardados) {
                    controladorAnalisis.ActualizarRequisitoAnalisis(a.getId(), a.getRequisito().getId(), muestreo.getId());
                }

                if (analisisNoCoincidentes.size() > 0) {
                    manejarRevMuestreo();
                    manejarAnalisisNoCoincidentes(analisisNoCoincidentes);
                }
            }
            controladorMuestreos.ActualizarMuestreo(muestreo.getId(), idEspecificacionMuestraSeleccionada);
        }
    }

    private void manejarRevMuestreo() {
        if (controladorRevMuestreos.ObtenerRevMuestreo(muestreo.getId()) == null) {
            switch (tipoMuestraSeleccionado) {
                case Producto:
                    controladorRevMuestreos.CrearMuestreoProducto(muestreo.getId(), muestreo.getMuestra().getEspecificacionMuestra().getDenominacion(),
                            muestreo.getMuestra().getEspecificacionMuestra().getDestino().getDenominacion(), muestreo.getUsuarioMonitor().getNombre(),
                            muestreo.getObservaciones(), muestreo.getMuestra().getEspecificacionMuestra().getArea().getNombre(), fechaMuestreo,
                            fechaProduccion, fechaOrigen, esRepeticion);
                    break;

                default:
                    controladorRevMuestreos.CrearMuestreoGenerica(muestreo.getId(), muestreo.getMuestra().getEspecificacionMuestra().getDenominacion(),
                            muestreo.getMuestra().getEspecificacionMuestra().getDestino().getDenominacion(), muestreo.getUsuarioMonitor().getNombre(),
                            muestreo.getObservaciones(), muestreo.getMuestra().getEspecificacionMuestra().getArea().getNombre(), fechaMuestreo,
                            esRepeticion, enContactoProducto);
                    break;
            }
        }
    }

    private void manejarAnalisisNoCoincidentes(List<Analisis> analisisNoCoincidentes) {
        for (Analisis a : analisisNoCoincidentes) {
            if (a.getResultado().getClass().getSimpleName().equalsIgnoreCase("ResultadoRecuento")) {
                controladorRevResultados.AgregarResultadoRecuento(a.getId(), muestreo.getId(), a.getRequisito().getEnsayo().getDenominacion(), a.getUsuarioAnalista().getNombre(),
                        a.getLaboratorio().getNombre(), a.getObservaciones(), ((ResultadoRecuento) a.getResultado()).getValor(),
                        ((Recuento) a.getRequisito().getLimiteVigente()).getValorRecuentoInaceptable(), a.getFechaAnalisis());
            } else {
                ValorDeteccion valor = ValorDeteccion.valueOf(((ResultadoBusqueda) a.getResultado()).getValor().toString());
                ValorDeteccion valorInaceptable = ValorDeteccion.valueOf(((Busqueda) a.getRequisito().getLimiteVigente()).getValorBusquedaInaceptable().toString());
                controladorRevResultados.AgregarResultadoBusqueda(a.getId(), muestreo.getId(), a.getRequisito().getEnsayo().getDenominacion(), a.getUsuarioAnalista().getNombre(),
                        a.getLaboratorio().getNombre(), a.getObservaciones(), valor, valorInaceptable, a.getFechaAnalisis());
            }
            controladorAnalisis.EliminarAnalisis(a.getId());
        }
    }

    private void redirigir() throws IOException {
        context.redirect(context.getRequestContextPath() + "/muestreos/listamuestreosconresultados.xhtml?tipomuestra=" + tipoMuestraSeleccionado);
        facesContext.renderResponse();
        facesContext.responseComplete();
    }

    private List<Analisis> tratarAnalisis(List<Analisis> listaAnalisis) {
        List<Analisis> analisisNoCoincidentes = new ArrayList<>();
        Iterator it = listaAnalisis.iterator();
        while (it.hasNext()) {
            Analisis analisis = (Analisis) it.next();
            for (Requisito requisito : especificacionSeleccionada.getDestino().getRequisitos()) {
                if (analisis.getRequisito().getEnsayo().getId().equals(requisito.getEnsayo().getId())) {
                    analisis.setRequisito(requisito);
                }
            }
            it.remove();
            analisisNoCoincidentes.add(analisis);
        }
        return analisisNoCoincidentes;
    }

    private boolean datosCambiados() {
        observaciones = observaciones == null ? new String() : observaciones;
        lote = lote == null ? new String() : lote;
        muestreo.setObservaciones(muestreo.getObservaciones() == null ? new String() : muestreo.getObservaciones());
        if (fechaMuestreo != muestreo.getFechaMuestreo() || esRepeticion != muestreo.isEsRepeticion() || !muestreo.getObservaciones().equals(observaciones)
                || almacenamientoSeleccionado != muestreo.getAlmacenamiento() || !idUsuarioSeleccionado.equals(muestreo.getUsuarioMonitor().getId())) {
            return true;
        }
        switch (tipoMuestraSeleccionado) {
            case Producto:
                if (lote == null || lote.isEmpty()) {
                    if (fechaOrigen != ((MuestraProducto) muestreo.getMuestra()).getFechaOrigen()
                            || fechaProduccion != ((MuestraProducto) muestreo.getMuestra()).getFechaProduccion()) {
                        return true;
                    }
                }
                if (fechaOrigen != ((MuestraProducto) muestreo.getMuestra()).getFechaOrigen()
                        || fechaProduccion != ((MuestraProducto) muestreo.getMuestra()).getFechaProduccion()
                        || !lote.equals(((MuestraProducto) muestreo.getMuestra()).getLote())) {
                    return true;
                }

                break;
            case Ambiente:
                if (enContactoProducto != ((MuestraAmbiente) muestreo.getMuestra()).getContactaProducto()) {
                    return true;
                }
                break;
            case Operario:
                if (!nombre.equals(((MuestraOperario) muestreo.getMuestra()).getNombre()) || padron != ((MuestraOperario) muestreo.getMuestra()).getPadron()) {
                    return true;
                }
                break;
        }
        return false;
    }

    private boolean comprobarDatosMuestreo() {
        boolean ok = true;
        if (fechaMuestreo == null) {
            MensajesWeb.MostrarError("form-editar-muestreo:mensajes-vista", "Error: ", "La fecha de muestreo es un dato obligatorio.");
            ok = false;
        }

        if (idUsuarioSeleccionado == null || idUsuarioSeleccionado.equals(0)) {
            MensajesWeb.MostrarError("form-editar-muestreo:mensajes-vista", "Error: ", "No se selecciono Monitor.");
            ok = false;
        }

        if (idEspecificacionMuestraSeleccionada == null || idEspecificacionMuestraSeleccionada.equals(0)) {
            MensajesWeb.MostrarError("form-editar-muestreo:mensajes-vista", "Error: ", "No se selecciono Muestra.");
            ok = false;
        }
        return ok;
    }

    private boolean comprobarDatosMuestreoProducto() {
        boolean ok = true;

        if (fechaOrigen == null) {
            MensajesWeb.MostrarError("form-editar-muestreo:mensajes-vista", "Error: ", "La fecha de Faena es un dato obligatorio.");
            ok = false;
        }

        if (fechaProduccion == null) {
            MensajesWeb.MostrarError("form-editar-muestreo:mensajes-vista", "Error: ", "La fecha de Produccion es un dato obligatorio.");
            ok = false;
        }

        if (fechaMuestreo != null && fechaOrigen != null && fechaProduccion != null) {
            if (fechaMuestreo.before(fechaOrigen) || fechaMuestreo.before(fechaProduccion)) {
                MensajesWeb.MostrarError("form-editar-muestreo:mensajes-vista", "Error: ", "La fecha de muestreo no puede ser posterior a la fecha del producto.");
                ok = false;
            }
        }

        return ok;
    }

    private boolean comprobarDatosMuestreoOperario() {
        boolean ok = true;
        if (nombre.isEmpty() || nombre == null) {
            MensajesWeb.MostrarError("form-editar-muestreo:mensajes-vista", "Error: ", "El nombre del operario muestreado es un dato obligatorio.");
            ok = false;
        }

        if (padron < 1) {
            MensajesWeb.MostrarError("form-editar-muestreo:mensajes-vista", "Error: ", "El paron o identificacion del operario muestreado es un dato obligatorio.");
            ok = false;
        }
        return ok;

    }

    @PostConstruct
    public void init() {
        try {
            facesContext = FacesContext.getCurrentInstance();
            context = facesContext.getExternalContext();

            Long idMuestra = 0L;
            idMuestra = Long.valueOf(context.getRequestParameterMap().get("idmuestreo"));
            controladorMuestreos = new ControladorMuestreos();
            muestreo = controladorMuestreos.ObtenerMuestreo(idMuestra);

            controladorAnalisis = new ControladorAnalisis();

            controladorAreas = new ControladorAreas();
            areas = controladorAreas.ListarAreas(true);
            controladorUsuarios = new ControladorUsuarios();
            usuarios = controladorUsuarios.ListarUsuarios(true);
            especificacionesMuestra = new ArrayList<>();

            // datos del muestreo guardado
            cargarDatosMuestreo();

            //controladores de cambio de destino/especificacion
            controladorRevResultados = new ControladorRevResultados();
            controladorRevMuestreos = new ControladorRevMuestreos();
            esInicial = false;
        } catch (Exception ex) {
            //TODO: redirigir a error
        }
    }

    private void cargarDatosMuestreo() {
        tipoMuestraSeleccionado = muestreo.getMuestra().getEspecificacionMuestra().getTipoMuestra();
        idAreaSeleccionada = muestreo.getMuestra().getEspecificacionMuestra().getArea().getId();
        observaciones = muestreo.getObservaciones() == null ? new String() : muestreo.getObservaciones();

        filtrarMuestraArea(null);

        switch (tipoMuestraSeleccionado) {

            case Producto:
                fechaProduccion = ((MuestraProducto) muestreo.getMuestra()).getFechaProduccion();
                fechaOrigen = ((MuestraProducto) muestreo.getMuestra()).getFechaOrigen();
                lote = ((MuestraProducto) muestreo.getMuestra()).getLote() == null ? new String() : ((MuestraProducto) muestreo.getMuestra()).getLote();
                break;

            case Ambiente:
                enContactoProducto = ((MuestraAmbiente) muestreo.getMuestra()).getContactaProducto();
                break;

            case Operario:
                padron = ((MuestraOperario) muestreo.getMuestra()).getPadron();
                nombre = ((MuestraOperario) muestreo.getMuestra()).getNombre();
                break;

            default:
                break;
        }

        idEspecificacionMuestraSeleccionada = muestreo.getMuestra().getEspecificacionMuestra().getId();
        observaciones = muestreo.getObservaciones();
        almacenamientoSeleccionado = muestreo.getAlmacenamiento();
        esRepeticion = muestreo.isEsRepeticion();
        fechaMuestreo = muestreo.getFechaMuestreo();
        idUsuarioSeleccionado = muestreo.getUsuarioMonitor().getId();
    }

}

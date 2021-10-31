/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package web.muestreos;

import controladores.microbiologia.ControladorAnalisis;
import controladores.muestreos.ControladorMuestreos;
import revision.controladores.muestreos.ControladorRevMuestreos;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import javax.faces.view.ViewScoped;

import javax.inject.Named;
import modelo.microbiologia.Analisis;
import modelo.microbiologia.Busqueda;
import modelo.microbiologia.Recuento;
import modelo.microbiologia.ResultadoBusqueda;
import modelo.microbiologia.ResultadoRecuento;
import modelo.muestreo.Area;
import modelo.muestreo.Destino;
import modelo.muestreo.MuestraAmbiente;
import modelo.muestreo.MuestraProducto;
import modelo.muestreo.Muestreo;
import modelo.muestreo.TipoMuestra;
import modelo.usuarios.Usuario;
import revision.controladores.muestreos.ControladorRevResultados;
import revision.modelo.microbiologia.ValorDeteccion;
import web.helpers.MensajesWeb;
import web.helpers.Paginator;
import web.sesion.SesionDeUsuario;

/**
 *
 * @author dipe2
 */
@ViewScoped
@Named
@ManagedBean
public class ListaDeMuestreoConResultados implements Serializable {
    
    private ControladorMuestreos controladorMuestreos;
    private ControladorRevMuestreos revMuestreos;
    private ControladorRevResultados revResultados;
    
    private ExternalContext context;
    
    private TipoMuestra tipoMuestra;
    
    private List<Muestreo> muestreos;
    
    public TipoMuestra getTipoMuestra() {
        return tipoMuestra;
    }
    
    public void setTipoMuestra(TipoMuestra tipoMuestra) {
        this.tipoMuestra = tipoMuestra;
    }
    
    public List<Muestreo> getMuestreos() {
        return muestreos;
    }
    
    public void setMuestreos(List<Muestreo> muestreos) {
        this.muestreos = muestreos;
    }
    
    public void redirigir(Long id) throws IOException {
        context.redirect(context.getRequestContextPath() + "/microbiologia/analisis/altaanalisisresultados.xhtml?idmuestreo=" + id);
        FacesContext.getCurrentInstance().renderResponse();
        FacesContext.getCurrentInstance().responseComplete();
    }
    
    public void eliminarResultado(Long idEnsayo) throws IOException {
        if (idEnsayo > 0) {
            if (new ControladorAnalisis().EliminarAnalisis(idEnsayo) > 0) {
                context.redirect(context.getRequestContextPath() + "/muestreos/listamuestreosconresultados.xhtml?tipomuestra=" + tipoMuestra);
                FacesContext.getCurrentInstance().renderResponse();
                FacesContext.getCurrentInstance().responseComplete();
            }else{
                MensajesWeb.MostrarError("form-lista-muestreos:mensajes-vista", "Erorr: ", "No se pudo eliminar el ensayo seleccionado");
            }
        }else{
            MensajesWeb.MostrarError("form-lista-muestreos:mensajes-vista", "Erorr: ", "No se pudo eliminar el ensayo seleccionado");
        }
    }
    
    public void enviarRevision(Long idMuestreo, Long idAnalisis) throws IOException {
        try {
            Muestreo muestreo = muestreos.stream()
                    .filter(m -> m.getId().equals(idMuestreo))
                    .findAny()
                    .get();
            if (muestreo != null) {
                Analisis analisis = muestreo.FindAnalisis(idAnalisis);
                if (revMuestreos.ObtenerRevMuestreo(idMuestreo) == null) {
                    switch (tipoMuestra) {
                        case Producto:
                            MuestraProducto muestra = (MuestraProducto) muestreo.getMuestra();
                            revMuestreos.CrearMuestreoProducto(idMuestreo, muestra.getEspecificacionMuestra().getDenominacion(), analisis.getRequisito().getDestino().getDenominacion(),
                                    muestreo.getUsuarioMonitor().getNombre(), muestreo.getObservaciones(), muestra.getEspecificacionMuestra().getArea().getNombre(), muestreo.getFechaMuestreo(),
                                    muestra.getFechaProduccion(), muestra.getFechaOrigen(), muestreo.isEsRepeticion());
                            break;
                            
                        default:
                            MuestraAmbiente muestraAmb = (MuestraAmbiente) muestreo.getMuestra();
                            revMuestreos.CrearMuestreoGenerica(idMuestreo, muestraAmb.getEspecificacionMuestra().getDenominacion(), analisis.getRequisito().getDestino().getDenominacion(),
                                    muestreo.getUsuarioMonitor().getNombre(), muestreo.getObservaciones(), muestraAmb.getEspecificacionMuestra().getArea().getNombre(), muestreo.getFechaMuestreo(),
                                    muestreo.isEsRepeticion(), muestraAmb.getContactaProducto());
                            break;
                    }
                    
                } else {
                    if (analisis.getResultado().getClass().getSimpleName().equalsIgnoreCase("ResultadoRecuento")) {
                        revResultados.AgregarResultadoRecuento(idAnalisis, idMuestreo, analisis.getRequisito().getEnsayo().getDenominacion(), analisis.getUsuarioAnalista().getNombre(),
                                analisis.getLaboratorio().getNombre(), analisis.getObservaciones(), ((ResultadoRecuento) analisis.getResultado()).getValor(),
                                ((Recuento) analisis.getRequisito().getLimiteVigente()).getValorRecuentoInaceptable(), analisis.getFechaAnalisis());
                    } else {
                        ValorDeteccion valor = ValorDeteccion.valueOf(((ResultadoBusqueda) analisis.getResultado()).getValor().toString());
                        ValorDeteccion valorInaceptable = ValorDeteccion.valueOf(((Busqueda) analisis.getRequisito().getLimiteVigente()).getValorBusquedaInaceptable().toString());
                        revResultados.AgregarResultadoBusqueda(idAnalisis, idMuestreo, analisis.getRequisito().getEnsayo().getDenominacion(), analisis.getUsuarioAnalista().getNombre(),
                                analisis.getLaboratorio().getNombre(), analisis.getObservaciones(), valor, valorInaceptable, analisis.getFechaAnalisis());
                    }
                }
            }
        } catch (Exception e) {
        }
        context.redirect(context.getRequestContextPath() + "/revisiones/listarevisionesmuestreos.xhtml?tipomuestra=" + tipoMuestra);
        FacesContext.getCurrentInstance().renderResponse();
        FacesContext.getCurrentInstance().responseComplete();
    }
    
    public void redirigirVerificar(Long id) throws IOException {
        context.redirect(context.getRequestContextPath() + "/microbiologia/analisis/altaverificacion.xhtml?idmuestreo=" + id);
        FacesContext.getCurrentInstance().renderResponse();
        FacesContext.getCurrentInstance().responseComplete();
    }
    
    //<editor-fold desc="Filtros">
    //<editor-fold desc="Atributos">
    private List<Muestreo> muestreosSinFiltro;
    
    private List<Destino> destinosDispobibles;
    private List<Long> destinosSeleccionados;
    
    private List<Area> areasDisponibles;
    private List<Long> areasSeleccionadas;
    
    private List<Usuario> monitoresDisponibles;
    private List<Long> monitoresSeleccionados;
    
    private String nombreBuscar;
    private Repeticion repeticionSeleccionada;
    
    private Date fechaMuestreoDesde;
    private Date fechaMuestreoHasta;
    
    private Date fechaOrigenDesde;
    private Date fechaOrigenHasta;
    
    private Date fechaProduccionDesde;
    private Date fechaProduccionHasta;
    
    private boolean filtradoDestino;
    private boolean filtradoNombre;
    private boolean filtradoArea;
    private boolean filtradoRepeticion;
    private boolean filtradoMonitores;
    private boolean filtradoFechaMuestreo;
    private boolean filtradoFechaOrigen;
    private boolean filtradoFechaProduccion;
    
    //</editor-fold>
    //<editor-fold desc="Setters y Getters">
    public Repeticion getRepeticionSeleccionada() {
        return repeticionSeleccionada;
    }
    
    public void setRepeticionSeleccionada(Repeticion repeticionSeleccionada) {
        this.repeticionSeleccionada = repeticionSeleccionada;
    }
    
    public List<Destino> getDestinosDispobibles() {
        return destinosDispobibles;
    }
    
    public void setDestinosDispobibles(List<Destino> destinosDispobibles) {
        this.destinosDispobibles = destinosDispobibles;
    }
    
    public List<Long> getDestinosSeleccionados() {
        return destinosSeleccionados;
    }
    
    public void setDestinosSeleccionados(List<Long> destinosSeleccionados) {
        this.destinosSeleccionados = destinosSeleccionados;
    }
    
    public List<Area> getAreasDisponibles() {
        return areasDisponibles;
    }
    
    public void setAreasDisponibles(List<Area> areasDisponibles) {
        this.areasDisponibles = areasDisponibles;
    }
    
    public List<Long> getAreasSeleccionadas() {
        return areasSeleccionadas;
    }
    
    public void setAreasSeleccionadas(List<Long> areasSeleccionadas) {
        this.areasSeleccionadas = areasSeleccionadas;
    }
    
    public List<Usuario> getMonitoresDisponibles() {
        return monitoresDisponibles;
    }
    
    public void setMonitoresDisponibles(List<Usuario> monitoresDisponibles) {
        this.monitoresDisponibles = monitoresDisponibles;
    }
    
    public List<Long> getMonitoresSeleccionados() {
        return monitoresSeleccionados;
    }
    
    public void setMonitoresSeleccionados(List<Long> monitoresSeleccionados) {
        this.monitoresSeleccionados = monitoresSeleccionados;
    }
    
    public Date getFechaMuestreoDesde() {
        return fechaMuestreoDesde;
    }
    
    public void setFechaMuestreoDesde(Date fechaMuestreoDesde) {
        this.fechaMuestreoDesde = fechaMuestreoDesde;
    }
    
    public Date getFechaMuestreoHasta() {
        return fechaMuestreoHasta;
    }
    
    public void setFechaMuestreoHasta(Date fechaMuestreoHasta) {
        this.fechaMuestreoHasta = fechaMuestreoHasta;
    }
    
    public Date getFechaOrigenDesde() {
        return fechaOrigenDesde;
    }
    
    public void setFechaOrigenDesde(Date fechaOrigenDesde) {
        this.fechaOrigenDesde = fechaOrigenDesde;
    }
    
    public Date getFechaOrigenHasta() {
        return fechaOrigenHasta;
    }
    
    public void setFechaOrigenHasta(Date fechaOrigenHasta) {
        this.fechaOrigenHasta = fechaOrigenHasta;
    }
    
    public Date getFechaProduccionDesde() {
        return fechaProduccionDesde;
    }
    
    public void setFechaProduccionDesde(Date fechaProduccionDesde) {
        this.fechaProduccionDesde = fechaProduccionDesde;
    }
    
    public Date getFechaProduccionHasta() {
        return fechaProduccionHasta;
    }
    
    public void setFechaProduccionHasta(Date fechaProduccionHasta) {
        this.fechaProduccionHasta = fechaProduccionHasta;
    }
    
    public String getNombreBuscar() {
        return nombreBuscar;
    }
    
    public void setNombreBuscar(String nombreBuscar) {
        this.nombreBuscar = nombreBuscar;
    }
    
    //</editor-fold>
    //<editor-fold desc="Botones">
    public void botonBuscarNombre(String nombre) {
        if (nombre.equals("") || nombre.isEmpty()) {
            resetTodo();
        } else {
            muestreos = muestreosSinFiltro;
            if (filtradoRepeticion) {
                muestreos = filtrarRepeticion(muestreos, repeticionSeleccionada);
            }
            if (filtradoDestino) {
                muestreos = filtrarDestinos(muestreos, destinosSeleccionados);
            }
            
            if (filtradoArea) {
                muestreos = filtrarDestinos(muestreos, areasSeleccionadas);
            }
            
            if (filtradoMonitores) {
                muestreos = filtrarMonitores(muestreos, monitoresSeleccionados);
            }
            
            if (filtradoFechaMuestreo) {
                muestreos = filtrarFechaMonitoreo(muestreos, fechaMuestreoDesde, fechaMuestreoHasta);
            }
            
            if (filtradoFechaOrigen) {
                muestreos = filtrarFechaOrigen(muestreos, fechaMuestreoDesde, fechaMuestreoHasta);
            }
            
            if (filtradoFechaProduccion) {
                muestreos = filtrarFechaProduccion(muestreos, fechaMuestreoDesde, fechaMuestreoHasta);
            }
            
            muestreos = buscarNombre(muestreos, nombre);
            filtradoNombre = true;
        }
        prepararPagina();
    }
    
    public void botonFiltrarRepeticion(Repeticion repeticion) {
        if (repeticion.equals(Repeticion.Todas)) {
            resetTodo();
        }
        muestreos = muestreosSinFiltro;
        if (filtradoNombre) {
            muestreos = buscarNombre(muestreos, nombreBuscar);
        }
        
        if (filtradoDestino) {
            muestreos = filtrarDestinos(muestreos, destinosSeleccionados);
        }
        
        if (filtradoArea) {
            muestreos = filtrarDestinos(muestreos, areasSeleccionadas);
        }
        
        if (filtradoMonitores) {
            muestreos = filtrarMonitores(muestreos, monitoresSeleccionados);
        }
        
        if (filtradoFechaMuestreo) {
            muestreos = filtrarFechaMonitoreo(muestreos, fechaMuestreoDesde, fechaMuestreoHasta);
        }
        
        if (filtradoFechaOrigen) {
            muestreos = filtrarFechaOrigen(muestreos, fechaMuestreoDesde, fechaMuestreoHasta);
        }
        
        if (filtradoFechaProduccion) {
            muestreos = filtrarFechaProduccion(muestreos, fechaMuestreoDesde, fechaMuestreoHasta);
        }
        
        muestreos = filtrarRepeticion(muestreos, repeticion);
        filtradoRepeticion = true;
        
        prepararPagina();
    }
    
    public void botonFiltrarDestinos() {
        muestreos = muestreosSinFiltro;
        if (filtradoNombre) {
            muestreos = buscarNombre(muestreos, nombreBuscar);
        }
        
        if (filtradoRepeticion) {
            muestreos = filtrarRepeticion(muestreos, repeticionSeleccionada);
        }
        
        if (filtradoArea) {
            muestreos = filtrarDestinos(muestreos, areasSeleccionadas);
        }
        
        if (filtradoMonitores) {
            muestreos = filtrarMonitores(muestreos, monitoresSeleccionados);
        }
        
        if (filtradoFechaMuestreo) {
            muestreos = filtrarFechaMonitoreo(muestreos, fechaMuestreoDesde, fechaMuestreoHasta);
        }
        
        if (filtradoFechaOrigen) {
            muestreos = filtrarFechaOrigen(muestreos, fechaMuestreoDesde, fechaMuestreoHasta);
        }
        
        if (filtradoFechaProduccion) {
            muestreos = filtrarFechaProduccion(muestreos, fechaMuestreoDesde, fechaMuestreoHasta);
        }
        
        muestreos = filtrarDestinos(muestreos, destinosSeleccionados);
        filtradoDestino = true;
        
        prepararPagina();
    }
    
    public void botonFiltrarAreas() {
        
        muestreos = muestreosSinFiltro;
        if (filtradoNombre) {
            muestreos = buscarNombre(muestreos, nombreBuscar);
        }
        
        if (filtradoRepeticion) {
            muestreos = filtrarRepeticion(muestreos, repeticionSeleccionada);
        }
        
        if (filtradoDestino) {
            muestreos = filtrarDestinos(muestreos, destinosSeleccionados);
        }
        
        if (filtradoMonitores) {
            muestreos = filtrarMonitores(muestreos, monitoresSeleccionados);
        }
        
        if (filtradoFechaMuestreo) {
            muestreos = filtrarFechaMonitoreo(muestreos, fechaMuestreoDesde, fechaMuestreoHasta);
        }
        
        if (filtradoFechaOrigen) {
            muestreos = filtrarFechaOrigen(muestreos, fechaMuestreoDesde, fechaMuestreoHasta);
        }
        
        if (filtradoFechaProduccion) {
            muestreos = filtrarFechaProduccion(muestreos, fechaMuestreoDesde, fechaMuestreoHasta);
        }
        
        muestreos = filtrarAreas(muestreos, areasSeleccionadas);
        filtradoArea = true;
        
        prepararPagina();
    }
    
    public void botonFiltrarMonitores() {
        muestreos = muestreosSinFiltro;
        if (filtradoNombre) {
            muestreos = buscarNombre(muestreos, nombreBuscar);
        }
        
        if (filtradoRepeticion) {
            muestreos = filtrarRepeticion(muestreos, repeticionSeleccionada);
        }
        
        if (filtradoDestino) {
            muestreos = filtrarDestinos(muestreos, destinosSeleccionados);
        }
        
        if (filtradoArea) {
            muestreos = filtrarDestinos(muestreos, areasSeleccionadas);
        }
        
        if (filtradoFechaMuestreo) {
            muestreos = filtrarFechaMonitoreo(muestreos, fechaMuestreoDesde, fechaMuestreoHasta);
        }
        
        if (filtradoFechaOrigen) {
            muestreos = filtrarFechaOrigen(muestreos, fechaMuestreoDesde, fechaMuestreoHasta);
        }
        
        if (filtradoFechaProduccion) {
            muestreos = filtrarFechaProduccion(muestreos, fechaMuestreoDesde, fechaMuestreoHasta);
        }
        
        muestreos = filtrarMonitores(muestreos, monitoresSeleccionados);
        filtradoMonitores = true;
        
        prepararPagina();
    }
    
    public void botonFiltrarFechaMuestreo() {
        muestreos = muestreosSinFiltro;
        if (filtradoNombre) {
            muestreos = buscarNombre(muestreos, nombreBuscar);
        }
        
        if (filtradoRepeticion) {
            muestreos = filtrarRepeticion(muestreos, repeticionSeleccionada);
        }
        
        if (filtradoDestino) {
            muestreos = filtrarDestinos(muestreos, destinosSeleccionados);
        }
        
        if (filtradoArea) {
            muestreos = filtrarDestinos(muestreos, areasSeleccionadas);
        }
        
        if (filtradoMonitores) {
            muestreos = filtrarMonitores(muestreos, monitoresSeleccionados);
        }
        
        if (filtradoFechaOrigen) {
            muestreos = filtrarFechaOrigen(muestreos, fechaMuestreoDesde, fechaMuestreoHasta);
        }
        
        if (filtradoFechaProduccion) {
            muestreos = filtrarFechaProduccion(muestreos, fechaMuestreoDesde, fechaMuestreoHasta);
        }
        
        muestreos = filtrarFechaMonitoreo(muestreos, fechaMuestreoDesde, fechaMuestreoHasta);
        filtradoFechaMuestreo = true;
        
        prepararPagina();
    }
    
    public void botonFiltrarFechaOrigen() {
        muestreos = muestreosSinFiltro;
        if (filtradoNombre) {
            muestreos = buscarNombre(muestreos, nombreBuscar);
        }
        
        if (filtradoRepeticion) {
            muestreos = filtrarRepeticion(muestreos, repeticionSeleccionada);
        }
        
        if (filtradoDestino) {
            muestreos = filtrarDestinos(muestreos, destinosSeleccionados);
        }
        
        if (filtradoArea) {
            muestreos = filtrarDestinos(muestreos, areasSeleccionadas);
        }
        
        if (filtradoMonitores) {
            muestreos = filtrarMonitores(muestreos, monitoresSeleccionados);
        }
        
        if (filtradoFechaMuestreo) {
            muestreos = filtrarFechaMonitoreo(muestreos, fechaMuestreoDesde, fechaMuestreoHasta);
        }
        
        if (filtradoFechaProduccion) {
            muestreos = filtrarFechaProduccion(muestreos, fechaMuestreoDesde, fechaMuestreoHasta);
        }
        
        muestreos = filtrarFechaOrigen(muestreos, fechaMuestreoDesde, fechaMuestreoHasta);
        filtradoFechaMuestreo = true;
        
        prepararPagina();
    }
    
    public void botonFiltrarFechaProduccion() {
        muestreos = muestreosSinFiltro;
        if (filtradoNombre) {
            muestreos = buscarNombre(muestreos, nombreBuscar);
        }
        
        if (filtradoRepeticion) {
            muestreos = filtrarRepeticion(muestreos, repeticionSeleccionada);
        }
        
        if (filtradoDestino) {
            muestreos = filtrarDestinos(muestreos, destinosSeleccionados);
        }
        
        if (filtradoArea) {
            muestreos = filtrarDestinos(muestreos, areasSeleccionadas);
        }
        
        if (filtradoMonitores) {
            muestreos = filtrarMonitores(muestreos, monitoresSeleccionados);
        }
        
        if (filtradoFechaMuestreo) {
            muestreos = filtrarFechaMonitoreo(muestreos, fechaMuestreoDesde, fechaMuestreoHasta);
        }
        
        if (filtradoFechaOrigen) {
            muestreos = filtrarFechaOrigen(muestreos, fechaMuestreoDesde, fechaMuestreoHasta);
        }
        
        muestreos = filtrarFechaProduccion(muestreos, fechaMuestreoDesde, fechaMuestreoHasta);
        filtradoFechaMuestreo = true;
        
        prepararPagina();
    }
    
    public void botonResetTodo() {
        muestreos = resetTodo();
        filtradoNombre = false;
        filtradoRepeticion = false;
        filtradoDestino = false;
        filtradoArea = false;
        filtradoMonitores = false;
        filtradoFechaMuestreo = false;
        filtradoFechaOrigen = false;
        filtradoFechaProduccion = false;
        resetAreas();
        resetDestinos();
        resetMonitores();
        prepararPagina();
    }
    
    //</editor-fold>
    //<editor-fold desc="Metodos">
    private List buscarNombre(List<Muestreo> list, String nombre) {
        return list.stream()
                .filter(a -> a.getMuestra()
                        .getEspecificacionMuestra()
                        .getDenominacion()
                        .toLowerCase()
                        .startsWith(nombre.toLowerCase()))
                .collect(Collectors.toList());
    }
    
    private List resetTodo() {
        muestreos = new ArrayList<>(muestreosSinFiltro);
        nombreBuscar = "";
        repeticionSeleccionada = Repeticion.Todas;
        return muestreos;
    }
    
    private List filtrarRepeticion(List<Muestreo> list, Repeticion repeticion) {
        if(repeticion == Repeticion.Si){
            return list.stream()
                    .filter(m->m.isEsRepeticion())
                    .toList();
        }
        return list.stream()
                .filter(m->!m.isEsRepeticion())
                .toList();
    }
    
    private List filtrarDestinos(List<Muestreo> list, List destinos) {
        List<Muestreo> muestreosFiltrados = new ArrayList<>();
        list.stream().forEach(m -> {
            for (int i = 0; i < destinos.size(); i++) {
                if (m.getMuestra()
                        .getEspecificacionMuestra()
                        .getDestino()
                        .getId()
                        .equals(destinos.get(i))) {
                    muestreosFiltrados.add(m);
                }
            }
        }
        );
        return muestreosFiltrados;
    }
    
    private List filtrarAreas(List<Muestreo> list, List areas) {
        List<Muestreo> muestreosFiltrados = new ArrayList<>();
        list.stream().forEach(m -> {
            for (int i = 0; i < areas.size(); i++) {
                if (m.getMuestra()
                        .getEspecificacionMuestra()
                        .getArea()
                        .getId()
                        .equals(areas.get(i))) {
                    muestreosFiltrados.add(m);
                }
            }
        }
        );
        return muestreosFiltrados;
    }
    
    private List filtrarMonitores(List<Muestreo> list, List monitores) {
        List<Muestreo> muestreosFiltrados = new ArrayList<>();
        list.stream().forEach(m -> {
            for (int i = 0; i < monitores.size(); i++) {
                if (m.getUsuarioMonitor()
                        .getId()
                        .equals(monitores.get(i))) {
                    muestreosFiltrados.add(m);
                }
            }
        }
        );
        return muestreosFiltrados;
    }
    
    private List filtrarFechaMonitoreo(List<Muestreo> list, Date fechaDesde, Date fechaHasta) {
        List<Muestreo> muestreosFiltrados = new ArrayList<>();
        list.stream()
                .forEach(m -> {
                    if (m.getFechaMuestreo().after(fechaDesde) && m.getFechaMuestreo().before(fechaHasta)) {
                        muestreosFiltrados.add(m);
                    }
                });
        return muestreosFiltrados;
    }
    
    private List filtrarFechaOrigen(List<Muestreo> list, Date fechaDesde, Date fechaHasta) {
        List<Muestreo> muestreosFiltrados = new ArrayList<>();
        list.stream()
                .forEach(m -> {
                    if (((MuestraProducto) m.getMuestra()).getFechaOrigen().after(fechaDesde)
                            && ((MuestraProducto) m.getMuestra()).getFechaOrigen().before(fechaHasta)) {
                        muestreosFiltrados.add(m);
                    }
                });
        return muestreosFiltrados;
    }
    
    private List filtrarFechaProduccion(List<Muestreo> list, Date fechaDesde, Date fechaHasta) {
        List<Muestreo> muestreosFiltrados = new ArrayList<>();
        list.stream()
                .forEach(m -> {
                    if (((MuestraProducto) m.getMuestra()).getFechaProduccion().after(fechaDesde)
                            && ((MuestraProducto) m.getMuestra()).getFechaProduccion().before(fechaHasta)) {
                        muestreosFiltrados.add(m);
                    }
                });
        return muestreosFiltrados;
    }
    
    public void resetDestinos() {
        destinosSeleccionados = extraerIdDestinosDisponibles(muestreosSinFiltro);
    }
    
    public void resetAreas() {
        areasSeleccionadas = extraerIdAreasDisponibles(muestreosSinFiltro);
    }
    
    public void resetMonitores() {
        monitoresSeleccionados = extraerIdMonitoresDisponibles(muestreosSinFiltro);
    }
    
    public void vaciarAreas() {
        areasSeleccionadas.clear();
    }
    
    public void vaciarDestinos() {
        destinosSeleccionados.clear();
    }
    
    public void vaciarMonitores() {
        monitoresSeleccionados.clear();
    }
    
    public void resetFechaMuestreo() {
        if (!muestreosSinFiltro.isEmpty()) {
            fechaMuestreoDesde = extraerFechaMuestreo(muestreosSinFiltro, true);
            fechaMuestreoHasta = extraerFechaMuestreo(muestreosSinFiltro, false);
        }
    }
    
    public void resetFechaOrigen() {
        if (!muestreosSinFiltro.isEmpty()) {
            fechaOrigenDesde = extraerFechaOrigen(muestreosSinFiltro, true);
            fechaOrigenHasta = extraerFechaOrigen(muestreosSinFiltro, false);
        }
    }
    
    public void resetFechaProduccion() {
        if (!muestreosSinFiltro.isEmpty()) {
            fechaProduccionDesde = extraerFechaProduccion(muestreosSinFiltro, true);
            fechaProduccionHasta = extraerFechaProduccion(muestreosSinFiltro, false);
        }
    }
    
    private List extraerDestinosDisponibles(List<Muestreo> lista) {
        List<Destino> destinos = new ArrayList<>();
        lista.stream()
                .forEach(m -> {
                    if (!destinos.contains(m.getMuestra().getEspecificacionMuestra().getDestino())) {
                        destinos.add(m.getMuestra().getEspecificacionMuestra().getDestino());
                    }
                });
        
        return destinos;
    }
    
    private List extraerIdDestinosDisponibles(List<Muestreo> lista) {
        List<Long> destinos = new ArrayList<>();
        lista.stream()
                .forEach(m -> {
                    if (!destinos.contains(m.getMuestra().getEspecificacionMuestra().getDestino().getId())) {
                        destinos.add(m.getMuestra().getEspecificacionMuestra().getDestino().getId());
                    }
                });
        
        return destinos;
    }
    
    private List extraerAreasDisponibles(List<Muestreo> lista) {
        List<Area> areas = new ArrayList<>();
        lista.stream()
                .forEach(m -> {
                    if (!areas.contains(m.getMuestra().getEspecificacionMuestra().getArea())) {
                        areas.add(m.getMuestra().getEspecificacionMuestra().getArea());
                    }
                });
        
        return areas;
    }
    
    private List extraerIdAreasDisponibles(List<Muestreo> lista) {
        List<Long> areas = new ArrayList<>();
        lista.stream()
                .forEach(m -> {
                    if (!areas.contains(m.getMuestra().getEspecificacionMuestra().getArea().getId())) {
                        areas.add(m.getMuestra().getEspecificacionMuestra().getArea().getId());
                    }
                });
        
        return areas;
    }
    
    private List extraerMonitoresDisponibles(List<Muestreo> lista) {
        List<Usuario> monitores = new ArrayList<>();
        lista.stream()
                .forEach(m -> {
                    if (!monitores.contains(m.getUsuarioMonitor())) {
                        monitores.add(m.getUsuarioMonitor());
                    }
                });
        
        return monitores;
    }
    
    private List extraerIdMonitoresDisponibles(List<Muestreo> lista) {
        List<Long> monitores = new ArrayList<>();
        lista.stream()
                .forEach(m -> {
                    if (!monitores.contains(m.getUsuarioMonitor().getId())) {
                        monitores.add(m.getUsuarioMonitor().getId());
                    }
                });
        
        return monitores;
    }
    
    private Date extraerFechaMuestreo(List<Muestreo> lista, boolean minima) {
        if (minima) {
            return lista.stream()
                    .min(Comparator.comparing(m -> m.getFechaMuestreo()))
                    .get()
                    .getFechaMuestreo();
        }
        return lista.stream()
                .max(Comparator.comparing(m -> m.getFechaMuestreo()))
                .get()
                .getFechaMuestreo();
    }
    
    private Date extraerFechaOrigen(List<Muestreo> lista, boolean minima) {
        List<Date> fechas = new ArrayList<>();
        lista.stream()
                .forEach(m -> {
                    if (((MuestraProducto) m.getMuestra()).getFechaOrigen() != null) {
                        fechas.add(((MuestraProducto) m.getMuestra()).getFechaOrigen());
                    }
                });
        if (minima) {
            return fechas
                    .stream()
                    .min(Date::compareTo)
                    .get();
        }
        return fechas
                .stream()
                .max(Date::compareTo)
                .get();
    }
    
    private Date extraerFechaProduccion(List<Muestreo> lista, boolean minima) {
        List<Date> fechas = new ArrayList<>();
        lista.stream()
                .forEach(m -> {
                    if (((MuestraProducto) m.getMuestra()).getFechaProduccion() != null) {
                        fechas.add(((MuestraProducto) m.getMuestra()).getFechaProduccion());
                    }
                });
        if (minima) {
            return fechas
                    .stream()
                    .min(Date::compareTo)
                    .get();
        }
        return fechas
                .stream()
                .max(Date::compareTo)
                .get();
    }
    
    //</editor-fold>
    public enum Repeticion {
        Todas,
        Si,
        No;
    }
    
    //</editor-fold>
    //<editor-fold desc="Paginas">
    private Map<Integer, List<Muestreo>> dicPaginas;
    private List<Integer> listaPaginas;
    private int paginaActual;
    private int totalPaginas;
    
    public Map<Integer, List<Muestreo>> getDicPaginas() {
        return dicPaginas;
    }
    
    public void setDicPaginas(Map<Integer, List<Muestreo>> dicPaginas) {
        this.dicPaginas = dicPaginas;
    }
    
    public int getTotalPaginas() {
        return totalPaginas;
    }
    
    public void setTotalPaginas(int totalPaginas) {
        this.totalPaginas = totalPaginas;
    }
    
    public int getPaginaActual() {
        return paginaActual;
    }
    
    public void setPaginaActual(int paginaActual) {
        this.paginaActual = paginaActual;
    }
    
    public List<Integer> getListaPaginas() {
        return listaPaginas;
    }
    
    public void setListaPaginas(List<Integer> listaPaginas) {
        this.listaPaginas = listaPaginas;
    }
    
    private void prepararPagina() {
        Paginator<Muestreo> paginator = new Paginator();
        totalPaginas = paginator.calcularTotalPaginas(muestreos, SesionDeUsuario.MAX_PAGINA);
        dicPaginas = paginator.llenarDicPaginas(muestreos, SesionDeUsuario.MAX_PAGINA, totalPaginas);
        listaPaginas = paginator.llenarIndicePaginas(dicPaginas.keySet().stream().collect(Collectors.toList()));
        paginaActual = 1;
    }
    
    //</editor-fold>
    @PostConstruct
    public void init() {
        try {
            context = FacesContext.getCurrentInstance().getExternalContext();
            tipoMuestra = TipoMuestra.valueOf(context.getRequestParameterMap().get("tipomuestra"));
            controladorMuestreos = new ControladorMuestreos();
            muestreos = new ArrayList<>();
            muestreosSinFiltro = new ArrayList<>();
            
            controladorMuestreos.ListarMuestreos().stream()
                    .forEach(m -> {
                        if (m.getMuestra().getEspecificacionMuestra().getTipoMuestra() == tipoMuestra) {
                            muestreos.add(m);
                            muestreosSinFiltro.add(m);
                        }
                    });
            
            revMuestreos = new ControladorRevMuestreos();
            revResultados = new ControladorRevResultados();
            
            areasDisponibles = extraerAreasDisponibles(muestreosSinFiltro);
            destinosDispobibles = extraerDestinosDisponibles(muestreosSinFiltro);
            monitoresDisponibles = extraerMonitoresDisponibles(muestreosSinFiltro);
            
            repeticionSeleccionada = ListaDeMuestreoConResultados.Repeticion.Todas;
            destinosSeleccionados = new ArrayList<>();
            areasSeleccionadas = new ArrayList<>();
            monitoresSeleccionados = new ArrayList<>();
            
            prepararPagina();
            resetFechaMuestreo();
            
            if (tipoMuestra == TipoMuestra.Producto) {
                resetFechaOrigen();
                resetFechaProduccion();
            }
            resetDestinos();
            resetAreas();
            resetMonitores();
            
        } catch (Exception ex) {
            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect("/errores/error_404.xhtml");
                
            } catch (IOException ex1) {
                Logger.getLogger(ListaDeMuestreoConResultados.class
                        .getName()).log(Level.SEVERE, null, ex1);
            }
            FacesContext.getCurrentInstance().renderResponse();
            FacesContext.getCurrentInstance().responseComplete();
        }
    }
    
}

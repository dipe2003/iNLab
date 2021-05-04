/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.muestras;

import controladores.muestreos.ControladorEspecificacionMuestras;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import modelo.muestreo.Area;
import modelo.muestreo.Destino;
import modelo.muestreo.EspecificacionMuestra;
import web.helpers.MensajesWeb;
import web.helpers.Paginator;
import web.sesion.SesionDeUsuario;

/**
 *
 * @author dipe2
 */
@Named
@ViewScoped
public class ListaDeEspecificacionesMuestras implements Serializable {

    private ControladorEspecificacionMuestras controladorEspecificacionMuestras;
    private ExternalContext context;

    private List<EspecificacionMuestra> especificacionesMuestra;

    private TipoMuestra tipoSeleccionado;

    public TipoMuestra getTipoSeleccionado() {
        return tipoSeleccionado;
    }

    public void setTipoSeleccionado(TipoMuestra tipoSeleccionado) {
        this.tipoSeleccionado = tipoSeleccionado;
    }

    public List<EspecificacionMuestra> getEspecificacionesMuestra() {
        return especificacionesMuestra;
    }

    public void setEspecificacionesMuestra(List<EspecificacionMuestra> especificacionesMuestra) {
        this.especificacionesMuestra = especificacionesMuestra;
    }

    public void AgregarLimite(Long idRequisito, Long idEnsayo) throws IOException {
        redirigir(context.getRequestContextPath() + "/microbiologia/requisitos/altalimite.xhtml?idreq=" + idRequisito + "&idens=" + idEnsayo);
    }

    public void AgregarRequisito(Long idEspecificacion) throws IOException {
        redirigir(context.getRequestContextPath() + "/microbiologia/requisitos/altarequisito.xhtml?idespec=" + idEspecificacion);
    }

    private void redirigir(String url) throws IOException {
        context.redirect(url);
        FacesContext.getCurrentInstance().renderResponse();
        FacesContext.getCurrentInstance().responseComplete();
    }

    private void redirigir() throws IOException {
        context.redirect("/listaespecificacionesmuestras.xhtml?tipomuestra=" + tipoSeleccionado);
        FacesContext.getCurrentInstance().renderResponse();
        FacesContext.getCurrentInstance().responseComplete();
    }

    public void eliminarEspecificacionMuestra(Long idEspecificacionMuestra) throws IOException {
        if (idEspecificacionMuestra > 0) {
            if (controladorEspecificacionMuestras.EliminarEspecificacionMuestra(idEspecificacionMuestra) > 0) {
                context.redirect("/listaespecificacionesmuestras.xhtml?tipomuestra=" + tipoSeleccionado);
                FacesContext.getCurrentInstance().renderResponse();
                FacesContext.getCurrentInstance().responseComplete();
            } else {
                MensajesWeb.MostrarError("form-lista-especificaciones:botonEliminar", "Erorr: ", "No se pudo eliminar el ensayo seleccionado");
            }
        } else {
            MensajesWeb.MostrarError("form-lista-especificaciones:botonEliminar", "Erorr: ", "No se pudo eliminar el ensayo seleccionado");
        }
    }

    public void darBaja(Long idEspecificacion) throws IOException {
        if (controladorEspecificacionMuestras.DarDeBajaEspecificacionMuestra(idEspecificacion, new Date()) > 0) {
            redirigir();
        } else {
            MensajesWeb.MostrarError("form-lista-especificacion:botonDarBajaMuestra", "No se dio Baja:", "Contacta con el administrador.");
        }
    }

    public void restaurar(Long idEspecificacion) throws IOException {
        if (controladorEspecificacionMuestras.DarDeAltaEspecificacionMuestra(idEspecificacion) > 0) {
            redirigir();
        } else {
            MensajesWeb.MostrarError("form-lista-especificacion:botonDarAltaMuestra", "No se dio Alta:", "Contacta con el administrador.");
        }
    }

    //<editor-fold desc="Filtros">
    //<editor-fold desc="Atributos">
    private List<EspecificacionMuestra> especificacionesMuestraSinFiltro;
    private List<Destino> destinosDispobibles;
    private List<Long> destinosSeleccionados;

    private List<Area> areasDisponibles;
    private List<Long> areasSeleccionadas;

    private String nombreBuscar;
    private Vigencia vigenciaSeleccionada;

    private boolean filtradoDestino;
    private boolean filtradoNombre;
    private boolean filtradoArea;
    private boolean filtradoVigencia;

    //</editor-fold>
    //<editor-fold desc="Getters y Setters">
    public String getNombreBuscar() {
        return nombreBuscar;
    }

    public void setNombreBuscar(String nombreBuscar) {
        this.nombreBuscar = nombreBuscar;
    }

    public Vigencia getVigenciaSeleccionada() {
        return vigenciaSeleccionada;
    }

    public void setVigenciaSeleccionada(Vigencia vigenciaSeleccionada) {
        this.vigenciaSeleccionada = vigenciaSeleccionada;
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

    //</editor-fold>
    //<editor-fold desc="Botones">
    public void botonBuscarNombre(String nombre) {
        if (nombre.equals("") || nombre.isEmpty()) {
            resetTodo();
        } else {
            especificacionesMuestra = especificacionesMuestraSinFiltro;
            if (filtradoVigencia) {
                especificacionesMuestra = filtrarVigencia(especificacionesMuestra, vigenciaSeleccionada);
            }
            if (filtradoDestino) {
                especificacionesMuestra = filtrarDestinos(especificacionesMuestra, destinosSeleccionados);
            }

            if (filtradoArea) {
                especificacionesMuestra = filtrarDestinos(especificacionesMuestra, areasSeleccionadas);
            }

            especificacionesMuestra = buscarNombre(especificacionesMuestra, nombre);
            filtradoNombre = true;
        }
        prepararPagina();
    }

    public void botonFiltrarVigencia(Vigencia vigencia) {
        if (vigencia.equals(Vigencia.Todas)) {
            resetTodo();
        }
        especificacionesMuestra = especificacionesMuestraSinFiltro;
        if (filtradoNombre) {
            especificacionesMuestra = buscarNombre(especificacionesMuestra, nombreBuscar);
        }

        if (filtradoDestino) {
            especificacionesMuestra = filtrarDestinos(especificacionesMuestra, destinosSeleccionados);
        }

        if (filtradoArea) {
            especificacionesMuestra = filtrarDestinos(especificacionesMuestra, areasSeleccionadas);
        }

        especificacionesMuestra = filtrarVigencia(especificacionesMuestra, vigencia);
        filtradoVigencia = true;

        prepararPagina();
    }

    public void botonFiltrarDestinos() {
        especificacionesMuestra = especificacionesMuestraSinFiltro;
        if (filtradoNombre) {
            especificacionesMuestra = buscarNombre(especificacionesMuestra, nombreBuscar);
        }

        if (filtradoVigencia) {
            especificacionesMuestra = filtrarVigencia(especificacionesMuestra, vigenciaSeleccionada);
        }

        if (filtradoArea) {
            especificacionesMuestra = filtrarDestinos(especificacionesMuestra, areasSeleccionadas);
        }

        especificacionesMuestra = filtrarDestinos(especificacionesMuestra, destinosSeleccionados);
        filtradoDestino = true;

        prepararPagina();
    }

    public void botonFiltrarAreas() {
        especificacionesMuestra = especificacionesMuestraSinFiltro;
        if (filtradoNombre) {
            especificacionesMuestra = buscarNombre(especificacionesMuestra, nombreBuscar);
        }

        if (filtradoVigencia) {
            especificacionesMuestra = filtrarVigencia(especificacionesMuestra, vigenciaSeleccionada);
        }

        if (filtradoDestino) {
            especificacionesMuestra = filtrarDestinos(especificacionesMuestra, destinosSeleccionados);
        }

        especificacionesMuestra = filtrarAreas(especificacionesMuestra, areasSeleccionadas);
        filtradoArea = true;

        prepararPagina();
    }

    public void botonResetTodo() {
        especificacionesMuestra = resetTodo();
        filtradoNombre = false;
        filtradoVigencia = false;
        filtradoDestino = false;
        filtradoArea = false;
        resetDestinos();
        resetAreas();
        prepararPagina();
    }

    //</editor-fold>
    //<editor-fold desc="Metodos">
    private List buscarNombre(List<EspecificacionMuestra> list, String nombre) {
        return list.stream()
                .filter(a -> a.getDenominacion().toLowerCase().startsWith(nombre.toLowerCase()))
                .collect(Collectors.toList());
    }

    private List resetTodo() {
        especificacionesMuestra = especificacionesMuestraSinFiltro.stream()
                .collect(Collectors.toList());
        nombreBuscar = "";
        vigenciaSeleccionada = Vigencia.Todas;
        return especificacionesMuestra;
    }

    private List filtrarVigencia(List<EspecificacionMuestra> list, Vigencia vigencia) {
        List<EspecificacionMuestra> especificacionesFiltradas = new ArrayList<>();
        list.stream().forEach(esp -> {
            switch (vigencia) {
                case Vigentes:
                    if (esp.isVigente()) {
                        especificacionesFiltradas.add(esp);
                    }
                    break;

                default:
                    if (!esp.isVigente()) {
                        especificacionesFiltradas.add(esp);
                    }
                    break;
            }
        }
        );
        return especificacionesFiltradas;
    }

    private List filtrarDestinos(List<EspecificacionMuestra> list, List destinos) {
        List<EspecificacionMuestra> especificacionesFiltradas = new ArrayList<>();
        list.stream().forEach(esp -> {
            for (int i = 0; i < destinos.size(); i++) {
                if (esp.getDestino().getId().equals(destinos.get(i))) {
                    especificacionesFiltradas.add(esp);
                }
            }
        }
        );
        return especificacionesFiltradas;
    }

    private List filtrarAreas(List<EspecificacionMuestra> list, List areas) {
        List<EspecificacionMuestra> especificacionesFiltradas = new ArrayList<>();
        list.stream().forEach(esp -> {
            for (int i = 0; i < areas.size(); i++) {
                if (esp.getArea().getId().equals(areas.get(i))) {
                    especificacionesFiltradas.add(esp);
                }
            }
        });
        return especificacionesFiltradas;
    }

    public void resetDestinos() {
        destinosSeleccionados = extraerIdDestinosDisponibles(especificacionesMuestraSinFiltro);
    }

    public void resetAreas() {
        areasSeleccionadas = extraerIdAreasDisponibles(especificacionesMuestraSinFiltro);
    }

    public void vaciarDestinos() {
        destinosSeleccionados.clear();
    }

    public void vaciarAreas() {
        areasSeleccionadas.clear();
    }

    private List extraerDestinosDisponibles(List<EspecificacionMuestra> lista) {
        List<Destino> destinos = new ArrayList<>();
        lista.stream()
                .forEach(esp -> {
                    if (!destinos.contains(esp.getDestino())) {
                        destinos.add(esp.getDestino());
                    }
                });

        return destinos;
    }

    private List extraerIdDestinosDisponibles(List<EspecificacionMuestra> lista) {
        List<Long> destinos = new ArrayList<>();
        lista.stream()
                .forEach(esp -> {
                    if (!destinos.contains(esp.getDestino().getId())) {
                        destinos.add(esp.getDestino().getId());
                    }
                });

        return destinos;
    }

    private List extraerAreasDisponibles(List<EspecificacionMuestra> lista) {
        List<Area> areas = new ArrayList<>();
        lista.stream()
                .forEach(esp -> {
                    if (!areas.contains(esp.getArea())) {
                        areas.add(esp.getArea());
                    }
                });

        return areas;
    }

    private List extraerIdAreasDisponibles(List<EspecificacionMuestra> lista) {
        List<Long> areas = new ArrayList<>();
        lista.stream()
                .forEach(esp -> {
                    if (!areas.contains(esp.getArea().getId())) {
                        areas.add(esp.getArea().getId());
                    }
                });

        return areas;
    }

    //</editor-fold>
    public enum Vigencia {
        Todas,
        Vigentes,
        No_Vigentes;
    }

    //</editor-fold>
    //<editor-fold desc="Paginas">
    private Map<Integer, List<EspecificacionMuestra>> dicPaginas;
    private List<Integer> listaPaginas;
    private int paginaActual;
    private int totalPaginas;

    public Map<Integer, List<EspecificacionMuestra>> getDicPaginas() {
        return dicPaginas;
    }

    public void setDicPaginas(Map<Integer, List<EspecificacionMuestra>> dicPaginas) {
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
        Paginator<EspecificacionMuestra> paginator = new Paginator();
        totalPaginas = paginator.calcularTotalPaginas(especificacionesMuestra, SesionDeUsuario.MAX_PAGINA);
        dicPaginas = paginator.llenarDicPaginas(especificacionesMuestra, SesionDeUsuario.MAX_PAGINA, totalPaginas);
        listaPaginas = paginator.llenarIndicePaginas(dicPaginas.keySet().stream().collect(Collectors.toList()));
        paginaActual = 1;
    }

    //</editor-fold>
    @PostConstruct
    public void init() {
        try {
            context = FacesContext.getCurrentInstance().getExternalContext();
            tipoSeleccionado = TipoMuestra.valueOf(context.getRequestParameterMap().get("tipomuestra"));
            controladorEspecificacionMuestras = new ControladorEspecificacionMuestras();
            if (tipoSeleccionado != TipoMuestra.Todas) {
                especificacionesMuestra = controladorEspecificacionMuestras.ListarEspecificacionMuestras(true)
                        .stream()
                        .filter(esp -> esp.getTipoMuestra().toString().equals(tipoSeleccionado.toString()))
                        .collect(Collectors.toList());
                especificacionesMuestraSinFiltro = controladorEspecificacionMuestras.ListarEspecificacionMuestras(true)
                        .stream()
                        .filter(esp -> esp.getTipoMuestra().toString().equals(tipoSeleccionado.toString()))
                        .collect(Collectors.toList());
            } else {
                especificacionesMuestra = controladorEspecificacionMuestras.ListarEspecificacionMuestras(true);
                especificacionesMuestraSinFiltro = controladorEspecificacionMuestras.ListarEspecificacionMuestras(true);
            }

            areasDisponibles = extraerAreasDisponibles(especificacionesMuestraSinFiltro);
            destinosDispobibles = extraerDestinosDisponibles(especificacionesMuestraSinFiltro);

            vigenciaSeleccionada = Vigencia.Todas;
            destinosSeleccionados = new ArrayList<>();
            areasSeleccionadas = new ArrayList<>();

            prepararPagina();
            resetDestinos();
            resetAreas();
        } catch (Exception ex) {
            System.out.println("Error al cargar lista de especificaciones: " + ex.getMessage());
        }
    }

    public enum TipoMuestra {
        Todas,
        Producto,
        Ambiente,
        Operario,
        Otra
    }
}

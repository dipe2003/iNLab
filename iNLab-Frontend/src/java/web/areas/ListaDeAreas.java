/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.areas;

import com.itext.areas.ControladorITextAreas;
import controladores.muestreos.ControladorAreas;
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
import web.helpers.MensajesWeb;
import web.helpers.Paginator;
import web.sesion.SesionDeUsuario;

/**
 *
 * @author dipe2
 */
@Named
@ViewScoped

public class ListaDeAreas implements Serializable {

    private ControladorAreas controladorAreas;
    private ExternalContext context;

    private List<Area> areasSinFiltro;
    private List<Area> areas;

    public List<Area> getAreas() {
        return this.areas;
    }

    public void setAreas(List<Area> value) {
        this.areas = value;
    }

    public void imprimirLista() throws IOException {
        try {
            ControladorITextAreas conPdf = new ControladorITextAreas();
            ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
            conPdf.CrearListadoAreas(context, areasSinFiltro, false);
            FacesContext.getCurrentInstance().responseComplete();
            FacesContext.getCurrentInstance().renderResponse();
        } catch (IOException ex) {
            System.out.println("Error al crear pdf: " + ex.getMessage());
        }
    }
    
    public void eliminarArea(Long idArea) throws IOException {
        if (idArea > 0) {
            if (controladorAreas.EliminarArea(idArea) > 0) {
              redirigir();
            } else {
                MensajesWeb.MostrarError("form-lista-areas:botonEliminar", "Erorr: ", "No se pudo eliminar el area seleccionada");
            }
        } else {
            MensajesWeb.MostrarError("form-lista-areas:botonEliminar", "Erorr: ", "No se pudo eliminar el area seleccionada");
        }
    }
    
     public void darBaja(Long idArea) throws IOException {
        if (controladorAreas.DarDeBajaArea(idArea, new Date()) > 0) {
            redirigir();
        } else {
            MensajesWeb.MostrarError("form-listar-areas:botonDarBajaArea", "No se dio Baja:", "Contacta con el administrador.");
        }
    }

    public void restaurar(Long idArea) throws IOException {
        if (controladorAreas.DarDeAltaArea(idArea) > 0) {
            redirigir();
        } else {
            MensajesWeb.MostrarError("form-listar-areas:botonDarAltaArea", "No se dio Alta:", "Contacta con el administrador.");
        }
    }

    private void redirigir() throws IOException {
        if (context.isResponseCommitted()) {
            context = FacesContext.getCurrentInstance().getExternalContext();
        }
        context.redirect(context.getRequestContextPath() + "/areas/listaareas.xhtml");
    }

    //<editor-fold desc="Filtros">
    private String nombreBuscar;
    private TipoArea tipoAreaSeleccionada;
    private Vigencia vigenciaSeleccionada;

    private boolean filtradoNombre;
    private boolean filtradoTipo;
    private boolean filtradoVigencia;

    public String getNombreBuscar() {
        return nombreBuscar;
    }

    public void setNombreBuscar(String nombreBuscar) {
        this.nombreBuscar = nombreBuscar;
    }

    public TipoArea getTipoAreaSeleccionada() {
        return tipoAreaSeleccionada;
    }

    public void setTipoAreaSeleccionada(TipoArea tipoAreaSeleccionada) {
        this.tipoAreaSeleccionada = tipoAreaSeleccionada;
    }

    public Vigencia getVigenciaSeleccionada() {
        return vigenciaSeleccionada;
    }

    public void setVigenciaSeleccionada(Vigencia vigenciaSeleccionada) {
        this.vigenciaSeleccionada = vigenciaSeleccionada;
    }

    public void botonBuscarNombre(String nombre) {
        if (nombre.equals("") || nombre.isEmpty()) {
            resetTodo();
        } else {
            areas = areasSinFiltro;
            if (filtradoTipo) {
                areas = filtrarTipo(areas, tipoAreaSeleccionada);
            }
            if (filtradoVigencia) {
                areas = filtrarVigencia(areas, vigenciaSeleccionada);
            }
            areas = buscarNombre(areas, nombre);
            filtradoNombre = true;
        }
        prepararPagina();
    }

    public void botonFiltrarTipo(TipoArea tipo) {
        if (tipoAreaSeleccionada.equals(TipoArea.Todas)) {
            resetTodo();
        } else {
            areas = areasSinFiltro;
            if (filtradoNombre) {
                areas = buscarNombre(areas, nombreBuscar);
            }

            if (filtradoVigencia) {
                areas = filtrarVigencia(areas, vigenciaSeleccionada);
            }
            areas = filtrarTipo(areas, tipo);
            filtradoTipo = true;
        }
        prepararPagina();
    }

    public void botonFiltrarVigencia(Vigencia vigencia) {
        if (vigencia.equals(Vigencia.Todas)) {
            resetTodo();
        } else {
            areas = areasSinFiltro;
            if (filtradoNombre) {
                areas = buscarNombre(areas, nombreBuscar);
            }

            if (filtradoTipo) {
                areas = filtrarTipo(areas, tipoAreaSeleccionada);
            }
            areas = filtrarVigencia(areas, vigencia);
            filtradoVigencia = true;
        }
        prepararPagina();
    }

    public void botonResetTodo() {
        areas = resetTodo();
        filtradoNombre = false;
        filtradoTipo = false;
        filtradoVigencia = false;
        prepararPagina();
    }

    private List buscarNombre(List<Area> list, String nombre) {
        return list.stream()
                .filter(a -> a.getNombre().toLowerCase().startsWith(nombre.toLowerCase()))
                .collect(Collectors.toList());
    }

    private List resetTodo() {
        areas = areasSinFiltro.stream()
                .collect(Collectors.toList());
        nombreBuscar = "";
        tipoAreaSeleccionada = TipoArea.Todas;
        vigenciaSeleccionada = Vigencia.Todas;
        return areas;
    }

    private List filtrarTipo(List<Area> list, TipoArea tipo) {
        List<Area> areasfiltradas = new ArrayList<>();
        list.stream().forEach(a -> {
            switch (tipo) {
                case Productiva:
                    if (a.isEsProductiva()) {
                        areasfiltradas.add(a);
                    }
                    break;

                default:
                    if (!a.isEsProductiva()) {
                        areasfiltradas.add(a);
                    }
                    break;
            }
        }
        );
        return areasfiltradas;
    }

    private List filtrarVigencia(List<Area> list, Vigencia vigencia) {
        List<Area> areasfiltradas = new ArrayList<>();
        list.stream().forEach(a -> {
            switch (vigencia) {
                case Vigentes:
                    if (a.isEsVigente()) {
                        areasfiltradas.add(a);
                    }
                    break;

                default:
                    if (!a.isEsVigente()) {
                        areasfiltradas.add(a);
                    }
                    break;
            }
        }
        );
        return areasfiltradas;
    }

    //</editor-fold>
    //<editor-fold desc="Paginas">
    private Map<Integer, List<Area>> dicPaginas;
    private List<Integer> listaPaginas;
    private int paginaActual;
    private int totalPaginas;

    public Map<Integer, List<Area>> getDicPaginas() {
        return dicPaginas;
    }

    public void setDicPaginas(Map<Integer, List<Area>> dicPaginas) {
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
        Paginator<Area> paginator = new Paginator();
        totalPaginas = paginator.calcularTotalPaginas(areas, SesionDeUsuario.MAX_PAGINA);
        dicPaginas = paginator.llenarDicPaginas(areas, SesionDeUsuario.MAX_PAGINA, totalPaginas);
        listaPaginas = paginator.llenarIndicePaginas(dicPaginas.keySet().stream().collect(Collectors.toList()));
        paginaActual = 1;
    }

    //</editor-fold>
    
    @PostConstruct
    public void init() {
        context = FacesContext.getCurrentInstance().getExternalContext();
        controladorAreas = new ControladorAreas();
        areas = controladorAreas.ListarAreas(false);
        areasSinFiltro = controladorAreas.ListarAreas(false);
        tipoAreaSeleccionada = TipoArea.Todas;
        vigenciaSeleccionada = Vigencia.Todas;
        prepararPagina();
    }

    public enum TipoArea {
        Todas,
        Productiva,
        No_Productiva
    };

    public enum Vigencia {
        Todas,
        Vigentes,
        No_Vigentes;
    }
}

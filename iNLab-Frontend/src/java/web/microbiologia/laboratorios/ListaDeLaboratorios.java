/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.microbiologia.laboratorios;

import controladores.microbiologia.ControladorLaboratorios;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import modelo.microbiologia.Laboratorio;
import web.helpers.MensajesWeb;
import web.helpers.Paginator;
import web.sesion.SesionDeUsuario;

/**
 *
 * @author dipe2
 */
@Named
@ViewScoped

public class ListaDeLaboratorios implements Serializable {

    private ControladorLaboratorios controladorLaboratorios;

    private List<Laboratorio> laboratorios;

    public List<Laboratorio> getLaboratorios() {
        return this.laboratorios;
    }

    public void setLaboratorios(List<Laboratorio> value) {
        this.laboratorios = value;
    }

    public void eliminarLaboratorio(Long idLaboratorio) throws IOException {
        if (idLaboratorio > 0) {
            if (controladorLaboratorios.EliminarLaboratorio(idLaboratorio) > 0) {
                ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
                context.redirect(context.getRequestContextPath() + "/microbiologia/laboratorios/listalaboratorios.xhtml");
                FacesContext.getCurrentInstance().renderResponse();
                FacesContext.getCurrentInstance().responseComplete();
            } else {
                MensajesWeb.MostrarError("form-lista-laboratorios:mensjes-vista", "Erorr.", "No se pudo eliminar el Laboratorio seleccionado");
            }
        } else {
            MensajesWeb.MostrarError("form-lista-laboratorios:mensajes-vista", "Erorr.", "No se pudo eliminar el Laboratorio seleccionado");
        }
    }

    //<editor-fold desc="Filtros">
    private String nombreBuscar;
    private TipoLaboratorio tipoLaboratorioSeleccionado;
    private List<Laboratorio> laboratoriosSinFiltros;
    private TipoLaboratorio[] tiposLaboratorios = TipoLaboratorio.values();

    private boolean filtradoNombre;
    private boolean filtradoTipo;

    public String getNombreBuscar() {
        return nombreBuscar;
    }

    public void setNombreBuscar(String nombreBuscar) {
        this.nombreBuscar = nombreBuscar;
    }

    public TipoLaboratorio getTipoLaboratorioSeleccionado() {
        return tipoLaboratorioSeleccionado;
    }

    public void setTipoLaboratorioSeleccionado(TipoLaboratorio tipoLaboratorioSeleccionado) {
        this.tipoLaboratorioSeleccionado = tipoLaboratorioSeleccionado;
    }

    public TipoLaboratorio[] getTiposLaboratorios() {
        return tiposLaboratorios;
    }

    public void setTiposLaboratorios(TipoLaboratorio[] tiposLaboratorios) {
        this.tiposLaboratorios = tiposLaboratorios;
    }

    public void botonBuscarNombre(String nombre) {
        if (nombre.equals("") || nombre.isEmpty()) {
            resetTodo();
        } else {
            laboratorios = laboratoriosSinFiltros;
            if (filtradoTipo) {
                laboratorios = filtrarTipo(laboratorios, tipoLaboratorioSeleccionado);
            }
            laboratorios = buscarNombre(laboratorios, nombre);
            filtradoNombre = true;
        }
        prepararPagina();
    }

    public void botonFiltrarTipo(TipoLaboratorio tipo) {
        if (tipoLaboratorioSeleccionado.equals(TipoLaboratorio.Todos)) {
            resetTodo();
        } else {
            laboratorios = laboratoriosSinFiltros;
            if (filtradoNombre) {
                laboratorios = buscarNombre(laboratorios, nombreBuscar);
            }

            laboratorios = filtrarTipo(laboratorios, tipo);
            filtradoTipo = true;
        }
        prepararPagina();
    }

    public void botonResetTodo() {
        laboratorios = resetTodo();
        filtradoNombre = false;
        filtradoTipo = false;
        prepararPagina();
    }

    private List buscarNombre(List<Laboratorio> list, String nombre) {
        return list.stream()
                .filter(l -> l.getNombre().toLowerCase().startsWith(nombre.toLowerCase()))
                .collect(Collectors.toList());
    }

    private List resetTodo() {
        laboratorios = laboratoriosSinFiltros.stream()
                .collect(Collectors.toList());
        nombreBuscar = "";
        tipoLaboratorioSeleccionado = TipoLaboratorio.Todos;
        return laboratorios;
    }

    private List filtrarTipo(List<Laboratorio> list, TipoLaboratorio tipo) {
        List<Laboratorio> labsFiltrados = new ArrayList<>();
        list.stream().forEach(l -> {
            switch (tipo) {
                case Externos:
                    if (l.isEsExterno()) {
                        labsFiltrados.add(l);
                    }
                    break;

                default:
                    if (!l.isEsExterno()) {
                        labsFiltrados.add(l);
                    }
                    break;
            }
        }
        );
        return labsFiltrados;
    }

    //</editor-fold>
    //<editor-fold desc="Paginas">
    private Map<Integer, List<Laboratorio>> dicPaginas;
    private List<Integer> listaPaginas;
    private int paginaActual;
    private int totalPaginas;

    public Map<Integer, List<Laboratorio>> getDicPaginas() {
        return dicPaginas;
    }

    public void setDicPaginas(Map<Integer, List<Laboratorio>> dicPaginas) {
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
        Paginator<Laboratorio> paginator = new Paginator();
        totalPaginas = paginator.calcularTotalPaginas(laboratorios, SesionDeUsuario.MAX_PAGINA);
        dicPaginas = paginator.llenarDicPaginas(laboratorios, SesionDeUsuario.MAX_PAGINA, totalPaginas);
        listaPaginas = paginator.llenarIndicePaginas(dicPaginas.keySet().stream().collect(Collectors.toList()));
        paginaActual = 1;
    }

    //</editor-fold>    
    @PostConstruct
    public void init() {
        controladorLaboratorios = new ControladorLaboratorios();
        laboratorios = controladorLaboratorios.ListarLaboratorios();
        laboratoriosSinFiltros = controladorLaboratorios.ListarLaboratorios();

        prepararPagina();
    }

    public enum TipoLaboratorio {
        Todos,
        Internos,
        Externos
    };
}

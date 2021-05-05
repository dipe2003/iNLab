/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.microbiologia.ensayos;

import controladores.microbiologia.ControladorMicrobiologia;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import modelo.microbiologia.Ensayo;
import web.helpers.MensajesWeb;
import web.helpers.Paginator;
import web.sesion.SesionDeUsuario;

/**
 *
 * @author dipe2
 */
@Named
@ViewScoped
public class ListaDeEnsayos implements Serializable {

    private ControladorMicrobiologia controladorMicrobiologia;

    private List<Ensayo> ensayos;

    public List<Ensayo> getEnsayos() {
        return ensayos;
    }

    public void setEnsayos(List<Ensayo> ensayos) {
        this.ensayos = ensayos;
    }

    public void eliminarEnsayo(Long idEnsayo) throws IOException {
        if (idEnsayo > 0) {
            if (controladorMicrobiologia.EliminarEnsayo(idEnsayo) > 0) {
                ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
                context.redirect(context.getRequestContextPath() + "/microbiologia/ensayos/listaensayos.xhtml");
                FacesContext.getCurrentInstance().renderResponse();
                FacesContext.getCurrentInstance().responseComplete();
            } else {
                MensajesWeb.MostrarError("form-lista-ensayos:mensajes-vista", "Erorr.", "No se pudo eliminar el Ensayo seleccionado.");
            }
        } else {
            MensajesWeb.MostrarError("form-lista-ensayos:mensajes-vista", "Erorr.", "No se pudo eliminar el Ensayo seleccionado");
        }
    }

    //<editor-fold desc="Filtros">
    private List<Ensayo> ensayosSinFiltro;
    private String nombreBuscar;

    public String getNombreBuscar() {
        return nombreBuscar;
    }

    public void setNombreBuscar(String nombreBuscar) {
        this.nombreBuscar = nombreBuscar;
    }

    public void botonBuscarNombre(String nombre) {
        if (nombre.equals("") || nombre.isEmpty()) {
            resetTodo();
        } else {
            ensayos = buscarNombre(ensayosSinFiltro, nombre);
        }
        prepararPagina();
    }

    public void botonResetTodo() {
        ensayos = resetTodo();
        prepararPagina();
    }

    private List buscarNombre(List<Ensayo> list, String nombre) {
        return list.stream()
                .filter(d -> d.getDenominacion().toLowerCase().startsWith(nombre.toLowerCase()))
                .collect(Collectors.toList());
    }

    private List resetTodo() {
        ensayos = ensayosSinFiltro.stream()
                .collect(Collectors.toList());
        nombreBuscar = "";
        return ensayos;
    }

    //</editor-fold>
    //<editor-fold desc="Paginas">
    private Map<Integer, List<Ensayo>> dicPaginas;
    private List<Integer> listaPaginas;
    private int paginaActual;
    private int totalPaginas;

    public Map<Integer, List<Ensayo>> getDicPaginas() {
        return dicPaginas;
    }

    public void setDicPaginas(Map<Integer, List<Ensayo>> dicPaginas) {
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
        Paginator<Ensayo> paginator = new Paginator();
        totalPaginas = paginator.calcularTotalPaginas(ensayos, SesionDeUsuario.MAX_PAGINA);
        dicPaginas = paginator.llenarDicPaginas(ensayos, SesionDeUsuario.MAX_PAGINA, totalPaginas);
        listaPaginas = paginator.llenarIndicePaginas(dicPaginas.keySet().stream().collect(Collectors.toList()));
        paginaActual = 1;
    }

    //</editor-fold>
    
    
    @PostConstruct
    public void init() {
        controladorMicrobiologia = new ControladorMicrobiologia();
        ensayos = controladorMicrobiologia.ListarEnsayos();
        ensayosSinFiltro = controladorMicrobiologia.ListarEnsayos();

        prepararPagina();
    }
}

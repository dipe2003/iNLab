/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web.microbiologia.analisis;

import controladores.microbiologia.ControladorAnalisis;
import controladores.microbiologia.ControladorLaboratorios;
import controladores.muestreos.ControladorMuestreos;
import controladores.usuario.ControladorUsuarios;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;

import javax.inject.Named;
import modelo.microbiologia.Busqueda;
import modelo.microbiologia.Laboratorio;
import modelo.microbiologia.Requisito;
import modelo.microbiologia.ValorDeteccion;
import modelo.muestreo.Muestreo;
import modelo.muestreo.TipoMuestra;
import modelo.usuarios.Usuario;
import web.helpers.MensajesWeb;

/**
 *
 * @author dipe2
 */
@ViewScoped
@Named
public class AltaDeAnalisisResultadoManual implements Serializable {

    private ControladorUsuarios controladorUsuarios;
    private ControladorAnalisis controladorAnalisis;

    private ExternalContext context;

    //<editor-fold desc="Datos del Muestreo">
    private Muestreo muestreo;
    private List<Muestreo> muestreos;
    private Long idMuestreo;
    private TipoMuestra tipoMuestra;

    public List<Muestreo> getMuestreos() {
        return muestreos;
    }

    public void setMuestreos(List<Muestreo> muestreos) {
        this.muestreos = muestreos;
    }

    public Muestreo getMuestreo() {
        return muestreo;
    }

    public void setMuestreo(Muestreo muestreo) {
        this.muestreo = muestreo;
        if (muestreo != null) {
            setRequisitos(muestreo.getMuestra().getEspecificacionMuestra().getDestino().getRequisitos());
            setTipoMuestra(muestreo.getMuestra().getEspecificacionMuestra().getTipoMuestra());
            // por lo general la fecha de muestreo coincide con la fecha de analisis
            setFechaAnalisis(muestreo.getFechaMuestreo());
        }
    }

    public Long getIdMuestreo() {
        return idMuestreo;
    }

    public void setIdMuestreo(Long idMuestreo) {
        this.idMuestreo = idMuestreo;
    }

    public TipoMuestra getTipoMuestra() {
        return tipoMuestra;
    }

    public void setTipoMuestra(TipoMuestra tipoMuestra) {
        this.tipoMuestra = tipoMuestra;
    }

    //</editor-fold>
    //<editor-fold desc="Requisito y Ensayos">
    private Requisito requisito;
    private String tipoSeleccionado;
    private List<Requisito> requisitos;
    private Long requisitoSeleccionado;

    public Requisito getRequisito() {
        return requisito;
    }

    public void setRequisito(Requisito requisito) {
        this.requisito = requisito;
    }

    public String getTipoSeleccionado() {
        return tipoSeleccionado;
    }

    public void setTipoSeleccionado(String tipoSeleccionado) {
        this.tipoSeleccionado = tipoSeleccionado;
    }

    public List<Requisito> getRequisitos() {
        return requisitos;
    }

    public void setRequisitos(List<Requisito> requisitos) {
        this.requisitos = requisitos;
        if (requisitos.size() > 0) {
            setRequisitoSeleccionado(requisitos.get(0).getId());
        }
    }

    public Long getRequisitoSeleccionado() {
        return requisitoSeleccionado;
    }

    public void setRequisitoSeleccionado(Long requisitoSeleccionado) {
        this.requisitoSeleccionado = requisitoSeleccionado;
        findRequisito(requisitoSeleccionado);
        if (requisito != null) {
            actualizarTipoResultado(requisito);
        }
    }

    private void actualizarTipoResultado(Requisito req) {
        if (req.getLimiteVigente().getClass() == Busqueda.class) {
            tipoSeleccionado = "Busqueda";
            valorResultadoBusqueda = valoresBusquedaDisponibles[0];
        } else {
            tipoSeleccionado = "Recuento";
        }
    }

    private void findRequisito(Long id) {
        if (id != null && id > 0) {
            setRequisito(requisitos.stream()
                    .filter(r -> r.getId().equals(id))
                    .findFirst()
                    .get());
        } else {
            MensajesWeb.MostrarError("form-analisis-resultados:boton-buscar-muestreo", "No se ingreso busqueda: ", "Verifica los datos ingresados o contacta con el administrador.");
        }
    }

    public void findMuestreo(Long id) {
        if (id != null && id > 0) {
            if (muestreos.stream().anyMatch(m -> m.getId().equals(id))) {
                setMuestreo(muestreos.stream()
                        .filter(m -> m.getId().equals(id))
                        .findFirst()
                        .get());
            } else {
                MensajesWeb.MostrarAdvertencia("form-analisis-resultados:boton-buscar-muestreo", "No se encontro Muestreo o no tiene analisis pendientes: ", "Verifica los datos ingresados o contacta con el administrador.");
            }
        } else {
            MensajesWeb.MostrarError("form-analisis-resultados:boton-buscar-muestreo", "No se ingreso busqueda: ", "Verifica los datos ingresados o contacta con el administrador.");
        }
    }

    //</editor-fold>
    //<editor-fold desc="Analisis">
    private List<Usuario> usuarios;
    private Long idUsuarioSeleccionado;
    private Date fechaAnalisis;
    private String observacionesAnalisis;
    private Long laboratorioSeleccionado;
    private List<Laboratorio> laboratorios;

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

    public Long getIdUsuarioSeleccionado() {
        return idUsuarioSeleccionado;
    }

    public void setIdUsuarioSeleccionado(Long idUsuarioSeleccionado) {
        this.idUsuarioSeleccionado = idUsuarioSeleccionado;
    }

    public Date getFechaAnalisis() {
        return fechaAnalisis;
    }

    public void setFechaAnalisis(Date fechaAnalisis) {
        this.fechaAnalisis = fechaAnalisis;
    }

    public String getObservacionesAnalisis() {
        return observacionesAnalisis;
    }

    public void setObservacionesAnalisis(String observacionesAnalisis) {
        this.observacionesAnalisis = observacionesAnalisis;
    }

    public Long getLaboratorioSeleccionado() {
        return laboratorioSeleccionado;
    }

    public void setLaboratorioSeleccionado(Long idLaboratorio) {
        this.laboratorioSeleccionado = idLaboratorio;
    }

    public List<Laboratorio> getLaboratorios() {
        return laboratorios;
    }

    public void setLaboratorios(List<Laboratorio> laboratorios) {
        this.laboratorios = laboratorios;
    }

    //</editor-fold>
    //<editor-fold desc="Valores de Resultado">
    private float valorResultadoRecuento;
    private ValorDeteccion valorResultadoBusqueda;
    private ValorDeteccion[] valoresBusquedaDisponibles = ValorDeteccion.values();

    public float getValorResultadoRecuento() {
        return valorResultadoRecuento;
    }

    public void setValorResultadoRecuento(float valorResultadoRecuento) {
        this.valorResultadoRecuento = valorResultadoRecuento;
    }

    public ValorDeteccion getValorResultadoBusqueda() {
        return valorResultadoBusqueda;
    }

    public void setValorResultadoBusqueda(ValorDeteccion valorResultadoBusqueda) {
        this.valorResultadoBusqueda = valorResultadoBusqueda;
    }

    public ValorDeteccion[] getValoresBusquedaDisponibles() {
        return valoresBusquedaDisponibles;
    }

    public void setValoresBusquedaDisponibles(ValorDeteccion[] valoresBusquedaDisponibles) {
        this.valoresBusquedaDisponibles = valoresBusquedaDisponibles;
    }
    //</editor-fold> 

    public void darAltaAnalisisResultado() throws IOException {
        if (requisito == null) {
            MensajesWeb.MostrarError("form-analisis-resultados:botonAltaAnalisisResultado", "No se pudo guardar: ", "La muestra seleccionada no tiene requisitos asociados.");
        } else {
            if (requisito.getLimiteVigente().getClass().getSimpleName().equals("Busqueda")) {
                if (comprobarDatos(true)) {
                    if (controladorAnalisis.AgregarResultadoBusqueda(requisito.getId(), muestreo.getId(), fechaAnalisis, observacionesAnalisis, idUsuarioSeleccionado,
                            valorResultadoBusqueda, laboratorioSeleccionado) > 0) {
                        redirigir();
                    } else {
                        MensajesWeb.MostrarError("form-analisis-resultados:botonAltaAnalisisResultado", "No se pudo guardar: ", "Verifica los datos ingresados o contacta con el administrador.");
                    }
                }
            } else {
                if (comprobarDatos(false)) {
                    if (controladorAnalisis.AgregarResultadoRecuento(requisito.getId(), muestreo.getId(), fechaAnalisis, observacionesAnalisis, idUsuarioSeleccionado,
                            valorResultadoRecuento, laboratorioSeleccionado) > 0) {
                        redirigir();
                    } else {
                        MensajesWeb.MostrarError("form-analisis-resultados:botonAltaAnalisisResultado", "No se pudo guardar: ", "Verifica los datos ingresados o contacta con el administrador.");
                    }
                }
            }
        }
    }

    private boolean comprobarDatos(boolean tipoBusqueda) {
        boolean res = true;
        if (fechaAnalisis == null) {
            MensajesWeb.MostrarError("form-analisis-resultados:fecha-analisis", "Faltan Datos: ", "No se ingreso Fecha de Analisis.");
            res = false;
        } else {
            if (fechaAnalisis.before(muestreo.getFechaMuestreo())) {
                MensajesWeb.MostrarError("form-analisis-resultados:fecha-analisis", "Fecha de Analisis: ", "La fecha de Analisis no puede ser anterior al muestreo.");
                res = false;
            }
        }
        if (idUsuarioSeleccionado == null || idUsuarioSeleccionado.equals(0)) {
            MensajesWeb.MostrarError("form-analisis-resultados:usuario-analista", "Faltan Datos: ", "No se selecciono Analista.");
            res = false;
        }

        if (laboratorioSeleccionado == null || laboratorioSeleccionado.equals(0)) {
            MensajesWeb.MostrarError("form-analisis-resultados:select-laboratorio", "Faltan Datos: ", "No se selecciono Laboratorio.");
            res = false;
        }

        if (tipoBusqueda) {
            if (valorResultadoBusqueda == null) {
                MensajesWeb.MostrarError("form-analisis-resultados:panel-valores-busqueda", "Faltan Datos: ", "No se ingreso Resultado.");
                res = false;
            }
        }

        if (!tipoBusqueda) {
            if (valorResultadoRecuento < 0f) {
                MensajesWeb.MostrarError("form-analisis-resultados:panel-valores-recuento", "Faltan Datos: ", "No se ingreso Resultado o no es correcto.");
                res = false;
            }
        }
        return res;
    }

    public void redirigir() throws IOException {
        if (context.isResponseCommitted()) {
            context = FacesContext.getCurrentInstance().getExternalContext();
        }
        context.redirect(context.getRequestContextPath() + "/muestreos/listamuestreosconresultados.xhtml?tipomuestra=" + tipoMuestra);
        FacesContext.getCurrentInstance().renderResponse();
        FacesContext.getCurrentInstance().responseComplete();
    }

    @PostConstruct
    public void init() {
        try {
            context = FacesContext.getCurrentInstance().getExternalContext();

            controladorAnalisis = new ControladorAnalisis();
            controladorUsuarios = new ControladorUsuarios();
            muestreos = new ControladorMuestreos().ListarMuestreos()
                    .stream()
                    .filter(m -> !m.isHabilitado() && !m.EstaAnalizado())
                    .collect(Collectors.toList());

            usuarios = controladorUsuarios.ListarUsuarios(true);
            // solo si el usuario logueado es analista.
            // de lo contrario se debe seleccionar el analista de la lista.
//            if (sesion.getUsuarioLogueado().getTipo() == Permiso.Analista) {
//                idUsuarioSeleccionado = sesion.getUsuarioLogueado().getId();
//            }
            laboratorios = new ControladorLaboratorios().ListarLaboratorios();

        } catch (Exception ex) {
            //TODO: redirigir a error
        }
    }
}

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
import javax.annotation.PostConstruct;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;

import javax.inject.Named;
import modelo.microbiologia.Analisis;
import modelo.microbiologia.Busqueda;
import modelo.microbiologia.Laboratorio;
import modelo.microbiologia.Requisito;
import modelo.microbiologia.ResultadoBusqueda;
import modelo.microbiologia.ResultadoRecuento;
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
public class EditarAnalisisResultado implements Serializable {

    private ControladorUsuarios controladorUsuarios;
    private ControladorMuestreos controladorMuestreos;
    private ControladorAnalisis controladorAnalisis;

    private ExternalContext context;

    //<editor-fold desc="Datos del Muestreo">
    private Muestreo muestreo;
    private Analisis analisisSeleccionado;
    private Long idRequisitoSeleccionado;
    private TipoMuestra tipoMuestra;

    public Muestreo getMuestreo() {
        return muestreo;
    }

    public void setMuestreo(Muestreo muestreo) {
        this.muestreo = muestreo;
    }

    private Requisito requisito;

    public Requisito getRequisito() {
        return requisito;
    }

    public void setRequisito(Requisito requisito) {
        this.requisito = requisito;
    }

    public Long getIdRequisitoSeleccionado() {
        return idRequisitoSeleccionado;
    }

    public void setIdRequisitoSeleccionado(Long idRequisitoSeleccionado) {
        this.idRequisitoSeleccionado = idRequisitoSeleccionado;
    }

    private String tipoSeleccionado;

    public String getTipoSeleccionado() {
        return tipoSeleccionado;
    }

    public void setTipoSeleccionado(String tipoSeleccionado) {
        this.tipoSeleccionado = tipoSeleccionado;
    }

    //</editor-fold>
    //<editor-fold desc="Analisis">
    private List<Usuario> usuarios;
    private Long idUsuarioSeleccionado;
    private Date fechaAnalisis;
    private String observacionesAnalisis;
    private Long idLaboratorioSeleccionado;
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

    public Long getIdLaboratorioSeleccionado() {
        return idLaboratorioSeleccionado;
    }

    public void setIdLaboratorioSeleccionado(Long idLaboratorio) {
        this.idLaboratorioSeleccionado = idLaboratorio;
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

    public void guardarAnalisisResultado() throws IOException {
        if (requisito == null) {
            MensajesWeb.MostrarError("form-editar-analisis-resultados:botonEditarAnalisis", "No se pudo guardar: ", "La muestra seleccionada no tiene requisitos asociados.");
        } else {
            if (requisito.getLimiteVigente().getClass().getSimpleName().equals("Busqueda")) {
                if (comprobarDatos(true)) {
                    if (controladorAnalisis.ActualizarAnalisisBusqueda(analisisSeleccionado.getId(), muestreo.getId(), valorResultadoBusqueda, idUsuarioSeleccionado,
                            fechaAnalisis, observacionesAnalisis, idLaboratorioSeleccionado) > 0) {
                        redirigir();
                    } else {
                        MensajesWeb.MostrarError("form-editar-analisis-resultados:botonEditarAnalisis", "No se pudo guardar: ", "Verifica los datos ingresados o contacta con el administrador.");
                    }
                }
            } else {
                if (comprobarDatos(false)) {
                    if (controladorAnalisis.ActualizarAnalisisRecuento(analisisSeleccionado.getId(), muestreo.getId(), valorResultadoRecuento, idUsuarioSeleccionado, fechaAnalisis,
                            observacionesAnalisis, idLaboratorioSeleccionado) > 0) {
                        redirigir();
                    } else {
                        MensajesWeb.MostrarError("form-editar-analisis-resultados:botonEditarAnalisis", "No se pudo guardar: ", "Verifica los datos ingresados o contacta con el administrador.");
                    }
                }
            }
        }
    }

    private boolean comprobarSiHayCambios() {
        if (tipoSeleccionado == "Busqueda") {
            if (!analisisSeleccionado.getFechaAnalisis().equals(fechaAnalisis) || !analisisSeleccionado.getLaboratorio().getId().equals(idLaboratorioSeleccionado)
                    || !analisisSeleccionado.getUsuarioAnalista().getId().equals(idUsuarioSeleccionado) || ((ResultadoBusqueda) analisisSeleccionado.getResultado()).getValor() != valorResultadoBusqueda) {
                return true;
            }
        } else {
            if (!analisisSeleccionado.getFechaAnalisis().equals(fechaAnalisis) || !analisisSeleccionado.getLaboratorio().getId().equals(idLaboratorioSeleccionado)
                    || !analisisSeleccionado.getUsuarioAnalista().getId().equals(idUsuarioSeleccionado) || ((ResultadoRecuento) analisisSeleccionado.getResultado()).getValor() != valorResultadoRecuento) {
                return true;
            }
        }
        return false;
    }

    private boolean comprobarDatos(boolean tipoBusqueda) {
        boolean res = true;
        if (fechaAnalisis == null) {
            MensajesWeb.MostrarError("form-editar-analisis:fecha-analisis", "Faltan Datos: ", "No se ingreso Fecha de Analisis.");
            res = false;
        } else {
            if (fechaAnalisis.before(muestreo.getFechaMuestreo())) {
                MensajesWeb.MostrarError("form-editar-analisis:fecha-analisis", "Fecha de Analisis: ", "La fecha de Analisis no puede ser anterior al muestreo.");
                res = false;
            }
        }
        if (idUsuarioSeleccionado == null || idUsuarioSeleccionado.equals(0)) {
            MensajesWeb.MostrarError("form-editar-analisis:usuario-analista", "Faltan Datos: ", "No se selecciono Analista.");
            res = false;
        }

        if (idLaboratorioSeleccionado == null || idLaboratorioSeleccionado.equals(0)) {
            MensajesWeb.MostrarError("form-editar-analisis:select-laboratorio", "Faltan Datos: ", "No se selecciono Laboratorio.");
            res = false;
        }

        if (tipoBusqueda) {
            if (valorResultadoBusqueda == null) {
                MensajesWeb.MostrarError("form-editar-analisis:panel-valores-busqueda", "Faltan Datos: ", "No se ingreso Resultado.");
                res = false;
            }
        }

        if (!tipoBusqueda) {
            if (valorResultadoRecuento < 0f) {
                MensajesWeb.MostrarError("form-editar-analisis:panel-valores-recuento", "Faltan Datos: ", "No se ingreso Resultado o no es correcto.");
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
            Long idMuestreo = 0L;
            Long idEnsayo = 0L;
            idMuestreo = Long.valueOf(context.getRequestParameterMap().get("idmuestreo"));
            idEnsayo = Long.valueOf(context.getRequestParameterMap().get("idensayo"));
            if (idMuestreo != 0) {
                controladorAnalisis = new ControladorAnalisis();
                controladorMuestreos = new ControladorMuestreos();
                muestreo = controladorMuestreos.ObtenerMuestreo(idMuestreo);
                laboratorios = new ControladorLaboratorios().ListarLaboratorios();

                llenarDatosAnalisisResultado(muestreo.FindAnalisis(idEnsayo));

                //TODO: revisar scope
                controladorUsuarios = new ControladorUsuarios();
                usuarios = controladorUsuarios.ListarUsuarios(true);

            }
        } catch (Exception ex) {
            //TODO: redirigir a error
        }
    }

    private void llenarDatosAnalisisResultado(Analisis analisis) {
        analisisSeleccionado = analisis;
        requisito = analisis.getRequisito();
        tipoMuestra = analisis.getMuestreo().getMuestra().getEspecificacionMuestra().getTipoMuestra();
        if (requisito.getLimiteVigente().getClass() == Busqueda.class) {
            tipoSeleccionado = "Busqueda";
            valorResultadoBusqueda = ((ResultadoBusqueda) analisis.getResultado()).getValor();
        } else {
            tipoSeleccionado = "Recuento";
            valorResultadoRecuento = ((ResultadoRecuento) analisis.getResultado()).getValor();
        }
        idUsuarioSeleccionado = analisis.getUsuarioAnalista().getId();
        fechaAnalisis = analisis.getFechaAnalisis();
        observacionesAnalisis = analisis.getObservaciones();
        idLaboratorioSeleccionado = analisis.getLaboratorio().getId();
    }
}

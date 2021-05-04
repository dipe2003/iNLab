/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores.microbiologia;

import java.util.Date;
import java.util.Objects;
import modelo.microbiologia.Analisis;
import modelo.microbiologia.Ensayo;
import modelo.microbiologia.Laboratorio;
import modelo.microbiologia.Requisito;
import modelo.microbiologia.ResultadoBusqueda;
import modelo.microbiologia.ResultadoRecuento;
import modelo.microbiologia.ValorDeteccion;
import modelo.muestreo.Muestreo;
import modelo.usuarios.Usuario;
import persistencia.generica.FabricaRepositorio;
import persistencia.generica.RepositorioPersistencia;

/**
 *
 * @author dipe2
 */
public class ControladorAnalisis {

    private final FabricaRepositorio fabricaRepositorio = new FabricaRepositorio();

    private final RepositorioPersistencia<Usuario> repositorioUsuarios;
    private final RepositorioPersistencia<Analisis> repositorioAnalisis;
    private final RepositorioPersistencia<Laboratorio> repositorioLaboratorio;
    private final RepositorioPersistencia<Ensayo> repositorioEnsayos;
    private final RepositorioPersistencia<Muestreo> repositorioMuestreo;

    public ControladorAnalisis() {
        repositorioUsuarios = fabricaRepositorio.GetRepositorioUsuarios();
        repositorioAnalisis = fabricaRepositorio.GetRepositorioAnalisis();
        repositorioLaboratorio = fabricaRepositorio.GetRepositorioLaboratorio();
        repositorioEnsayos =  fabricaRepositorio.GetRepositorioEnsayos();
        repositorioMuestreo = fabricaRepositorio.GetRepositorioMuestreo();
    }

    //</editor-fold>
    // <editor-fold desc="Resultados">
    public Long AgregarResultadoRecuento(Long idRequisito, Long idMuestreo, Date fechaAnalisis, String observaciones, Long idAnalista, float resultado, Long idLaboratorio) {
        Muestreo muestreo = repositorioMuestreo.Find(idMuestreo);
        Requisito requisito = muestreo.getMuestra().getEspecificacionMuestra().getDestino().FindRequisito(idRequisito);
        Usuario usuario = repositorioUsuarios.Find(idAnalista);
        Laboratorio laboratorio = repositorioLaboratorio.Find(idLaboratorio);
        try {
            Analisis analisis = laboratorio.AgregarAnalisis(requisito, fechaAnalisis, observaciones, usuario, muestreo);
            analisis.AgregarResultadoRecuento(resultado);
            return repositorioLaboratorio.Update(laboratorio).getId();
        } catch (Exception ex) {
        }
        return -1L;
    }

    public Long AgregarResultadoRecuento(Long idAnalisis, Long idMuestreo, float resultado) {
        Muestreo muestreo = repositorioMuestreo.Find(idMuestreo);
        try {
            muestreo.FindAnalisis(idAnalisis).AgregarResultadoRecuento(resultado);
            return repositorioMuestreo.Update(muestreo).getId();
        } catch (Exception ex) {
        }
        return -1L;
    }

    public Long ActualizarAnalisisRecuento(Long idAnalisis, Long idMuestreo, float resultado, Long idAnalista, Date fecha, String observaciones, Long idLaboratorio) {
        Muestreo muestreo = repositorioMuestreo.Find(idMuestreo);
        try {
            Analisis analisis = muestreo.FindAnalisis(idAnalisis);
            if (!Objects.equals(idAnalista, analisis.getUsuarioAnalista().getId())) {
                analisis.setUsuarioAnalista(repositorioUsuarios.Find(idAnalista));
            }
            ((ResultadoRecuento) analisis.getResultado()).setValor(resultado);
            analisis.setFechaAnalisis(fecha);
            analisis.setObservaciones(observaciones);
            if (!Objects.equals(idLaboratorio, analisis.getLaboratorio().getId())) {
                analisis.setLaboratorio(repositorioLaboratorio.Find(idLaboratorio));
            }
            return repositorioMuestreo.Update(muestreo).getId();
        } catch (Exception ex) {
        }
        return -1L;
    }

    public Long ActualizarRequisitoAnalisis(Long idAnalisis, Long idRequisito, Long idMuestreo) {
        Muestreo muestreo = repositorioMuestreo.Find(idMuestreo);
        try {
            Ensayo ensayo = repositorioEnsayos.Find(muestreo.FindAnalisis(idAnalisis).getRequisito().getEnsayo().getId());
            Requisito requisito = ensayo.getRequisitos()
                    .stream().filter(req -> req.getId()
                    .equals(idRequisito))
                    .findFirst()
                    .get();
            muestreo.FindAnalisis(idAnalisis).setRequisito(requisito);
            return repositorioMuestreo.Update(muestreo).getId();
        } catch (Exception e) {
        }
        return -1L;
    }

    public Long AgregarResultadoBusqueda(Long idRequisito, Long idMuestreo, Date fechaAnalisis, String observaciones, Long idAnalista, ValorDeteccion resultado,
            Long idLaboratorio) {
        Muestreo muestreo = repositorioMuestreo.Find(idMuestreo);
        Requisito requisito = muestreo.getMuestra().getEspecificacionMuestra().getDestino().FindRequisito(idRequisito);
        Usuario usuario = repositorioUsuarios.Find(idAnalista);
        Laboratorio laboratorio = repositorioLaboratorio.Find(idLaboratorio);
        try {
            Analisis analisis = laboratorio.AgregarAnalisis(requisito, fechaAnalisis, observaciones, usuario, muestreo);
            analisis.AgregarResultadoBusqueda(resultado);
            return repositorioLaboratorio.Update(laboratorio).getId();
        } catch (Exception ex) {
        }
        return -1L;
    }

    public Long AgregarResultadoBusqueda(Long idAnalisis, Long idMuestreo, ValorDeteccion resultado) {
        Muestreo muestreo = repositorioMuestreo.Find(idMuestreo);
        try {
            muestreo.FindAnalisis(idAnalisis).AgregarResultadoBusqueda(resultado);
            return repositorioMuestreo.Update(muestreo).getId();
        } catch (Exception ex) {
        }
        return -1L;
    }

    public Long ActualizarAnalisisBusqueda(Long idAnalisis, Long idMuestreo, ValorDeteccion resultado, Long idAnalista, Date fecha, String observaciones,
            Long idLaboratorio) {
        Muestreo muestreo = repositorioMuestreo.Find(idMuestreo);
        try {
            Analisis analisis = muestreo.FindAnalisis(idAnalisis);
            if (!Objects.equals(idAnalista, analisis.getUsuarioAnalista().getId())) {
                analisis.setUsuarioAnalista(repositorioUsuarios.Find(idAnalista));
            }
            ((ResultadoBusqueda) analisis.getResultado()).setValor(resultado);
            analisis.setFechaAnalisis(fecha);
            analisis.setObservaciones(observaciones);
            if (!Objects.equals(idLaboratorio, analisis.getLaboratorio().getId())) {
                analisis.setLaboratorio(repositorioLaboratorio.Find(idLaboratorio));
            }
            return repositorioMuestreo.Update(muestreo).getId();
        } catch (Exception ex) {
        }
        return -1L;
    }

    public Long CrearAnalisis(Long idRequisito, Long idMuestreo, Date fechaAnalisis, String observaciones, Long idAnalista, Long idLaboratorio) {
        Muestreo muestreo = repositorioMuestreo.Find(idMuestreo);
        Requisito requisito = muestreo.getMuestra().getEspecificacionMuestra().getDestino().FindRequisito(idRequisito);
        Usuario usuario = repositorioUsuarios.Find(idAnalista);
        Laboratorio laboratorio = repositorioLaboratorio.Find(idLaboratorio);
        try {
            return repositorioAnalisis.Create(laboratorio.AgregarAnalisis(requisito, fechaAnalisis, observaciones, usuario, muestreo)).getId();
        } catch (Exception ex) {
        }
        return -1L;
    }

    public Long EliminarAnalisis(Long idAnalisis) {
        try {
            Analisis a = repositorioAnalisis.Find(idAnalisis);
            repositorioAnalisis.Delete(a);
            return 1L;
        } catch (Exception e) {
            System.out.println("Error al eliminar analisis: " + e.getMessage());
        }
        return -1L;
    }

}

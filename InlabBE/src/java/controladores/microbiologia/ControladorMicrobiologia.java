/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores.microbiologia;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.ejb.Stateless;
import modelo.microbiologia.Busqueda;
import modelo.microbiologia.Ensayo;
import modelo.microbiologia.EstadoVerificacion;
import modelo.microbiologia.Recuento;
import modelo.microbiologia.Requisito;
import modelo.microbiologia.ValorDeteccion;
import modelo.muestreo.Destino;
import modelo.muestreo.Muestreo;
import modelo.usuarios.Usuario;
import persistencia.generica.FabricaRepositorio;
import persistencia.generica.RepositorioPersistencia;

/**
 *
 * @author dipe2
 */
@Stateless
public class ControladorMicrobiologia {

    private final FabricaRepositorio fabricaRepositorio = new FabricaRepositorio();

    private final RepositorioPersistencia<Ensayo> repositorioEnsayos;
    private final RepositorioPersistencia<Muestreo> repositorioMuestreos;
    private final RepositorioPersistencia<Usuario> repositorioUsuarios;
    private final RepositorioPersistencia<Destino> repositorioDestinos;
    private final RepositorioPersistencia<Requisito> repositorioRequisitos;

    public ControladorMicrobiologia() {
        repositorioUsuarios = fabricaRepositorio.GetRepositorioUsuarios();
        repositorioEnsayos = fabricaRepositorio.GetRepositorioEnsayos();
        repositorioDestinos = fabricaRepositorio.GetRepositorioDestinos();
        repositorioMuestreos = fabricaRepositorio.GetRepositorioMuestreo();
        repositorioRequisitos = fabricaRepositorio.GetRepositorioRequisitos();
    }

    // <editor-fold desc="Ensayos">
    public Long CrearEnsayo(String denominacion) {
        Ensayo ensayo = new Ensayo(denominacion);
        return repositorioEnsayos.Create(ensayo).getId();
    }

    public Long ActualizarEnsayo(Long idEnsayo, String nuevaDenominacion) {
        Ensayo ensayo = repositorioEnsayos.Find(idEnsayo);
        ensayo.setDenominacion(nuevaDenominacion);
        return repositorioEnsayos.Update(ensayo).getId();
    }

    public List<Ensayo> ListarEnsayos() {
        return repositorioEnsayos.findAll()
                .stream()
                .sorted()
                .collect(Collectors.toList());
    }

    public Ensayo ObtenerEnsayo(Long idEnsayo) {
        return repositorioEnsayos.Find(idEnsayo);
    }

    public Long EliminarEnsayo(Long idEnsayo) {
        try {
            Ensayo ensayo = repositorioEnsayos.Find(idEnsayo);
            repositorioEnsayos.Delete(ensayo);
            return 1L;
        } catch (Exception e) {
            System.out.println("Error al eliminar Ensayo: " + e.getMessage());
        }
        return -1L;
    }

    //</editor-fold>
    // <editor-fold desc="Requisitos">
    public Long CrearRequisito(Long idEnsayo, Long idDestino) {
        Ensayo ensayo = repositorioEnsayos.Find(idEnsayo);
        Destino destino = repositorioDestinos.Find(idDestino);
        ensayo.AregarRequisito(destino);
        return repositorioEnsayos.Update(ensayo).getId();
    }

    public Long EliminarRequisito(Long idRequisito, Long idEnsayo) {
        try {
            Requisito requisito = repositorioRequisitos.Find(idRequisito);
            repositorioRequisitos.Delete(requisito);
            return 1L;
        } catch (Exception ex) {
            System.out.println("Error al eliminar Requisito: " + ex.getMessage());
        }
        return -1L;
    }

    //</editor-fold>
    // <editor-fold desc="Limites">
    public Long AgregarLimiteRecuento(Long idEnsayo, Long idRequisito, float marginal, float inaceptable) {
        Ensayo ensayo = repositorioEnsayos.Find(idEnsayo);
        try {
            ensayo.FindRequisito(idRequisito).AgregarRecuento(marginal, inaceptable);
            return repositorioEnsayos.Update(ensayo).getId();
        } catch (Exception ex) {
            System.out.println("Error al agregar limite: " + ex.getMessage());
        }
        return -1L;
    }

    public Long AgregarLimiteBusqueda(Long idEnsayo, Long idRequisito, ValorDeteccion valorInaceptable, ValorDeteccion valorAceptable) {
        Ensayo ensayo = repositorioEnsayos.Find(idEnsayo);
        try {
            ensayo.FindRequisito(idRequisito).AgregarBusqueda(valorInaceptable, valorAceptable);
            return repositorioEnsayos.Update(ensayo).getId();
        } catch (Exception ex) {
            System.out.println("Error al agregar limite: " + ex.getMessage());
        }
        return -1L;
    }

    public Long ActualizarLimiteBusqueda(Long idEnsayo, Long idRequisito, ValorDeteccion nuevoValorInaceptable, ValorDeteccion nuevoValorAceptable) {
        Ensayo ensayo = repositorioEnsayos.Find(idEnsayo);
        try {
            Busqueda limite = (Busqueda) ensayo.FindRequisito(idRequisito).getLimiteVigente();
            limite.setValorBusquedaAceptable(nuevoValorAceptable);
            limite.setValorBusquedaInaceptable(nuevoValorInaceptable);
            return repositorioEnsayos.Update(ensayo).getId();
        } catch (Exception ex) {
            System.out.println("Error al actualizar limite: " + ex.getMessage());
        }
        return -1L;
    }

    public Long ActualizarLimiteRecuento(Long idEnsayo, Long idRequisito, float nuevoMarginal, float nuevoInaceptable) {
        Ensayo ensayo = repositorioEnsayos.Find(idEnsayo);
        try {
            Recuento limite = (Recuento) ensayo.FindRequisito(idRequisito).getLimiteVigente();
            limite.setValorRecuentoMarginal(nuevoMarginal);
            limite.setValorRecuentoInaceptable(nuevoInaceptable);
            return repositorioEnsayos.Update(ensayo).getId();
        } catch (Exception ex) {
            System.out.println("Error al actualizar limite: " + ex.getMessage());
        }
        return -1L;
    }

    //</editor-fold>
    // <editor-fold desc="Verificacion">
    public Long AgregarVerificacion(Long idMuestreo, Long idVerificador, String observaciones, EstadoVerificacion estado) {
        Muestreo muestreo = repositorioMuestreos.Find(idMuestreo);
        try {
            muestreo.AgregarVerificacion(repositorioUsuarios.Find(idVerificador), estado, observaciones);
            return repositorioMuestreos.Update(muestreo).getId();
        } catch (Exception ex) {
            System.out.println("Error al agregar verificacion: " + ex.getMessage());
        }
        return -1L;
    }

    public Long ActualizarVerificacion(Long idAnalisis, Long idMuestreo, Long idVerificador, String observaciones, EstadoVerificacion estado) {
        Muestreo muestreo = repositorioMuestreos.Find(idMuestreo);
        try {
            muestreo.FindAnalisis(idAnalisis);
            if (!Objects.equals(muestreo.getVerificacion().getUsuarioVerificador().getId(), idVerificador)) {
                muestreo.getVerificacion().setUsuarioVerificador(repositorioUsuarios.Find(idVerificador));
            }
            muestreo.getVerificacion().setObservaciones(observaciones);
            muestreo.getVerificacion().setResultado(estado);
            return repositorioMuestreos.Update(muestreo).getId();
        } catch (Exception ex) {
            System.out.println("Error al actualizacion verificacion: " + ex.getMessage());
        }
        return -1L;
    }
    //</editor-fold>
}

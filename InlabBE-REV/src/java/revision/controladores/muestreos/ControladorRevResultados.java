/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package revision.controladores.muestreos;

import java.util.Date;
import revision.modelo.microbiologia.RevResultado;
import revision.modelo.microbiologia.ValorDeteccion;
import revision.modelo.muestreos.RevMuestreo;
import revision.persistencia.FabricaRepositorio;
import revision.persistencia.RepositorioPersistencia;

/**
 *
 * @author dipe2
 */
public class ControladorRevResultados {

    private final RepositorioPersistencia<RevMuestreo> repositorioRevMuestreos;
    private final FabricaRepositorio fabricaRepositorio = new FabricaRepositorio();

    public ControladorRevResultados() {
        repositorioRevMuestreos = fabricaRepositorio.GetRepositorioRevMuestreo();
    }

    public Long QuitarResultado(Long idMuestreo, Long idEnsayo) {
        try {
            RevMuestreo m = repositorioRevMuestreos.Find(idMuestreo);
            RevResultado res = m.GetRevResultado(idEnsayo);
            m.RemoveRevResultado(res);
            return repositorioRevMuestreos.Update(m).getId();
        } catch (Exception e) {
            System.out.println("Error al eliminar el RevResultado: " + e.getMessage());
        }
        return -1L;
    }

    public Long AgregarResultadoRecuento(Long idResultado, Long idMuestreo, String requisito, String analista, String laboratorio, String observaciones,
            float resultadoRecuento, float limiteRecuento, Date fechaResultado) {
        try {
            RevMuestreo muestreo = repositorioRevMuestreos.Find(idMuestreo);
            muestreo.CrearResultadoRecuento(idResultado, requisito, analista, laboratorio, observaciones, resultadoRecuento, limiteRecuento, fechaResultado);
            return repositorioRevMuestreos.Update(muestreo).getId();
        } catch (Exception ex) {
        }
        return 0L;
    }

    public Long AgregarResultadoBusqueda(Long idResultado, Long idMuestreo, String requisito, String analista, String laboratorio, String observaciones,
            ValorDeteccion resultadoDeteccion, ValorDeteccion limiteDeteccion, Date fechaResultado) {
        try {
            RevMuestreo muestreo = repositorioRevMuestreos.Find(idMuestreo);
            muestreo.CrearResultadoDeteccion(idResultado, requisito, analista, laboratorio, observaciones, resultadoDeteccion, limiteDeteccion, fechaResultado);
            return repositorioRevMuestreos.Update(muestreo).getId();
        } catch (Exception ex) {
        }
        return 0L;
    }

}

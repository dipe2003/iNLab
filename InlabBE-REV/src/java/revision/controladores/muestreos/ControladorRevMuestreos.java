/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package revision.controladores.muestreos;

import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import revision.modelo.muestreos.RevMuestreo;
import revision.modelo.muestreos.RevMuestreoGenerica;
import revision.modelo.muestreos.RevMuestreoProducto;
import revision.persistencia.FabricaRepositorio;
import revision.persistencia.RepositorioPersistencia;

/**
 *
 * @author dipe2
 */
@Stateless
public class ControladorRevMuestreos {

    private final FabricaRepositorio fabricaRepositorio = new FabricaRepositorio();
    private final RepositorioPersistencia<RevMuestreo> repotitorioRevMuestreos;

    public ControladorRevMuestreos() {
        repotitorioRevMuestreos = fabricaRepositorio.GetRepositorioRevMuestreo();
    }

    public Long CrearMuestreoProducto(Long idMuestreo, String denominacion, String destino, String monitor, String observaciones, String area, Date fechaMuestreo,
            Date fechaProduccion, Date fechaOrigen, boolean esRepeticion) {
        try {
            RevMuestreo muestreo = new RevMuestreoProducto(idMuestreo, denominacion, destino, monitor, observaciones, area, esRepeticion, fechaMuestreo,
                    fechaProduccion, fechaOrigen);
            return repotitorioRevMuestreos.Create(muestreo).getId();
        } catch (Exception ex) {
        }
        return 0L;
    }

    public Long CrearMuestreoGenerica(Long idMuestreo, String denominacion, String destino, String monitor, String observaciones, String area,
            Date fechaMuestreo, boolean esRepeticion, boolean contactaProducto) {
        try {
            RevMuestreo muestreo = new RevMuestreoGenerica(idMuestreo, denominacion, destino, monitor, observaciones, area, esRepeticion, fechaMuestreo, contactaProducto);
            return repotitorioRevMuestreos.Create(muestreo).getId();
        } catch (Exception ex) {
        }
        return 0L;
    }

    public List<RevMuestreo> ListarRevMuestreos() {
        return repotitorioRevMuestreos.findAll();
    }

    public RevMuestreo ObtenerRevMuestreo(Long idMuestreo) {
        return repotitorioRevMuestreos.Find(idMuestreo);
    }
    
    public Long EliminarMuestreo(Long idMuestreo){
        try {
            RevMuestreo m = repotitorioRevMuestreos.Find(idMuestreo);
            repotitorioRevMuestreos.Delete(m);
            return 1L;
        } catch (Exception e) {
            System.out.println("Error al eliminar el RevMuestreo: " + e.getMessage());
        }
        return -1L;
    }
    
}

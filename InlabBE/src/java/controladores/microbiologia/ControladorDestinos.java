/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores.microbiologia;

import java.util.List;
import java.util.stream.Collectors;
import javax.ejb.Stateless;
import modelo.muestreo.Destino;
import persistencia.generica.FabricaRepositorio;
import persistencia.generica.RepositorioPersistencia;

/**
 *
 * @author dipe2
 */
@Stateless
public class ControladorDestinos {

    private final FabricaRepositorio fabricaRepositorio = new FabricaRepositorio();
    private final RepositorioPersistencia<Destino> repositorioDestinos;

    public ControladorDestinos() {
        repositorioDestinos = fabricaRepositorio.GetRepositorioDestinos();
    }

    public Long CrearDestino(String nombre) {
        Destino destino = new Destino(nombre);
        return repositorioDestinos.Create(destino).getId();
    }

    public Long ActualizarDestino(Long idDestino, String nuevoNombre) {
        Destino destino = repositorioDestinos.Find(idDestino);
        destino.setDenominacion(nuevoNombre);
        return repositorioDestinos.Update(destino).getId();
    }

    public List<Destino> ListarDestinos() {
        return repositorioDestinos.findAll()
                .stream()
                .sorted()
                .collect(Collectors.toList());
    }

    public Destino ObtenerDestino(Long idDestino) {
        return repositorioDestinos.Find(idDestino);
    }

    public Long EliminarDestino(Long idDestino) {
        try {
            Destino destino = repositorioDestinos.Find(idDestino);
            repositorioDestinos.Delete(destino);
            return 1L;
        } catch (Exception e) {
            System.out.println("Error al eliminar Destino: " + e.getMessage());
        }
        return -1L;
    }
}

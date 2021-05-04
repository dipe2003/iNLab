/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controladores.usuario;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.ejb.Stateless;
import modelo.usuarios.ControladorSeguridad;
import modelo.usuarios.Credencial;
import modelo.usuarios.Permiso;
import modelo.usuarios.Usuario;
import persistencia.generica.FabricaRepositorio;
import persistencia.generica.RepositorioPersistencia;

/**
 *
 * @author dipe2
 */
@Stateless
public class ControladorUsuarios {
    private final ControladorSeguridad cSeg = new ControladorSeguridad();

    private final FabricaRepositorio fabricaRepositorio = new FabricaRepositorio();

    private final RepositorioPersistencia<Usuario> repositorioUsuarios;

    public ControladorUsuarios() {
       repositorioUsuarios = fabricaRepositorio.GetRepositorioUsuarios();
    }

    public Long CrearUsuario(String nombre, Permiso tipo, String password) {
        Usuario usuario = new Usuario(nombre, tipo);
        String[] cred = cSeg.getPasswordSeguro(password);
        Credencial credencial = new Credencial(cred[1], cred[0]);
        usuario.setCredencialUsuario(credencial);
        credencial.setUsuarioCredencial(usuario);
        return repositorioUsuarios.Create(usuario).getId();
    }

    public Long DarDeBajaUsuario(Long idUsuario, Date fecha) {
        Usuario usuario = repositorioUsuarios.Find(idUsuario);
        usuario.setFechaObsoleto(fecha);
        return repositorioUsuarios.Update(usuario).getId();
    }

    public Long ActualizarUsuario(Long idUsuario, String nuevoNombre, Permiso tipo) {
        Usuario usuario = repositorioUsuarios.Find(idUsuario);
        usuario.setNombre(nuevoNombre);
        usuario.setTipo(tipo);
        return repositorioUsuarios.Update(usuario).getId();
    }

    public Long DarDeAltaUsuario(Long idUsuario) {
        Usuario usuario = repositorioUsuarios.Find(idUsuario        );
        usuario.setFechaObsoleto(null);
        return repositorioUsuarios.Update(usuario).getId();
    }

    public Long ActualizarCredencial(Long idUsuario, String nuevoPassword) {
        Usuario usuario = repositorioUsuarios.Find(idUsuario);
        String[] cred = cSeg.getPasswordSeguro(nuevoPassword);
        usuario.getCredencialUsuario().setPassword(cred[1]);
        usuario.getCredencialUsuario().setPasswordKey(cred[0]);
        return repositorioUsuarios.Update(usuario).getId();
    }

    public List<Permiso> Permisos() {
        List<Permiso> permisos = new ArrayList<>();
        Permiso[] ps = Permiso.values();
        for (Permiso p : ps) {
            permisos.add(p);
        }
        return permisos;
    }

    public List<Usuario> ListarUsuarios(boolean incluirObsoletos) {
        if (!incluirObsoletos) {
            return repositorioUsuarios.findAll()
                    .stream()
                    .filter(u -> u.getFechaObsoleto() == null)
                    .sorted()
                    .collect(Collectors.toList());
        }
        return repositorioUsuarios.findAll()
                .stream()
                .sorted()
                .collect(Collectors.toList());
    }

    public Usuario ObtenerUsuario(Long id, String password) {
        Usuario usuario = repositorioUsuarios.findAll()
                .stream()
                .filter(u -> u.getId().equals(id))
                .findFirst()
                .get();
        if (usuario.getCredencialUsuario()
                .getPassword()
                .equals(new ControladorSeguridad().getPasswordSeguro(password, usuario.getCredencialUsuario().getPasswordKey()))) {
            return usuario;
        }
        return null;
    }

    public Usuario ObtenerUsuario(Long id) {
        return repositorioUsuarios.Find(id);
    }
}

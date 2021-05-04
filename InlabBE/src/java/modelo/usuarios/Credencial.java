/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo.usuarios;

import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

/**
 *
 * @author dipe2
 */
@Entity
public class Credencial implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String Password = new String();
    private String PasswordKey = new String();

    @OneToOne(cascade = CascadeType.ALL)
    private Usuario usuarioCredencial;
    
    //  Constructores
    public Credencial() {
    }

    public Credencial(String Password, String PasswordKey) {
        this.Password = Password;
        this.PasswordKey = PasswordKey;
    }

    public Long getId() {
        return id;
    }

    public String getPassword() {
        return Password;
    }

    public String getPasswordKey() {
        return PasswordKey;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setPassword(String Password) {
        this.Password = Password;
    }

    public void setPasswordKey(String PasswordKey) {
        this.PasswordKey = PasswordKey;
    }

    public void setUsuarioCredencial(Usuario UsuarioCredencial) {
        this.usuarioCredencial = UsuarioCredencial;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Credencial)) {
            return false;
        }
        Credencial other = (Credencial) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelo.usuarios.Credencial[ id=" + id + " ]";
    }

}

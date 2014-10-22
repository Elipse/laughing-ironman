/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mx.com.croer.entities.busqueda;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author elialva
 */
@Embeddable
public class DiccionarioOrtogramaPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "idDiccionario")
    private String idDiccionario;
    @Basic(optional = false)
    @Column(name = "ortograma")
    private String ortograma;

    public DiccionarioOrtogramaPK() {
    }

    public DiccionarioOrtogramaPK(String idDiccionario, String ortograma) {
        this.idDiccionario = idDiccionario;
        this.ortograma = ortograma;
    }

    public String getIdDiccionario() {
        return idDiccionario;
    }

    public void setIdDiccionario(String idDiccionario) {
        this.idDiccionario = idDiccionario;
    }

    public String getOrtograma() {
        return ortograma;
    }

    public void setOrtograma(String ortograma) {
        this.ortograma = ortograma;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idDiccionario != null ? idDiccionario.hashCode() : 0);
        hash += (ortograma != null ? ortograma.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DiccionarioOrtogramaPK)) {
            return false;
        }
        DiccionarioOrtogramaPK other = (DiccionarioOrtogramaPK) object;
        if ((this.idDiccionario == null && other.idDiccionario != null) || (this.idDiccionario != null && !this.idDiccionario.equals(other.idDiccionario))) {
            return false;
        }
        if ((this.ortograma == null && other.ortograma != null) || (this.ortograma != null && !this.ortograma.equals(other.ortograma))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.com.croer.entities.busqueda.DiccionarioOrtogramaPK[ idDiccionario=" + idDiccionario + ", ortograma=" + ortograma + " ]";
    }
    
}

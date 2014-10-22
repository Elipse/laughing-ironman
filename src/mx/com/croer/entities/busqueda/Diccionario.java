/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mx.com.croer.entities.busqueda;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author elialva
 */
@Entity
@Table(name = "diccionario")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Diccionario.findAll", query = "SELECT d FROM Diccionario d"),
    @NamedQuery(name = "Diccionario.findByIdDiccionario", query = "SELECT d FROM Diccionario d WHERE d.idDiccionario = :idDiccionario"),
    @NamedQuery(name = "Diccionario.findByNombre", query = "SELECT d FROM Diccionario d WHERE d.nombre = :nombre")})
public class Diccionario implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "idDiccionario")
    private String idDiccionario;
    @Column(name = "nombre")
    private String nombre;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "diccionario")
    private Collection<DiccionarioOrtograma> diccionarioOrtogramaCollection;

    public Diccionario() {
    }

    public Diccionario(String idDiccionario) {
        this.idDiccionario = idDiccionario;
    }

    public String getIdDiccionario() {
        return idDiccionario;
    }

    public void setIdDiccionario(String idDiccionario) {
        this.idDiccionario = idDiccionario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @XmlTransient
    public Collection<DiccionarioOrtograma> getDiccionarioOrtogramaCollection() {
        return diccionarioOrtogramaCollection;
    }

    public void setDiccionarioOrtogramaCollection(Collection<DiccionarioOrtograma> diccionarioOrtogramaCollection) {
        this.diccionarioOrtogramaCollection = diccionarioOrtogramaCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idDiccionario != null ? idDiccionario.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Diccionario)) {
            return false;
        }
        Diccionario other = (Diccionario) object;
        if ((this.idDiccionario == null && other.idDiccionario != null) || (this.idDiccionario != null && !this.idDiccionario.equals(other.idDiccionario))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.com.croer.entities.busqueda.Diccionario[ idDiccionario=" + idDiccionario + " ]";
    }
    
}

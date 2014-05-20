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
@Table(name = "_ortograma")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Ortograma.findAll", query = "SELECT o FROM Ortograma o"),
    @NamedQuery(name = "Ortograma.findByOrtograma", query = "SELECT o FROM Ortograma o WHERE o.ortograma = :ortograma"),
    @NamedQuery(name = "Ortograma.findByNumegrama", query = "SELECT o FROM Ortograma o WHERE o.numegrama = :numegrama")})
public class Ortograma implements Serializable {
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ortograma1")
    private Collection<ItemOrtograma> itemOrtogramaCollection;
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ortograma")
    private String ortograma;
    @Basic(optional = false)
    @Column(name = "numegrama")
    private String numegrama;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ortograma1")
    private Collection<Alineacion> alineacionCollection;

    public Ortograma() {
    }

    public Ortograma(String ortograma) {
        this.ortograma = ortograma;
    }

    public Ortograma(String ortograma, String numegrama) {
        this.ortograma = ortograma;
        this.numegrama = numegrama;
    }

    public String getOrtograma() {
        return ortograma;
    }

    public void setOrtograma(String ortograma) {
        this.ortograma = ortograma;
    }

    public String getNumegrama() {
        return numegrama;
    }

    public void setNumegrama(String numegrama) {
        this.numegrama = numegrama;
    }

    @XmlTransient
    public Collection<Alineacion> getAlineacionCollection() {
        return alineacionCollection;
    }

    public void setAlineacionCollection(Collection<Alineacion> alineacionCollection) {
        this.alineacionCollection = alineacionCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ortograma != null ? ortograma.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Ortograma)) {
            return false;
        }
        Ortograma other = (Ortograma) object;
        if ((this.ortograma == null && other.ortograma != null) || (this.ortograma != null && !this.ortograma.equals(other.ortograma))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.com.croer.entities.busqueda.Ortograma[ ortograma=" + ortograma + " ]";
    }

    @XmlTransient
    public Collection<ItemOrtograma> getItemOrtogramaCollection() {
        return itemOrtogramaCollection;
    }

    public void setItemOrtogramaCollection(Collection<ItemOrtograma> itemOrtogramaCollection) {
        this.itemOrtogramaCollection = itemOrtogramaCollection;
    }
    
}

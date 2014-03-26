/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mx.com.croer.entities.busqueda;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author elialva
 */
@Entity
@Table(name = "_alineacion")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Alineacion.findAll", query = "SELECT a FROM Alineacion a"),
    @NamedQuery(name = "Alineacion.findBySimigrama", query = "SELECT a FROM Alineacion a WHERE a.alineacionPK.simigrama = :simigrama"),
    @NamedQuery(name = "Alineacion.findByOrtograma", query = "SELECT a FROM Alineacion a WHERE a.alineacionPK.ortograma = :ortograma"),
    @NamedQuery(name = "Alineacion.findByAlineacion", query = "SELECT a FROM Alineacion a WHERE a.alineacion = :alineacion")})
public class Alineacion implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected AlineacionPK alineacionPK;
    @Basic(optional = false)
    @Column(name = "alineacion")
    private String alineacion;
    @JoinColumn(name = "ortograma", referencedColumnName = "ortograma", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Ortograma ortograma1;
    @JoinColumn(name = "simigrama", referencedColumnName = "simigrama", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Simigrama simigrama1;

    public Alineacion() {
    }

    public Alineacion(AlineacionPK alineacionPK) {
        this.alineacionPK = alineacionPK;
    }

    public Alineacion(AlineacionPK alineacionPK, String alineacion) {
        this.alineacionPK = alineacionPK;
        this.alineacion = alineacion;
    }

    public Alineacion(String simigrama, String ortograma) {
        this.alineacionPK = new AlineacionPK(simigrama, ortograma);
    }

    public AlineacionPK getAlineacionPK() {
        return alineacionPK;
    }

    public void setAlineacionPK(AlineacionPK alineacionPK) {
        this.alineacionPK = alineacionPK;
    }

    public String getAlineacion() {
        return alineacion;
    }

    public void setAlineacion(String alineacion) {
        this.alineacion = alineacion;
    }

    public Ortograma getOrtograma1() {
        return ortograma1;
    }

    public void setOrtograma1(Ortograma ortograma1) {
        this.ortograma1 = ortograma1;
    }

    public Simigrama getSimigrama1() {
        return simigrama1;
    }

    public void setSimigrama1(Simigrama simigrama1) {
        this.simigrama1 = simigrama1;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (alineacionPK != null ? alineacionPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Alineacion)) {
            return false;
        }
        Alineacion other = (Alineacion) object;
        if ((this.alineacionPK == null && other.alineacionPK != null) || (this.alineacionPK != null && !this.alineacionPK.equals(other.alineacionPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.com.croer.entities.busqueda.Alineacion[ alineacionPK=" + alineacionPK + " ]";
    }
    
}

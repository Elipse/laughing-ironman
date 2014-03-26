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
public class AlineacionPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "simigrama")
    private String simigrama;
    @Basic(optional = false)
    @Column(name = "ortograma")
    private String ortograma;

    public AlineacionPK() {
    }

    public AlineacionPK(String simigrama, String ortograma) {
        this.simigrama = simigrama;
        this.ortograma = ortograma;
    }

    public String getSimigrama() {
        return simigrama;
    }

    public void setSimigrama(String simigrama) {
        this.simigrama = simigrama;
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
        hash += (simigrama != null ? simigrama.hashCode() : 0);
        hash += (ortograma != null ? ortograma.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AlineacionPK)) {
            return false;
        }
        AlineacionPK other = (AlineacionPK) object;
        if ((this.simigrama == null && other.simigrama != null) || (this.simigrama != null && !this.simigrama.equals(other.simigrama))) {
            return false;
        }
        if ((this.ortograma == null && other.ortograma != null) || (this.ortograma != null && !this.ortograma.equals(other.ortograma))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.com.croer.entities.busqueda.AlineacionPK[ simigrama=" + simigrama + ", ortograma=" + ortograma + " ]";
    }
    
}

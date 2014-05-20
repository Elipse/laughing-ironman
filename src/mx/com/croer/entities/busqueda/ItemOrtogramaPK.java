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
public class ItemOrtogramaPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "idItem")
    private String idItem;
    @Basic(optional = false)
    @Column(name = "type")
    private String type;
    @Basic(optional = false)
    @Column(name = "ortograma")
    private String ortograma;

    public ItemOrtogramaPK() {
    }

    public ItemOrtogramaPK(String idItem, String type, String ortograma) {
        this.idItem = idItem;
        this.type = type;
        this.ortograma = ortograma;
    }

    public String getIdItem() {
        return idItem;
    }

    public void setIdItem(String idItem) {
        this.idItem = idItem;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
        hash += (idItem != null ? idItem.hashCode() : 0);
        hash += (type != null ? type.hashCode() : 0);
        hash += (ortograma != null ? ortograma.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ItemOrtogramaPK)) {
            return false;
        }
        ItemOrtogramaPK other = (ItemOrtogramaPK) object;
        if ((this.idItem == null && other.idItem != null) || (this.idItem != null && !this.idItem.equals(other.idItem))) {
            return false;
        }
        if ((this.type == null && other.type != null) || (this.type != null && !this.type.equals(other.type))) {
            return false;
        }
        if ((this.ortograma == null && other.ortograma != null) || (this.ortograma != null && !this.ortograma.equals(other.ortograma))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.com.croer.entities.busqueda.ItemOrtogramaPK[ idItem=" + idItem + ", type=" + type + ", ortograma=" + ortograma + " ]";
    }
    
}

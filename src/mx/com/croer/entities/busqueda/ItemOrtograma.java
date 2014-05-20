/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mx.com.croer.entities.busqueda;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
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
@Table(name = "item_ortograma")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ItemOrtograma.findAll", query = "SELECT i FROM ItemOrtograma i"),
    @NamedQuery(name = "ItemOrtograma.findByIdItem", query = "SELECT i FROM ItemOrtograma i WHERE i.itemOrtogramaPK.idItem = :idItem"),
    @NamedQuery(name = "ItemOrtograma.findByType", query = "SELECT i FROM ItemOrtograma i WHERE i.itemOrtogramaPK.type = :type"),
    @NamedQuery(name = "ItemOrtograma.findByOrtograma", query = "SELECT i FROM ItemOrtograma i WHERE i.itemOrtogramaPK.ortograma = :ortograma"),
    @NamedQuery(name = "ItemOrtograma.findByFrecuencia", query = "SELECT i FROM ItemOrtograma i WHERE i.frecuencia = :frecuencia")})
public class ItemOrtograma implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ItemOrtogramaPK itemOrtogramaPK;
    @Column(name = "frecuencia")
    private Integer frecuencia;
    @JoinColumn(name = "ortograma", referencedColumnName = "ortograma", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Ortograma ortograma1;
    @JoinColumns({
        @JoinColumn(name = "idItem", referencedColumnName = "idItem", insertable = false, updatable = false),
        @JoinColumn(name = "type", referencedColumnName = "type", insertable = false, updatable = false)})
    @ManyToOne(optional = false)
    private Itembusq itembusq;

    public ItemOrtograma() {
    }

    public ItemOrtograma(ItemOrtogramaPK itemOrtogramaPK) {
        this.itemOrtogramaPK = itemOrtogramaPK;
    }

    public ItemOrtograma(String idItem, String type, String ortograma) {
        this.itemOrtogramaPK = new ItemOrtogramaPK(idItem, type, ortograma);
    }

    public ItemOrtogramaPK getItemOrtogramaPK() {
        return itemOrtogramaPK;
    }

    public void setItemOrtogramaPK(ItemOrtogramaPK itemOrtogramaPK) {
        this.itemOrtogramaPK = itemOrtogramaPK;
    }

    public Integer getFrecuencia() {
        return frecuencia;
    }

    public void setFrecuencia(Integer frecuencia) {
        this.frecuencia = frecuencia;
    }

    public Ortograma getOrtograma1() {
        return ortograma1;
    }

    public void setOrtograma1(Ortograma ortograma1) {
        this.ortograma1 = ortograma1;
    }

    public Itembusq getItembusq() {
        return itembusq;
    }

    public void setItembusq(Itembusq itembusq) {
        this.itembusq = itembusq;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (itemOrtogramaPK != null ? itemOrtogramaPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ItemOrtograma)) {
            return false;
        }
        ItemOrtograma other = (ItemOrtograma) object;
        if ((this.itemOrtogramaPK == null && other.itemOrtogramaPK != null) || (this.itemOrtogramaPK != null && !this.itemOrtogramaPK.equals(other.itemOrtogramaPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.com.croer.entities.busqueda.ItemOrtograma[ itemOrtogramaPK=" + itemOrtogramaPK + " ]";
    }
    
}

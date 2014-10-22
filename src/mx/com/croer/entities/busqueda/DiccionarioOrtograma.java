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
@Table(name = "diccionario_ortograma")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DiccionarioOrtograma.findAll", query = "SELECT d FROM DiccionarioOrtograma d"),
    @NamedQuery(name = "DiccionarioOrtograma.findByIdDiccionario", query = "SELECT d FROM DiccionarioOrtograma d WHERE d.diccionarioOrtogramaPK.idDiccionario = :idDiccionario"),
    @NamedQuery(name = "DiccionarioOrtograma.findByOrtograma", query = "SELECT d FROM DiccionarioOrtograma d WHERE d.diccionarioOrtogramaPK.ortograma = :ortograma"),
    @NamedQuery(name = "DiccionarioOrtograma.findByFiller", query = "SELECT d FROM DiccionarioOrtograma d WHERE d.filler = :filler")})
public class DiccionarioOrtograma implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected DiccionarioOrtogramaPK diccionarioOrtogramaPK;
    @Column(name = "filler")
    private String filler;
    @JoinColumn(name = "idDiccionario", referencedColumnName = "idDiccionario", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Diccionario diccionario;
    @JoinColumn(name = "ortograma", referencedColumnName = "ortograma", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Ortograma ortograma1;

    public DiccionarioOrtograma() {
    }

    public DiccionarioOrtograma(DiccionarioOrtogramaPK diccionarioOrtogramaPK) {
        this.diccionarioOrtogramaPK = diccionarioOrtogramaPK;
    }

    public DiccionarioOrtograma(String idDiccionario, String ortograma) {
        this.diccionarioOrtogramaPK = new DiccionarioOrtogramaPK(idDiccionario, ortograma);
    }

    public DiccionarioOrtogramaPK getDiccionarioOrtogramaPK() {
        return diccionarioOrtogramaPK;
    }

    public void setDiccionarioOrtogramaPK(DiccionarioOrtogramaPK diccionarioOrtogramaPK) {
        this.diccionarioOrtogramaPK = diccionarioOrtogramaPK;
    }

    public String getFiller() {
        return filler;
    }

    public void setFiller(String filler) {
        this.filler = filler;
    }

    public Diccionario getDiccionario() {
        return diccionario;
    }

    public void setDiccionario(Diccionario diccionario) {
        this.diccionario = diccionario;
    }

    public Ortograma getOrtograma1() {
        return ortograma1;
    }

    public void setOrtograma1(Ortograma ortograma1) {
        this.ortograma1 = ortograma1;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (diccionarioOrtogramaPK != null ? diccionarioOrtogramaPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DiccionarioOrtograma)) {
            return false;
        }
        DiccionarioOrtograma other = (DiccionarioOrtograma) object;
        if ((this.diccionarioOrtogramaPK == null && other.diccionarioOrtogramaPK != null) || (this.diccionarioOrtogramaPK != null && !this.diccionarioOrtogramaPK.equals(other.diccionarioOrtogramaPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.com.croer.entities.busqueda.DiccionarioOrtograma[ diccionarioOrtogramaPK=" + diccionarioOrtogramaPK + " ]";
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mx.com.croer.entities.busqueda;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author elialva
 */
@Entity
@Table(name = "productobusq")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Productobusq.findAll", query = "SELECT p FROM Productobusq p"),
    @NamedQuery(name = "Productobusq.findByIdBean", query = "SELECT p FROM Productobusq p WHERE p.idBean = :idBean"),
    @NamedQuery(name = "Productobusq.findByPrioridad", query = "SELECT p FROM Productobusq p WHERE p.prioridad = :prioridad")})
public class Productobusq implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "idBean")
    private Integer idBean;
    @Lob
    @Column(name = "contexto")
    private String contexto;
    @Column(name = "prioridad")
    private Integer prioridad;

    public Productobusq() {
    }

    public Productobusq(Integer idBean) {
        this.idBean = idBean;
    }

    public Integer getIdBean() {
        return idBean;
    }

    public void setIdBean(Integer idBean) {
        this.idBean = idBean;
    }

    public String getContexto() {
        return contexto;
    }

    public void setContexto(String contexto) {
        this.contexto = contexto;
    }

    public Integer getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(Integer prioridad) {
        this.prioridad = prioridad;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idBean != null ? idBean.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Productobusq)) {
            return false;
        }
        Productobusq other = (Productobusq) object;
        if ((this.idBean == null && other.idBean != null) || (this.idBean != null && !this.idBean.equals(other.idBean))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.com.croer.busqueda.entities.Productobusq[ idBean=" + idBean + " ]";
    }
    
}

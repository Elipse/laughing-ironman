/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mx.com.croer.catalogodigital.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
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
@Table(name = "producto")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Producto.findAll", query = "SELECT p FROM Producto p"),
    @NamedQuery(name = "Producto.findByIdProducto", query = "SELECT p FROM Producto p WHERE p.idProducto = :idProducto"),
    @NamedQuery(name = "Producto.findByDescripcion", query = "SELECT p FROM Producto p WHERE p.descripcion = :descripcion"),
    @NamedQuery(name = "Producto.findByCantidad", query = "SELECT p FROM Producto p WHERE p.cantidad = :cantidad"),
    @NamedQuery(name = "Producto.findByPrecio", query = "SELECT p FROM Producto p WHERE p.precio = :precio")})
public class Producto implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "idProducto")
    private Integer idProducto;
    @Column(name = "descripcion")
    private Integer descripcion;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "cantidad")
    private BigDecimal cantidad;
    @Column(name = "precio")
    private BigDecimal precio;
    @JoinColumn(name = "Unidad_idUnidad", referencedColumnName = "idUnidad")
    @ManyToOne(optional = false)
    private Unidad unidadidUnidad;
    @JoinColumn(name = "Marca_idMarca", referencedColumnName = "idMarca")
    @ManyToOne(optional = false)
    private Marca marcaidMarca;

    public Producto() {
    }

    public Producto(Integer idProducto) {
        this.idProducto = idProducto;
    }

    public Integer getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(Integer idProducto) {
        this.idProducto = idProducto;
    }

    public Integer getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(Integer descripcion) {
        this.descripcion = descripcion;
    }

    public BigDecimal getCantidad() {
        return cantidad;
    }

    public void setCantidad(BigDecimal cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public Unidad getUnidadidUnidad() {
        return unidadidUnidad;
    }

    public void setUnidadidUnidad(Unidad unidadidUnidad) {
        this.unidadidUnidad = unidadidUnidad;
    }

    public Marca getMarcaidMarca() {
        return marcaidMarca;
    }

    public void setMarcaidMarca(Marca marcaidMarca) {
        this.marcaidMarca = marcaidMarca;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idProducto != null ? idProducto.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Producto)) {
            return false;
        }
        Producto other = (Producto) object;
        if ((this.idProducto == null && other.idProducto != null) || (this.idProducto != null && !this.idProducto.equals(other.idProducto))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "mx.com.croer.catalogodigital.entities.Producto[ idProducto=" + idProducto + " ]";
    }
    
}

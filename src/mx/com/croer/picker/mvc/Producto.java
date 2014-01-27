/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.com.croer.picker.mvc;

import javax.swing.Icon;

/**
 *
 * @author elialva
 */
public class Producto {

    private String descripcion;
    private Icon imagen;
    private String marca;

    public Producto(String descripcion, Icon image, String marca) {
        this.descripcion = descripcion;
        this.imagen = image;
        this.marca = marca;
    }

    /**
     * @return the imagen
     */
    public Icon getImage() {
        return imagen;
    }

    /**
     * @param image the imagen to set
     */
    public void setImage(Icon image) {
        this.imagen = image;
    }

    /**
     * @return the descripcion
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * @param descripcion the descripcion to set
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * @return the marca
     */
    public String getMarca() {
        return marca;
    }

    /**
     * @param marca the marca to set
     */
    public void setMarca(String marca) {
        this.marca = marca;
    }

}

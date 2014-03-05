/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.com.croer.entities.proxy;

import mx.com.croer.entities.proxy.Item;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;
import javax.swing.Icon;
import mx.com.croer.entities.catalogodigital.Marca;
import mx.com.croer.entities.catalogodigital.Producto;

/**
 *
 * @author elialva
 */
public class MarcaW implements Item {

    public static final String PROP_SOURCE = "PROP_SOURCE";
    public static final String PROP_ALIGNMENT = "PROP_ALIGNMENT";
    public static final String PROP_IMAGE = "image";
    public static final String PROP_SELECTION = "selection";
    private Marca source;
    private List alignment;
    private Icon image;
    private Integer selection;
    private final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
    private String context;

    public MarcaW(Marca marca) {
//        super(producto.getIdproducto());
        System.out.println("PRODWW " + marca.getDescripcion());
        this.source = marca;
    }

    /**
     * @return the source
     */
    @Override
    public Object getSource() {
        return source;
    }

    /**
     * @param source the source to set
     */
    @Override
    public void setSource(Object source) {
        java.lang.Object oldSource = this.source;
        this.source = (Marca) source;
        propertyChangeSupport.firePropertyChange(PROP_SOURCE, oldSource, source);
    }

    /**
     * @return the alignment
     */
    @Override
    public List getAlignment() {
        return alignment;
    }

    /**
     * @param alignment the alignment to set
     */
    @Override
    public void setAlignment(List alignment) {
        java.util.List oldAlignment = this.alignment;
        this.alignment = alignment;
        propertyChangeSupport.firePropertyChange(PROP_ALIGNMENT, oldAlignment, alignment);
    }

    /**
     * @return the image
     */
    @Override
    public Icon getImage() {
        return image;
    }

    /**
     * @param image the image to set
     */
    @Override
    public void setImage(Icon image) {
        javax.swing.Icon oldImage = this.image;
        this.image = image;
        propertyChangeSupport.firePropertyChange(PROP_IMAGE, oldImage, image);
    }

    /**
     * @return the selection
     */
    @Override
    public Integer getSelection() {
        return selection;
    }

    /**
     * @param selection the selection to set
     */
    @Override
    public void setSelection(Integer selection) {
        java.lang.Integer oldSelection = this.selection;
        this.selection = selection;
        propertyChangeSupport.firePropertyChange(PROP_SELECTION, oldSelection, selection);
    }

//    @Override
    public Integer getIdMarca() {
        return source.getIdMarca();
    }

//    @Override
    public void setDescripcion(String descripcion) {
        String oldDescripcion = source.getDescripcion();
        source.setDescripcion(descripcion);
        propertyChangeSupport.firePropertyChange("descripcion", oldDescripcion, descripcion);
    }

//    @Override
    public String getDescripcion() {
        return source.getDescripcion();
    }

    //se requiere para beansbinding
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    //se requiere para beansbinding
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }

    @Override
    public void setContext(String context) {
        String oldContext = this.context;
        this.context = context;
        propertyChangeSupport.firePropertyChange("context", oldContext, context);
    }

    @Override
    public String getContext() {
        return this.context;
    }
}

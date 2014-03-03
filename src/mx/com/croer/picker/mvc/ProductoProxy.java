/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.com.croer.picker.mvc;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;
import javax.swing.Icon;

/**
 *
 * @author elialva
 */
public class ProductoProxy implements Item {

    public static final String PROP_SOURCE = "PROP_SOURCE";
    public static final String PROP_ALIGNMENT = "PROP_ALIGNMENT";
    public static final String PROP_IMAGE = "image";
    public static final String PROP_SELECTION = "selection";
    private ProductoSearch source;
    private List alignment;
    private Icon image;
    private Integer selection;
    private final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

    public ProductoProxy(ProductoSearch producto) {
//        super(producto.getIdproducto());
        this.source = producto;
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
        this.source = (ProductoSearch) source;
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
    public String getIdproducto() {
        return source.getIdproducto();
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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getContext() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mx.com.croer.picker.mvc;

import java.util.List;
import javax.swing.Icon;
import mx.com.croer.catalogodigital.entities.Marca;
import mx.com.croer.catalogodigital.entities.Producto;

/**
 *
 * @author elialva
 */
public class MarcaW implements Item {
    private Marca marca;
    private List alignment;
    private String context;
    
    public MarcaW(Marca marca) {
        this.marca = marca;
    }

    @Override
    public void setSource(Object source) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object getSource() {
        return this.marca;
    }

    @Override
    public void setAlignment(List alignment) {
        this.alignment = alignment;
    }

    @Override
    public List getAlignment() {
        return this.alignment;
    }

    @Override
    public void setImage(Icon image) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Icon getImage() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setSelection(Integer selection) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Integer getSelection() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setContext(String context) {
        this.context = context;
    }

    @Override
    public String getContext() {
        return this.context;
    }
}

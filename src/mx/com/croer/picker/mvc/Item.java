/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.com.croer.picker.mvc;

import java.util.List;
import javax.swing.Icon;

/**
 *
 * @author elialva
 */
public interface Item {

    public void setSource(Object source);

    public Object getSource();
    
    public void setContext (String context);

    public String getContext();

    public void setAlignment(List alignment);

    public List getAlignment();

    public void setImage(Icon image);

    public Icon getImage();

    public void setSelection(Integer selection);

    public Integer getSelection();

}

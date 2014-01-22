/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.com.croer.picker.model;

import java.awt.Color;
import java.util.List;
import javax.swing.Icon;

/**
 *
 * @author elialva
 */
public abstract class DataPicker {

    public synchronized List readPage(Object pageHeader) {
        return null;
    }

    public synchronized List readPage(String pageHeader) {
        return null;
    }

    public Icon createIcon(Object bean) {
        return null;
    }

    public Integer countRows(Object input) {
        return null;
    }    

    public int getPageSize() {
        return 0;
    }

    

    public boolean hasIcon() {
        return true;
    }

    

    public Color nextMode() {
        return null;
    }

    

    public List<BeanColumn> readColumnList() {
        return null;
    }

    public int getHeightRow() {
        return 0;
    }

}

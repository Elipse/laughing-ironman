/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.com.croer.picker.mvc;

import java.awt.Color;
import java.util.List;
import javax.swing.Icon;

/**
 * Esta es una prueba de upload
 *
 * @author elialva
 */
public abstract class DataPicker {

    public synchronized List readPage(Object pageHeader) {
        return extractPage(pageHeader);
    }

    public synchronized List readPage(String pageHeader) {
        return extractPage(pageHeader);
    }

    public abstract Icon createIcon(Object bean);

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

    public abstract List extractPage(Object pageHeader);

    public abstract List extractPage(String pageHeader);
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.com.croer.picker.model; 

/*
No te pases, esta chido
*/

import javax.swing.table.TableCellRenderer;

/**
 *
 * @author elialva
 */
public class BeanColumn {

    private String property;
    private String colName;
    private Class clase;
    private TableCellRenderer renderer;
    private boolean visible;
    private int width;
    private boolean key;

    public BeanColumn(String property, String colName, Class clase, TableCellRenderer renderer,boolean visible,int width, boolean key) {
        this.property = property;
        this.colName = colName;
        this.clase = clase;
        this.renderer = renderer;
        this.visible = visible;
        this.width = width;
        this.key = key;
    }

    /**
     * @return the property
     */
    public String getProperty() {
        return property;
    }

    /**
     * @param property the property to set
     */
    public void setProperty(String property) {
        this.property = property;
    }

    /**
     * @return the colName
     */
    public String getColName() {
        return colName;
    }

    /**
     * @param colName the colName to set
     */
    public void setColName(String colName) {
        this.colName = colName;
    }

    /**
     * @return the clase
     */
    public Class getClase() {
        return clase;
    }

    /**
     * @param clase the clase to set
     */
    public void setClase(Class clase) {
        this.clase = clase;
    }

    /**
     * @return the renderer
     */
    public TableCellRenderer getRenderer() {
        return renderer;
    }

    /**
     * @param renderer the renderer to set
     */
    public void setRenderer(TableCellRenderer renderer) {
        this.renderer = renderer;
    }

    /**
     * @return the visible
     */
    public boolean isVisible() {
        return visible;
    }

    /**
     * @param visible the visible to set
     */
    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    /**
     * @return the width
     */
    public int getWidth() {
        return width;
    }

    /**
     * @param width the width to set
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * @return the key
     */
    public boolean isKey() {
        return key;
    }

    /**
     * @param key the key to set
     */
    public void setKey(boolean key) {
        this.key = key;
    }
}

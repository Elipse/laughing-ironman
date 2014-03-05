/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.com.croer.picker.mvc;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.util.List;
import javax.swing.Icon;

/**
 * Esta es una prueba de upload
 *
 * @author elialva
 */
public abstract class DataPicker {

    public synchronized List<Item> readPage(Object pageHeader) {
        return createPage(pageHeader);
    }

    public Icon createIcon(Object bean) {
        return new MissingItem(Color.yellow);
    }    

    public abstract int createPageSize();

    public abstract List<Item> createPage(Object pageHeader);

}

class MissingItem implements Icon {

    Color color;

    public MissingItem(Color color) {
        this.color = color;
    }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        Color oldColor = g.getColor();
        g.setColor(color);
        g.fill3DRect(x, y, getIconWidth(), getIconHeight(), true);
        g.setColor(oldColor);
    }

    @Override
    public int getIconWidth() {
        return 12;
    }

    @Override
    public int getIconHeight() {
        return 12;
    }

}

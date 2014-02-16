/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.com.croer.picker.mvc;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 *
 * @author elialva
 */
public class DataPickerImp extends DataPicker {

    @Override
    public int createPageSize() {
        return 3;
    }

    @Override
    public List createPage(Object pageHeader) {
        System.out.println("pageHEadr " + pageHeader.getClass());
        List list = new ArrayList();

        ProductoSearch p = new ProductoSearch("leche");
        p.setDescripcion("lala");
        ProductoProxy ps = new ProductoProxy(p);
        ps.setSelection(7);
        list.add(ps);
        p = new ProductoSearch("mango");
        p.setDescripcion("frut&veg");
        ps = new ProductoProxy(p);
        ps.setSelection(3);
        list.add(ps);
        p = new ProductoSearch("platanos");
        p.setDescripcion("frut&veg");
        ps = new ProductoProxy(p);
        ps.setSelection(null);
        list.add(ps);
        try {
            Thread.sleep(2000);

        } catch (InterruptedException ex) {
            Logger.getLogger(DataPickerImp.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    @Override
    public Icon createIcon(Object bean) {

        Icon image = new ImageIcon("C:\\Users\\IBM_ADMIN\\Documents\\@Projects_Eli\\201309 Finder&Getter\\_NBPOtros\\JavaProject1\\src\\mx\\com\\croer\\picker\\mvc\\Banana-icon.png");

        Item item = (Item) bean;
        try {
            Thread.sleep(500);

        } catch (InterruptedException ex) {
            Logger.getLogger(DataPickerImp.class.getName()).log(Level.SEVERE, null, ex);
        }
        return image;
    }
}

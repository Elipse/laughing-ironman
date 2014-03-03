/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.com.croer.picker.mvc;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import mx.com.croer.busqueda.entities.Simigrama;
import mx.com.croer.catalogodigital.entities.Marca;

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
    public List createPage(Object pageHeader, int rows) {
//        System.out.println("pageHEadr " + pageHeader.getClass());
        List list = new ArrayList();
//        EntityManagerFactory createEntityManagerFactory2 = Persistence.createEntityManagerFactory("JavaProject1PU2");
//        EntityManager createEntityManager2 = createEntityManagerFactory2.createEntityManager();
//        TypedQuery<Marca> createNamedQuery2 = createEntityManager2.createNamedQuery("Marca.findAll", Marca.class);
//        List<Marca> resultList2 = createNamedQuery2.getResultList();
//        for (Marca marca : resultList2) {
//            System.out.println("Marca " + marca.getDescripcion());
//        }
//
//        EntityManagerFactory createEntityManagerFactory3 = Persistence.createEntityManagerFactory("JavaProject1PU3");
//        EntityManager createEntityManager3 = createEntityManagerFactory3.createEntityManager();
//        TypedQuery<Simigrama> createNamedQuery3 = createEntityManager3.createNamedQuery("Simigrama.findAll", Simigrama.class);
//        List<Simigrama> resultList3 = createNamedQuery3.getResultList();
//        for (Simigrama simigrama : resultList3) {
//            System.out.println("Simigrama " + simigrama.getSimigrama());
//        }

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

        ItemProxy item = (ItemProxy) bean;
        try {
            Thread.sleep(500);

        } catch (InterruptedException ex) {
            Logger.getLogger(DataPickerImp.class.getName()).log(Level.SEVERE, null, ex);
        }
        return image;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.com.croer.picker.mvc;

import mx.com.croer.entities.proxy.Item;
import java.util.List;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import mx.com.croer.picker.access.SearchFetcher;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * @author elialva
 */
public class DataPickerImp extends DataPicker {

    private Class type = Object.class;
    private String originalQuery;
    private int pageSize = 3;

    @Override
    public int createPageSize() {
        return pageSize;
    }

    @Override
    public List<Item> createPage(Object pageHeader, int pageNumber) {
//        System.out.println("pageHEadr " + pageHeader.getClass());
//        List list = new ArrayList();
//        EntityManagerFactory createEntityManagerFactory2 = Persistence.createEntityManagerFactory("JavaProject1PU2");
//        EntityManager createEntityManager2 = createEntityManagerFactory2.createEntityManager();
//        TypedQuery<Marca> createNamedQuery2 = createEntityManager2.createNamedQuery("Marca.findAll", Marca.class);
//        List<Marca> resultList2 = createNamedQuery2.getResultList();
//        for (Marca marca : resultList2) {
//            System.out.println("MarcaPicasa " + marca.getDescripcion());
//        }
//
//        EntityManagerFactory createEntityManagerFactory3 = Persistence.createEntityManagerFactory("JavaProject1PU3");
//        EntityManager createEntityManager3 = createEntityManagerFactory3.createEntityManager();
//        TypedQuery<Simigrama> createNamedQuery3 = createEntityManager3.createNamedQuery("Simigrama.findAll", Simigrama.class);
//        List<Simigrama> resultList3 = createNamedQuery3.getResultList();
//        for (Simigrama simigrama : resultList3) {
//            System.out.println("Simigrama " + simigrama.getSimigrama());
//        }

//        try {
//            Thread.sleep(100);
//        } catch (InterruptedException ex) {
//            Logger.getLogger(DataPickerImp.class.getName()).log(Level.SEVERE, null, ex);
//        }
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring.xml");
        SearchFetcher searchFetcher = (SearchFetcher) context.getBean("searchFetcher");
        searchFetcher.setType(type);
        if (pageHeader.getClass() == String.class) {
            originalQuery = pageHeader.toString();
        }
//        
        List<Item> page = searchFetcher.getPage(originalQuery);

        int indexOf = page.indexOf(pageHeader);

        System.out.println("Fetch with " + pageHeader + " * " + pageNumber + "###" + indexOf);

        if (indexOf >= 0) {
            int i = indexOf + pageSize + 1;
            if (i > page.size()) {
                i = page.size();
            }
            return page.subList(indexOf, i);
        } else {
            return page;
        }
    }

    @Override
    public Icon createIcon(Object bean) {

        Icon image = new ImageIcon("C:\\Users\\IBM_ADMIN\\Documents\\@Projects_Eli\\201309 Finder&Getter\\_NBPOtros\\JavaProject1\\src\\mx\\com\\croer\\picker\\mvc\\Banana-icon.png");

        Item item = (Item) bean;
//        try {
//            Thread.sleep(500);
//        } catch (InterruptedException ex) {
//            Logger.getLogger(DataPickerImp.class.getName()).log(Level.SEVERE, null, ex);
//        }
        return image;
    }

    /**
     * @return the type
     */
    public Class getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(Class type) {
        this.type = type;
    }
}

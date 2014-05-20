/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.com.croer.entities.busqueda.controllers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import mx.com.croer.entities.busqueda.ItemOrtograma;
import mx.com.croer.entities.busqueda.ItemOrtogramaPK;
import mx.com.croer.entities.busqueda.Itembusq;
import mx.com.croer.entities.busqueda.Ortograma;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * @author elialva
 */
public class TestJpa {

    public static void main(String[] args) throws Exception {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring.xml");
        EntityManagerFactory bean = context.getBean("emfSearch", EntityManagerFactory.class);

        ItembusqJpaController itembusqJpaController = new ItembusqJpaController(bean);
        ItemOrtogramaJpaController itemOrtogramaJpaController = new ItemOrtogramaJpaController(bean);

//        ItemOrtograma itemOrtograma1 = null;
//        itemOrtograma1 = new ItemOrtograma("hola", "Producto", "dulce");
//        itemOrtograma1.setOrtograma1(new Ortograma("dulce"));
//        itemOrtograma1.setFrecuencia(1);
//        System.out.println("key " + itemOrtograma1.getItemOrtogramaPK());
//        itemOrtogramaJpaController.create(itemOrtograma1);
        Itembusq itembusq = new Itembusq("hola", "Producto");
        itembusq.setContexto("Un producto dulce y de colores");
        itembusq.setPrioridad(3);

        Ortograma ortograma = new Ortograma("dulce");
        ortograma.setNumegrama("99999");

        ItemOrtograma itemOrtograma = null;
        List<ItemOrtograma> list = new ArrayList<ItemOrtograma>();
        itemOrtograma = new ItemOrtograma("hola", "Producto", "dulce");
        itemOrtograma.setItembusq(itembusq);
        itemOrtograma.setOrtograma1(ortograma);
        itemOrtograma.setFrecuencia(1);
        list.add(itemOrtograma);

//        itemOrtograma = new ItemOrtograma("hola", "Producto", "colores");
//        itemOrtograma.setItembusq(itembusq);
//        itemOrtograma.setOrtograma1(ortograma);
//        itemOrtograma.setFrecuencia(1);
//        list.add(itemOrtograma);
        itembusq.setItemOrtogramaCollection(list);

        EntityManager createEntityManager = bean.createEntityManager();

//        createEntityManager.getTransaction().begin();
//        createEntityManager.persist(ortograma);  //El papa sin hijos
//        createEntityManager.persist(itembusq);   //El papa con hijos    
////        createEntityManager.persist(list.get(0));
//        createEntityManager.getTransaction().commit();
        createEntityManager.getTransaction().begin();

        Ortograma find = createEntityManager.find(Ortograma.class, "dulce");
        int size = find.getItemOrtogramaCollection().size();
        System.out.println("La talla es A " + size);

        itembusq = createEntityManager.merge(itembusq);
        createEntityManager.remove(itembusq);   //El papa con hijos
        createEntityManager.flush();

        createEntityManager.refresh(find);
        size = find.getItemOrtogramaCollection().size();
        System.out.println("La talla es D " + size);

        createEntityManager.getTransaction().rollback();
        createEntityManager.getTransaction().commit();

//        createEntityManager.getTransaction().begin();
//        Itembusq merge = createEntityManager.merge(itembusq);
//        merge.setItemOrtogramaCollection(new ArrayList<ItemOrtograma>());
//        createEntityManager.merge(merge);
//        ItemOrtograma find = createEntityManager.find(ItemOrtograma.class, new ItemOrtogramaPK("hola", "Producto", "dulce"));
//        createEntityManager.remove(find);
//        System.out.println("Merge " + merge.getContexto());
//        createEntityManager.getTransaction().commit();
//        createEntityManager.refresh(ortograma);  //Se hace de sus hijos
//        
//        Collection<ItemOrtograma> itemOrtogramaCollection = ortograma.getItemOrtogramaCollection();
//        for (ItemOrtograma itemOrtograma1 : itemOrtogramaCollection) {
//            System.out.println("itemOrdo " + itemOrtograma1);
//        }
    }

}

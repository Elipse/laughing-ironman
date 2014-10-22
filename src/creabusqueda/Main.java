/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package creabusqueda;

import mx.com.croer.entities.busqueda.ItembusqPK;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * @author elialva
 */
public class Main {

    public static void main(String[] args) {
//        Producto p = new Producto();
//        p.setIdProducto(323);
//        p.setCantidad(new BigDecimal(13));
//        p.setDescripcion("Yakult 40");
//        p.setPrecio(new BigDecimal(10.5));
//        ArrayList<String> key = new ArrayList<>();
//        key.add("idProducto");
//        ArrayList<String> context = new ArrayList<>();
//        context.add("descripcion");
//        context.add("precio");
//        context.add("cantidad");
//        Context.saveContext(p, key,context);

//        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring.xml");
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("newSpring.xml");
        ItembusqRepository bean = (ItembusqRepository) context.getBean("itembusqRepository");
        System.out.println("bean " + bean.getClass());
        System.out.println("LaClas " + bean.findOne(new ItembusqPK("9", "Producto")).getContexto());
        
//        ServiCua bean = context.getBean("serviCua", ServiCua.class);
//        System.out.println("Bean " + bean.getItembusqRepository());
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.com.croer.breakdown;

import static com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type.Real;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * @author elialva
 */
public class Escaneo {

    public static void main(String[] args) {
//        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("newFile.xml");
//        Real bean = context.getBean(Real.class);
//        bean.ddd();
//        Real bean1 = (Real) context.getBean("real");
//        bean1.ddd();

        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring.xml");
        ItemCntrllr bean2 = (ItemCntrllr) context.getBean("eamControler");
        bean2.addItem();
        System.out.println("Bean2 " + bean2.hashCode());
        System.out.println("context " + context);
        SubRutina sb = new SubRutina();
        sb.printContext();
//        context = new ClassPathXmlApplicationContext("spring.xml");
//        ItemCntrllr bean3 = (ItemCntrllr) context.getBean("eamControler");
//        System.out.println("Bean3 " + bean3.hashCode());
        
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.com.croer.picker.access;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author elialva
 */
public class NewClass {

    public static void itera(List l) {
        System.out.println("A punto de iterar");
        for (Object object : l) {
            System.out.println("Object " + object);
        }
    }
    public static void main(String[] args) {
        List l = new ArrayList();
        l.add("Azul");
        l.add("Rojo");
        l.add("Verde");
        l.add("Magenta");
//        List j = l;
        List j = new ArrayList(l);
        System.out.println("La L");
        itera(l);
        System.out.println("La J");
        itera(j);
        l.clear();
        System.out.println("La L");
        itera(l);
        System.out.println("La J");
        itera(j);
        
        
    }

}

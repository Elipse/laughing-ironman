/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mx.com.croer.picker.mvc;

import javax.swing.text.JTextComponent;

/**
 *
 * @author elialva
 */
public class Pavon {
    
    public static void bind(JTextComponent j) {
        PavonImp pavon = new PavonImp();
        pavon.bindImp(j);
    }    

    
}

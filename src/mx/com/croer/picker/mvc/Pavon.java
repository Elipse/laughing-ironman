/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.com.croer.picker.mvc;

import javax.swing.text.JTextComponent;
import mx.com.croer.entities.catalogodigital.Producto;

/**
 *
 * @author elialva
 */
public class Pavon {

    public static PickerController bind(JTextComponent component, Class type, ItemListener listener) {
        PickerController pickerController = new PickerControllerImpl(component, Producto.class);
        return pickerController;
    }
}

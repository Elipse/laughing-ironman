/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.com.croer.picker.mvc;

/**
 *
 * @author elialva
 */
public interface PickerController {

    public void fetch(Object input);

    public void forward();

    public void backward();

    public void cancel();

}

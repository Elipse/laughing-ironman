/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.com.croer.picker.mvc;

import java.util.List;

/**
 *
 * @author elialva
 */
public interface PickerController<E> {

    public void fetch(Object input);

    public void forward();

    public void backward();

    public void makeSelection(int[] indexes);

    public void cancel();

}

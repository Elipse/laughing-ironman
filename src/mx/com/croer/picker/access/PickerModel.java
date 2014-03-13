/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.com.croer.picker.access;

import mx.com.croer.picker.mvc.BrowseListener;

/**
 *
 * @author elialva
 */
public interface PickerModel {

    public void fetch(Object input);

    public int forward();

    public int backward();

    public void cancel();

    public void addBrowseListener(BrowseListener sl);

    public void removeBrowseListener(BrowseListener sl);

}

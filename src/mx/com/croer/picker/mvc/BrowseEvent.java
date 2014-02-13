/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.com.croer.picker.mvc;

import java.util.Map.Entry;

/**
 *
 * @author elialva
 */
public class BrowseEvent {

    public static final int PAGE = 1;
    public static final int ENTRY = 2;
    public static final int COUNT = 3;
    public static final int PROGRESS = 4;

    private final PickerModel model;
    private final Entry<String, Object> property;

    /**
     *
     * @param model
     * @param property
     */
    public BrowseEvent(PickerModel model, Entry<String, Object> property) {
        this.model = model;
        this.property = property;
    }

    /**
     * @return the model
     */
    public PickerModel getModel() {
        return model;
    }

    /**
     * @return the beanList
     */
    public Entry<String, Object> getProperty() {
        return property;
    }
}

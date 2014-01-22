/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.com.croer.picker.mvc;

import mx.com.croer.picker.model.PickerModel;
import java.util.List;
import java.util.Map.Entry;
import javax.swing.Icon;

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
    private final Object argument;
    private final int type;

    public BrowseEvent(PickerModel model, List argument, int type) {
        this.model = model;
        this.argument = argument;
        this.type = type;
    }

    public BrowseEvent(PickerModel model, Entry<Object, Icon> argument, int type) {
        this.model = model;
        this.argument = argument;
        this.type = type;
    }

    public BrowseEvent(PickerModel model, Integer argument, int type) {
        this.model = model;
        this.argument = argument;
        this.type = type;
    }

    /**
     * @return the model
     */
    public PickerModel getModel() {
        return model;
    }

    /**
     * @return the argument
     */
    public Object getArgument() {
        return argument;
    }

    /**
     * @return the type
     */
    public int getType() {
        return type;
    }
}

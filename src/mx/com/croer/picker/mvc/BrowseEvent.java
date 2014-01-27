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
public class BrowseEvent {

    public static final int PAGE = 1;
    public static final int ENTRY = 2;
    public static final int COUNT = 3;
    public static final int PROGRESS = 4;

    private final PickerModel model;
    private final List beanList;
    private final List<String> propList;
    private final Integer position;

    /**
     *
     * @param model
     * @param beanList A list of Beans
     * @param propList A list of entries made up of String, String
     * @param position null means all of the list must be displayed, not null
     * means just an instance should be refreshed
     */
    public BrowseEvent(PickerModel model, List beanList, List propList, Integer position) {
        this.model = model;
        this.beanList = beanList;
        this.propList = propList;
        this.position = position;
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
    public List getBeanList() {
        return beanList;
    }

     /**
     * @return the propList
     */
    public List getPropList() {
        return propList;
    }

    /**
     * @return the position
     */
    public Integer getPosition() {
        return position;
    }
}

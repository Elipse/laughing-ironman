/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.com.croer.picker.access;

import mx.com.croer.picker.mvc.Item;

/**
 *
 * @author elialva
 */
public interface BusinessFetcher {
    
    public <T extends Object> T createBusinessObject(Object key, Class<T> type);

    public Item createItem(Object item);

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.com.croer.picker.access;

import mx.com.croer.picker.mvc.ItemProxy;

/**
 *
 * @author elialva
 */
public interface ItemCreator {
    
    public String getCandidateSql();
    
    public String geXHintSql();

    public <T extends Object> T createItem(Object key, Class<T> type);

    public ItemProxy createItemProxy(Object item);

}

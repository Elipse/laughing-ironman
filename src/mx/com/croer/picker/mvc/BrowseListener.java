/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.com.croer.picker.mvc;

import java.util.EventListener;

/**
 *
 * @author elialva
 */
public interface BrowseListener extends EventListener {

    void update(BrowseEvent e);
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.com.croer.picker.mvc;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;
import javax.swing.event.EventListenerList;
import mx.com.croer.picker.mvc.BrowseEvent;
import mx.com.croer.picker.mvc.BrowseListener;

/**
 *
 * @author elialva
 */
public abstract class PickerModel {

    protected EventListenerList listenerList = new EventListenerList();
    protected final PropertyChangeSupport propertySupport = new PropertyChangeSupport(this);

    public abstract void fetch(Object input);

    public abstract void forward();

    public abstract void backward();

    public abstract void cancel();

    public synchronized void addBrowseListener(BrowseListener sl) {
        listenerList.add(BrowseListener.class, sl);
    }

    public synchronized void removeBrowseListener(BrowseListener sl) {
        listenerList.remove(BrowseListener.class, sl);
    }

    protected void fireSearchPerformed(BrowseEvent e) {
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();

        //SearchEvent e = new SearchEvent(this, SearchEvent.SEARCH_PERFORMED, this.message, this.list);
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == BrowseListener.class) {
                ((BrowseListener) listeners[i + 1]).update(e);
            }
        }
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.removePropertyChangeListener(listener);
    }

    List makeSelection(List selectionList) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.com.croer.picker.access;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import mx.com.croer.entities.proxy.Item;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.Icon;
import javax.swing.SwingWorker;
import javax.swing.event.EventListenerList;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import mx.com.croer.picker.mvc.BrowseEvent;
import mx.com.croer.picker.mvc.BrowseListener;

/**
 *
 * @author elialva
 *
 */
public class PickerModelImpl implements PickerModel {

    private int pageSize;               //Tamaño de la página
    private List listPageHeader;        //Encabezados de cada página
    private int pageNumber;             //Número de página que se muestra
    //Business
    private DataPicker dataPicker;
    private BeanWorker beanWorker = new BeanWorker(null, null, 0, null);
    private IconWorker iconWorker = new IconWorker(null);
    private final ClassListener classListener = new ClassListener();
    private Object sync;
    private boolean backward;
    private boolean forward;
    private List resultList;
    protected EventListenerList listenerList = new EventListenerList();
    private int direction;

    public PickerModelImpl() {
    }

    public PickerModelImpl(DataPicker dataPicker) {
        this.dataPicker = dataPicker;
    }

    public void setDataPicker(DataPicker dataPicker) {
        this.dataPicker = dataPicker;
        if (listPageHeader != null && listPageHeader.size() > 0) { //Hay una búsqueda activa, re-ejecuta desde el inicio
            fetch((String) listPageHeader.get(0));
        }
    }

    @Override
    public void fetch(Object input) {
        System.out.println("dataPicker " + dataPicker);
        pageSize = dataPicker.createPageSize();
        pageNumber = 1;
        fireSearchPerformed(new BrowseEvent(PickerModelImpl.this, new SimpleEntry<String, Object>("page", pageNumber)));
        listPageHeader = new ArrayList();
        System.out.println("Pushing..." + input);
        listPageHeader.add(input);
        setForward(false);
        setBackward(false);
        displayPage();
    }

    @Override
    public int forward() {
        System.out.println("forward " + forward);
        if (!forward || pageNumber + 1 > listPageHeader.size()) {
            return 0;
        }
        pageNumber++;
        fireSearchPerformed(new BrowseEvent(PickerModelImpl.this, new SimpleEntry<String, Object>("page", pageNumber)));
        setBackward(true);
        setForward(!(pageNumber == listPageHeader.size()));
        displayPage();
        return pageNumber;
    }

    @Override
    public int backward() {
        System.out.println("backward " + backward);
        if (!backward || pageNumber - 1 < 1) {
            return 0;
        }
        pageNumber--;
        fireSearchPerformed(new BrowseEvent(PickerModelImpl.this, new SimpleEntry<String, Object>("page", pageNumber)));
        setBackward(!(pageNumber == 1));
        setForward(true);
        displayPage();
        return pageNumber;
    }

    @Override
    public void cancel() {

    }

    private void displayPage() {

        sync = new Object();  //Cambia el pulso sincronico
        
        if (!iconWorker.isDone()) {  //TODO Revisar si es necesario para los demás
            iconWorker.cancel(false);  //Cancela búsqueda en proceso y se jecuta done()
        }
        iconWorker.removePropertyChangeListener(classListener);

        //Con false brinca a done() pero el thread sigue, con true se interrumpe el thread
        beanWorker.cancel(true);  //Cancela búsqueda en proceso y se jecuta done()
        beanWorker.removePropertyChangeListener(classListener);

        Object pageHeader = listPageHeader.get(pageNumber - 1);  //TODO WOW Da por echo q la búsqueda previa terminó
        //No puedo avanzar a la siguiente si no termino la consulta actual
        iconWorker = new IconWorker(sync);
        iconWorker.addPropertyChangeListener(classListener);

        beanWorker = new BeanWorker(sync, pageHeader, pageNumber, iconWorker);  //se agrega a constr para no hacer referencias a PickerModelImpl
        beanWorker.addPropertyChangeListener(classListener);

        System.out.println("beforeExecute " + beanWorker);
        beanWorker.execute();  //beanWorker ejecuta iconWorker.execute()
        System.out.println("afterExecute " + beanWorker);
    }

    @Override
    public void addBrowseListener(BrowseListener sl) {
        listenerList.add(BrowseListener.class, sl);
    }

    @Override
    public void removeBrowseListener(BrowseListener sl) {
        listenerList.remove(BrowseListener.class, sl);
    }

    protected void fireSearchPerformed(BrowseEvent e) {
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();

        //SearchEvent e = new SearchEvent(this, SearchEvent.SEARCH_PERFORMED, this.message, this.list);
        // Process the listeners last to first, notifying
        // those that are interested in this event
//        System.out.println("HAY ALGUINE " + listeners[1].getClass());
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == BrowseListener.class) {
                ((BrowseListener) listeners[i + 1]).update(e);
            }
        }
    }

    /**
     * @param backward the backward to set
     */
    private void setBackward(boolean backward) {
        this.backward = backward;
        fireSearchPerformed(new BrowseEvent(this, new SimpleEntry<String, Object>("backward", backward)));
    }

    /**
     * @param forward the forward to set
     */
    private void setForward(boolean forward) {
        this.forward = forward;
        fireSearchPerformed(new BrowseEvent(this, new SimpleEntry<String, Object>("forward", forward)));
    }

    private void postResults(PropertyChangeEvent evt) {
        String propertyName = evt.getPropertyName();

        if (propertyName.equals("resultList")) {
            resultList = (List) evt.getNewValue();
            BrowseEvent e = new BrowseEvent(PickerModelImpl.this, new SimpleEntry<String, Object>("list", resultList));
            fireSearchPerformed(e);
        }

        if (propertyName.equals("entry")) {
            SimpleEntry<Object, Icon> entry = (SimpleEntry<Object, Icon>) evt.getNewValue();
            Item key = (Item) entry.getKey();

            Icon value = entry.getValue();

            int index = resultList.indexOf(key);
            if (index < 0) {
                throw new IllegalStateException("Abnormal ending - Thread logic is wrong");
            }

            Item item = (Item) resultList.get(index);
            item.setImage(value);

            BrowseEvent e = new BrowseEvent(PickerModelImpl.this, new SimpleEntry<String, Object>("image", index));
            fireSearchPerformed(e);
        }
    }

    private class BeanWorker extends SwingWorker<List, Void> {

        private final Object sync;
        private final Object pageHeader;
        private final int pageNumber;
        private final IconWorker iconWorker;

        public BeanWorker(Object sync, Object pageHeader, int pageNumber, IconWorker iconWorker) {
            this.sync = sync;
            this.pageHeader = pageHeader;
            this.pageNumber = pageNumber;
            this.iconWorker = iconWorker;
            System.out.println("Creando BeanWorker");
        }

        @Override
        protected List doInBackground() throws Exception {
            //Si ocurre una excepcion aqui manda llamar a done de inmediato
            System.out.println("BeanWorker.pageHeader " + pageHeader);
            List l = PickerModelImpl.this.dataPicker.readPage(pageHeader, pageNumber);  //dataPicker sync or local variables always
            return l;
        }

        @Override
        protected void done() {
//            if (isCancelled() || this.sync != PickerModelImpl.this.sync) { //Si está activa otra lista: termina
            if (this.sync != PickerModelImpl.this.sync) { //Si está activa otra lista: termina
                System.out.println("ASYNCOXXX");
                return;
            }

            List resultList = new ArrayList();
            try {
                System.out.println("af" + this.getState().name() + " getop " + get());
                resultList.addAll(get());
                System.out.println("setForward " + resultList.size() + "*" + pageSize);

//                setBackward(pageNumber > 1);
                setForward(resultList.size() > pageSize);

                if (resultList.size() > pageSize && pageNumber == listPageHeader.size()) {
                    listPageHeader.add(resultList.get(pageSize));
                }
                int i = pageSize > resultList.size() ? resultList.size() : pageSize;
                resultList = resultList.subList(0, i);
            } catch (Exception ex) {  //Cacha las excepciones de doInBackground
                System.out.println("beer" + this.getState().name());
                System.out.println("exceptionBeanWorker " + ex);
////                Exceptions.printStackTrace(ex);
            }

            resultList = Collections.unmodifiableList(resultList); //Donde va???
            firePropertyChange("resultList", null, resultList);

//            if (PickerModelImpl.this.dataPicker.hasIcon()) {
            this.iconWorker.setResultList(resultList);
            this.iconWorker.execute();
//            }
        }
    }

    private class IconWorker extends SwingWorker<Void, SimpleEntry<Object, Icon>> {

        private final Object sync;
        private List resultList;

        public IconWorker(Object sync) {
            this.sync = sync;
            this.resultList = new ArrayList();
        }

        private void setResultList(List resultList) {
            this.resultList = resultList;
        }

        @Override
        protected Void doInBackground() throws Exception {
            for (Object bean : resultList) {
                Icon icon = PickerModelImpl.this.dataPicker.readIcon((Item) bean);
                SimpleEntry<Object, Icon> entry = new SimpleEntry<Object, Icon>(bean, icon);
                if (isCancelled()) {
                    break;
                }
                publish(entry);
            }
            return null;
        }

        @Override
        protected void process(List<SimpleEntry<Object, Icon>> chunks) {
            if (this.sync != PickerModelImpl.this.sync) { //Si está activa otra lista: termina
                return;
            }

            for (SimpleEntry<Object, Icon> entry : chunks) {
                firePropertyChange("entry", null, entry);
            }
        }

        @Override
        protected void finalize() throws Throwable {
            try {
                System.out.println(this + " I'm leaving now");
            } finally {
                super.finalize();
            }
        }
    }

    private class ClassListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            PickerModelImpl.this.postResults(evt);
        }
    }
}

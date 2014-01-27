/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.com.croer.picker.mvc;

import java.beans.*;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.SwingWorker;
import mx.com.croer.picker.mvc.BrowseEvent;
//import org.openide.util.Exceptions;

/**
 *
 * @author elialva
 *
 */
public class PickerModelImpl extends PickerModel {

    private int pageSize;               //Tamaño de la página
    private List listPageHeader;        //Encabezados de cada página
    private int pageNumber;             //Número de página que se muestra
    //Business
    private DataPicker dataPicker;
    private BeanWorker beanWorker = new BeanWorker(null, null, 0, null);
    private IconWorker iconWorker = new IconWorker(null);
    private CountWorker countWorker = new CountWorker(null, null);
    private final ClassListener classListener = new ClassListener();
    private Object sync;
    private boolean backward;
    private boolean forward;
    private List resultList;

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

        List list = new ArrayList();
        
        List propList = new ArrayList();
        
        Icon image = new ImageIcon("C:\\Users\\IBM_ADMIN\\Documents\\@Projects_Eli\\201309 Finder&Getter\\_NBPOtros\\JavaProject1\\src\\mx\\com\\croer\\picker\\mvc\\Banana-icon.png");

        System.out.println("imagNe " + image.getIconHeight());
        
        list.add(new Producto("leche", image,"lala"));
        list.add(new Producto("mango", image,"frut&veg"));
        list.add(new Producto("platanos", image,"frut&veg"));
               
        fireSearchPerformed(new BrowseEvent(this, list, null));

        if (true) {
            return;
        }

        pageSize = dataPicker.getPageSize();
        pageNumber = 1;
        listPageHeader = new ArrayList();
        listPageHeader.add(input);
        setForward(false);
        setBackward(false);
        displayPage(+0);
    }

    @Override
    public void forward() {
        if (!forward) {
            return;
        }
        displayPage(+1);
    }

    @Override
    public void backward() {
        if (!backward) {
            return;
        }
        displayPage(-1);
    }

    @Override
    public void cancel() {

    }

    private void displayPage(int direction) {

        sync = new Object();  //Cambia el pulso sincronico

        if (!iconWorker.isDone()) {  //TODO Revisar si es necesario para los demás
            iconWorker.cancel(false);  //Cancela búsqueda en proceso y se jecuta done()
        }
        iconWorker.removePropertyChangeListener(classListener);

        beanWorker.cancel(false);  //Cancela búsqueda en proceso y se jecuta done()
        beanWorker.removePropertyChangeListener(classListener);

        pageNumber += direction;
        setBackward(pageNumber == 1 ? false : true);
        Object pageHeader = listPageHeader.get(pageNumber - 1);

        iconWorker = new IconWorker(sync);
        iconWorker.addPropertyChangeListener(classListener);

        beanWorker = new BeanWorker(sync, pageHeader, pageSize + 1, iconWorker);  //se agrega a constr para no hacer referencias a PickerModelImpl
        beanWorker.addPropertyChangeListener(classListener);

        beanWorker.execute();  //beanWorker ejecuta iconWorker.execute()

        if (direction == 0) {  //Inicia nueva búsqueda
            countWorker.cancel(false);
            countWorker.removePropertyChangeListener(classListener);
            countWorker = new CountWorker(sync, pageHeader);
            countWorker.addPropertyChangeListener(classListener);
            countWorker.execute();
        }
    }

    /**
     * @param backward the backward to set
     */
    private void setBackward(boolean backward) {
        boolean oldValue = this.backward;
        this.backward = backward;
        propertySupport.firePropertyChange("backward", oldValue, this.backward);
    }

    /**
     * @param forward the forward to set
     */
    private void setForward(boolean forward) {
        boolean oldValue = this.forward;
        this.forward = forward;
        propertySupport.firePropertyChange("forward", oldValue, this.forward);
    }

    private void postResults(PropertyChangeEvent evt) {
        String propertyName = evt.getPropertyName();

        /* From MVC PATTERN XEAM */
        BrowseEvent e = new BrowseEvent(PickerModelImpl.this, new ArrayList(), null);
        fireSearchPerformed(e);

        if (propertyName.equals("resultList")) {
            resultList = (List) evt.getNewValue();
            setForward(resultList.size() > pageSize ? true : false);
            if (resultList.size() > pageSize && pageNumber > listPageHeader.size()) {
                listPageHeader.add(resultList.get(pageSize + 1));
            }
            propertySupport.firePropertyChange(evt);
        }

        if (propertyName.equals("entry")) {
            SimpleEntry<Object, Icon> entry = (SimpleEntry<Object, Icon>) evt.getNewValue();
            Object key = entry.getKey();
            Icon value = entry.getValue();

            int index = resultList.indexOf(key);
            if (index < 0) {
                throw new IllegalStateException("Abnormal ending - Thread logic is wrong");
            }
            propertySupport.fireIndexedPropertyChange("entry", index, key, value);
        }

        if (propertyName.equals("progress") || propertyName.equals("count")) {
            propertySupport.firePropertyChange(evt);
        }
    }

    private class BeanWorker extends SwingWorker<List, Void> {

        private final Object sync;
        private final Object pageHeader;
        private final int rows;
        private final IconWorker iconWorker;

        public BeanWorker(Object sync, Object pageHeader, int rows, IconWorker iconWorker) {
            this.sync = sync;
            this.pageHeader = pageHeader;
            this.rows = rows;
            this.iconWorker = iconWorker;
        }

        @Override
        protected List doInBackground() throws Exception {
            List o = PickerModelImpl.this.dataPicker.readPage(pageHeader);  //dataPicker sync or local variables always
            System.out.println("evenflo " + o);
            return o;
        }

        @Override
        protected void done() {
//            if (isCancelled() || this.sync != PickerModelImpl.this.sync) { //Si está activa otra lista: termina
            if (this.sync != PickerModelImpl.this.sync) { //Si está activa otra lista: termina
                return;
            }

            List resultList = new ArrayList();
            try {
                System.out.println("af" + this.getState().name());
                resultList.addAll(get());
            } catch (Exception ex) {
                System.out.println("beer" + this.getState().name());
//                Exceptions.printStackTrace(ex);
            }

            resultList = Collections.unmodifiableList(resultList);

            firePropertyChange("resultList", null, resultList);

            if (PickerModelImpl.this.dataPicker.hasIcon()) {
                this.iconWorker.setResultList(resultList);
                this.iconWorker.execute();
            }
        }
    }

    private class IconWorker extends SwingWorker<Void, SimpleEntry<Object, Icon>> {

        private final Object sync;
        private List resultList;
        private int progress;

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
                Icon icon = PickerModelImpl.this.dataPicker.createIcon(bean);
                SimpleEntry<Object, Icon> entry = new SimpleEntry<Object, Icon>(bean, icon);
                if (isCancelled()) {
                    break;
                }
                publish(entry);
//                Thread.sleep(2000);
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
                setProgress(++progress / resultList.size());
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

    private class CountWorker extends SwingWorker<Integer, Void> {

        private final Object sync;
        private final Object input;

        public CountWorker(Object sync, Object input) {
            this.sync = sync;
            this.input = input;
        }

        @Override
        protected Integer doInBackground() throws Exception {
            return PickerModelImpl.this.dataPicker.countRows(input); //dataPicker sync or local variables always
        }

        @Override
        protected void done() {
            if (this.sync != PickerModelImpl.this.sync) { //Si está activa otra lista: termina
                return;
            }

            Integer count = new Integer(0);
            try {
                count = get();
            } catch (Exception ex) {
//                Exceptions.printStackTrace(ex);
            }

            firePropertyChange("count", new Integer(0), count);
        }
    }

    private class ClassListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            PickerModelImpl.this.postResults(evt);
        }
    }
}

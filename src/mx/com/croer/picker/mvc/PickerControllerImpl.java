/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.com.croer.picker.mvc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.Icon;
import javax.swing.text.JTextComponent;

public final class PickerControllerImpl implements PickerController {

    private final PickerModel model;
    private final PickerView view;
    private final JTextComponent textComponent;

    public PickerControllerImpl(JTextComponent textComponent, Class clas) {
        this.textComponent = textComponent;
        model = createModel(clas);
        view = createView(PickerControllerImpl.this, model, clas);
    }

    protected PickerModel createModel(Class clas) {
        DataPicker dataPicker = createDataPicker(clas);
        PickerModel localModel = new PickerModelImpl(dataPicker);
        return localModel;
    }

    protected DataPicker createDataPicker(Class clas) {
        switch (clas.getSimpleName()) {
            case "ProductoSearch":
                return new DataPickerImp();
            default:
                break;
        }
        return null;
    }

    protected PickerView createView(PickerController controller, PickerModel model, Class clas) {
        List<BeanColumn> net = createNet(clas);
        PickerView localView = new PickerView(textComponent, controller, model, net);
        return localView;
    }

    protected List<BeanColumn> createNet(Class clas) {
        List<BeanColumn> beanColumnList = new ArrayList<>();

        beanColumnList.add(new BeanColumn("idproducto", "Desc", String.class, null, true, 100, false));
//        beanColumnList.add(new BeanColumn("descripcion", "Mark", String.class, null, true, 100, false));
        beanColumnList.add(new BeanColumn("image", "Imagen", Icon.class, null, true, 64, false));
        beanColumnList.add(new BeanColumn("selection", "#", Integer.class, null, true, 15, false));
        return beanColumnList;
    }

    @Override
    public void fetch(Object input) {
        model.fetch(input);
        view.displayPopup(true);
        view.startProgess();
    }

    @Override
    public void forward() {
        model.forward();
        view.startProgess();
    }

    @Override
    public void backward() {
        model.backward();
        view.startProgess();
    }

    @Override
    public void cancel() {
        model.cancel();
        view.displayPopup(false);
        view.stopProgress();
    }

    @Override
    public void makeSelection(List objects) {
        Map<Object, Integer> map = new HashMap<Object, Integer>();

        for (Object object : objects) {
            Item item = (Item) object;
            map.put(item.getSource(), item.getSelection());
        }

        for (Map.Entry<Object, Integer> entry : map.entrySet()) {
            Object object = entry.getKey();
            Integer integer = entry.getValue();
            System.out.println("PartnerUp " + object.getClass() + " / " + integer);
        }
        //Post objects
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.com.croer.picker.mvc;

import java.util.ArrayList;
import java.util.List;
import javax.swing.Icon;
import javax.swing.text.JTextComponent;

public final class PickerControllerImpl implements PickerController {

    private final PickerModel model;
    private final PickerView view;
    private final JTextComponent textComponent;

    public PickerControllerImpl(JTextComponent textComponent, Class clas) {
        this.textComponent = textComponent;
        model = createModel(clas);
        view = createView(PickerControllerImpl.this, model,clas);  
    }

    protected PickerModel createModel(Class clas) {
        DataPicker dataPicker = createDataPicker(clas);
        PickerModel localModel = new PickerModelImpl(dataPicker);
        return localModel;
    }

    protected DataPicker createDataPicker(Class clas) {
        switch (clas.getName()) {
            case "Producto":
                break;
            default:
                break;
        }
        return null;
    }

    protected PickerView createView(PickerController controller, PickerModel model,Class clas) {
        List<BeanColumn> net = createNet(clas);
        PickerView localView = new PickerView(textComponent, controller, model,net);
        return localView;
    }

    protected List<BeanColumn> createNet(Class clas) {
        List<BeanColumn> beanColumnList = new ArrayList<>();
        
        beanColumnList.add(new BeanColumn("descripcion", "Desc", String.class, null, true, 100, false));
        beanColumnList.add(new BeanColumn("marca", "Mark", String.class, null, true, 100, false));
        beanColumnList.add(new BeanColumn("image", "Imagen", Icon.class, null, true, 64, false));
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
}

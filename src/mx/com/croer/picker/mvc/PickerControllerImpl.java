/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.com.croer.picker.mvc;

import mx.com.croer.picker.model.PickerModel;
import mx.com.croer.picker.model.PickerModelImpl;
import javax.swing.text.JTextComponent;
import mx.com.croer.picker.model.DataPicker;

public final class PickerControllerImpl implements PickerController {

    private final PickerModel model;
    private final PickerView view;
    private final JTextComponent textComponent;
    private final Class clas;

    public PickerControllerImpl(JTextComponent textComponent, Class clas) {
        this.textComponent = textComponent;
        this.clas = clas;
        model = createModel();
        view = createView(PickerControllerImpl.this, model);
    }

    protected PickerModel createModel() {
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

    protected PickerView createView(PickerController controller, PickerModel model) {
        PickerView localView = new PickerView(textComponent, controller, model);
        return localView;
    }

    @Override
    public void fetch(Object input) {
        model.fetch(input);
        view.displayView(true);
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
        view.displayView(false);
        view.stopProgress();
    }

}

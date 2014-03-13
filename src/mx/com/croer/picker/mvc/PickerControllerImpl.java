/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.com.croer.picker.mvc;

import mx.com.croer.picker.access.PickerModelImpl;
import mx.com.croer.picker.access.PickerModel;
import mx.com.croer.picker.access.DataPicker;
import java.awt.Color;
import java.awt.Component;
import java.util.AbstractMap;
import mx.com.croer.entities.proxy.Item;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import javax.swing.text.JTextComponent;
import mx.com.croer.picker.access.SearchFetcher;
import org.springframework.context.support.ClassPathXmlApplicationContext;

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
            case "X":
//                DataPickerImp dp=new DataPickerImp();
//                dp.setType(clas);                
//                return dp;
                break;
            default:
                ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring.xml");
                SearchFetcher dp = (SearchFetcher) context.getBean("searchFetcher");
//                DataPickerImp dp = new DataPickerImp();
//                SearchFetcher dp = new SearchFetcher();
                dp.setType(clas);
                return dp;
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

//        beanColumnList.add(new BeanColumn("idproducto", "Desc", String.class, null, true, 100, false));
        beanColumnList.add(new BeanColumn("context", "Mark", String.class, new TableCellRendererX(), true, 200, false));
//        beanColumnList.add(new BeanColumn("context", "Mark", String.class, null, true, 100, false));
        beanColumnList.add(new BeanColumn("alignment", "Alinea", List.class, null, false, 64, false));
        beanColumnList.add(new BeanColumn("image", "Imagen", Icon.class, null, true, 64, false));
        beanColumnList.add(new BeanColumn("selection", "#", Integer.class, null, true, 5, false));
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
        int f = model.forward();
        if (f != 0) {
            view.startProgess();
        }
    }

    @Override
    public void backward() {
        if (model.backward() != 0) {
            view.startProgess();
        }
    }

    @Override
    public void cancel() {
        model.cancel();
        view.displayPopup(false);
        view.stopProgress();
    }

    @Override
    public void makeSelection(List objects) {
        Map<Object, Integer> map = new HashMap<>();

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

    private class TableCellRendererX extends JLabel implements TableCellRenderer {

        private final String[] trueColors = new String[]{"Fuchsia", "Blue", "Red"};

        public TableCellRendererX() {
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            int rowModel = table.convertRowIndexToModel(row);
            int columnCount = table.getModel().getColumnCount();
            List<AbstractMap.SimpleEntry<Integer, Integer>> alignment = null;

            for (int i = 0; i < columnCount; i++) {
                if (table.getModel().getColumnName(i).equalsIgnoreCase("Alinea")) {
                    alignment = (List<AbstractMap.SimpleEntry<Integer, Integer>>) table.getModel().getValueAt(rowModel, i);
                    break;
                }
            }

            if (isSelected) {
                setBackground(table.getSelectionBackground());
            } else {
                if (row % 2 == 0) {
//                    setBackground(UIManager.getColor("Table.alternateRowColor"));
                    setBackground(Color.WHITE);
                } else {
                    setBackground(table.getBackground());
                }
            }

            setText(colorByPosition(value == null ? "" : value.toString(), alignment));

            return this;
        }

        public String colorByPosition(String text, List<AbstractMap.SimpleEntry<Integer, Integer>> alignment) {

            if (text == null) {
                return "";
            }

            if (alignment == null) {
                return text;
            }

            ArrayList<String> posColor = new ArrayList<String>();
            int j = 0;
            for (AbstractMap.SimpleEntry<Integer, Integer> simpleEntry : alignment) {
                String s
                        = String.format("%05d", simpleEntry.getKey()) + ":"
                        + String.format("%05d", simpleEntry.getValue()) + ":"
                        + trueColors[j++ % trueColors.length];
                posColor.add(s);
            }

            Collections.sort(posColor);

            String color = "<b><font color=#colorname#>#text#</font></b>";
            String htmlText = "";

            int ind = 0;
            for (String color_pos : posColor) {
                String[] split = color_pos.split(":");
                int begin = Integer.parseInt(split[0]);
                int end = Integer.parseInt(split[1]);
                String subText = text.substring(begin, begin + end);
                String colorString = color.replaceAll("#colorname#", split[2]).replaceAll("#text#", subText);
                htmlText += text.substring(ind, begin) + colorString;
                ind = begin + end;
            }

            htmlText += text.substring(ind);

            return "<html>" + htmlText + "</html>";
        }
    }
}

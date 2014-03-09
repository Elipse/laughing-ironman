/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.com.croer.picker.mvc;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import mx.com.croer.entities.proxy.Item;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumn;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.JTextComponent;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import org.apache.commons.lang3.StringUtils;
import org.jdesktop.beansbinding.AutoBinding.UpdateStrategy;
import org.jdesktop.beansbinding.BindingGroup;
import org.jdesktop.beansbinding.ELProperty;
import org.jdesktop.observablecollections.ObservableCollections;
import org.jdesktop.swingbinding.JTableBinding;
import org.jdesktop.swingbinding.JTableBinding.ColumnBinding;
import org.jdesktop.swingbinding.SwingBindings;

/**
 *
 * @author elialva
 */
public class PickerView implements BrowseListener, ListSelectionListener {

    private JTextComponent textComponent;
    private PickerViewPanel panel;
    private JTable table;
    private PickerController controller;
    private PickerModel model;
    private JPopupMenu popup;
    private boolean done = true;
    private List list;
    private int count;
    private BindingGroup bindingGroup;
    private JTableBinding jTableBinding;
    private final List<BeanColumn> crisscross;
    private int position;

    private boolean backward;
    private boolean forward;

    public PickerView(JTextComponent textComponent, PickerController controller, PickerModel model, final List<BeanColumn> crisscross) {
        this.textComponent = textComponent;
        this.controller = controller;
        this.model = model;
        this.crisscross = crisscross;

        //Listen to the model's changes
        this.model.addBrowseListener(PickerView.this);

        //Listen to the text component's events and panel's events
        MultipleListener ml = new MultipleListener();
        this.textComponent.addKeyListener(ml);
        this.textComponent.addFocusListener(ml);
        this.textComponent.getDocument().addDocumentListener(ml);

        //Creates the floating panel
        this.panel = createPanel();
        this.panel.addPropertyChangeListener(ml);
        this.popup = new JPopupMenu();
        this.popup.setFocusable(false);
        this.popup.add(this.panel);

        configTable();
    }

    private PickerViewPanel createPanel() {
        PickerViewPanel localPanel = new PickerViewPanel();
        return localPanel;
    }

    private void keyPressedInTextComponent(KeyEvent e) {
        if (done) {
            if (popup.isShowing()) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_ENTER: {
                        selectRow(0);
                        displayPopup(false);
                        controller.makeSelection(list);
                        for (Object object : list) {
                            Item item = (Item) object;
                            item.setSelection(null);
                        }
                        e.consume();
                        break;
                    }
                    case KeyEvent.VK_ESCAPE: {
                        displayPopup(false);
                        break;
                    }
                    case KeyEvent.VK_PAGE_UP: {
                        controller.backward();
                        e.consume();
                        break;
                    }
                    case KeyEvent.VK_PAGE_DOWN: {
                        controller.forward();
                        e.consume();
                        break;
                    }
                    case KeyEvent.VK_UP: {
                        changeListSelectedIndex(-1);
                        break;
                    }

                    case KeyEvent.VK_DOWN: {
                        changeListSelectedIndex(+1);
                        break;
                    }
                    case KeyEvent.VK_CONTROL:
                        String keys = KeyEvent.getModifiersExText(e.getModifiersEx());
                        switch (keys) {
                            case "Ctrl":
                                selectRow(+1);
                                break;
                            case "Ctrl+Shift":
                                selectRow(-1);
                                break;
                        }
                        break;
                    default:
                }
            } else {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_ENTER:
                        e.consume();
                    case KeyEvent.VK_PAGE_UP:
                    case KeyEvent.VK_PAGE_DOWN:
                    case KeyEvent.VK_UP:
                    case KeyEvent.VK_DOWN:
                        displayPopup(true);
                        break;
                }
            }
        } else {
            if (popup.isShowing()) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_ESCAPE:
                        this.controller.cancel();
                        break;
                }
            } else {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_ENTER:
                    case KeyEvent.VK_PAGE_UP:
                    case KeyEvent.VK_PAGE_DOWN:
                    case KeyEvent.VK_UP:
                    case KeyEvent.VK_DOWN:
                        this.controller.fetch(this.textComponent.getText()); //si se canceló empieza desde el inicio
                        break;
                }
            }
        }
    }

    private void documentChangeInTextComponent(DocumentEvent e) {
        this.controller.fetch(this.textComponent.getText());
        colorStyledDocument((DefaultStyledDocument) e.getDocument());
    }

    private void focusLostInTextComponent(FocusEvent e) {
        this.controller.cancel();
    }

    private void browseByClick(PropertyChangeEvent evt) {

        switch (evt.getPropertyName()) {
            case "UP":
                this.controller.backward();
                break;
            case "DOWN":
                this.controller.forward();
                break;
            case "MOUSEMOVED":
//                panel.getTable().getSelectionModel().setSelectionInterval((Integer) evt.getNewValue(), (Integer) evt.getNewValue());
                break;
            case "MOUSERELEASED":
//                table.addRowSelectionInterval((Integer) evt.getNewValue(), (Integer) evt.getNewValue());
                break;
        }
    }

    @Override
    public void update(BrowseEvent e) {
        String key = e.getProperty().getKey();
        Object value = e.getProperty().getValue();

        switch (key) {
            case "list":
                System.out.println("Lops " + value);
                list.addAll((List) value);
                if (list.size() > 0) {
                    count = 0;
                    panel.setProgress(count);
                    panel.setMessage("Listando...");
                } else {
                    panel.setProgress(100);
                    panel.setMessage("No hay registros");
                }
                break;
            case "image":
                double p = ((++count + 0.0) / list.size()) * 100;
                panel.setProgress((int) p);
                if (count == list.size()) {
                    panel.setMessage("Registros encontrados ");
                }
                break;
            case "backward":
                panel.setEnableBackward((boolean) value);
                backward = (boolean) value;
                break;
            case "forward":
                panel.setEnableForward((boolean) value);
                forward = (boolean) value;
                break;
            default:
                throw new AssertionError();
        }
    }

    public void colorStyledDocument(final DefaultStyledDocument document) {

        String[] styleNames = new String[]{"magentaStyle", "blueStyle", "redStyle"};
        final Style[] styles = new Style[styleNames.length];
        Color[] colors = new Color[]{Color.magenta, Color.blue, Color.red};
        StyleContext sc = new StyleContext();

        for (int i = 0; i < styles.length; i++) {
            styles[i] = sc.addStyle(styleNames[i], sc.getStyle(StyleContext.DEFAULT_STYLE));
            StyleConstants.setForeground(styles[i], colors[i]);
            StyleConstants.setBold(styles[i], true);
        }

        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                String input = "";
                try {
                    input = document.getText(0, document.getLength());
                } catch (BadLocationException ex) {
//                    Logger.getLogger(ColorPane.class.getName()).log(Level.SEVERE, null, ex);
                }

                StringBuilder inputMut = new StringBuilder(input);
                String[] split = StringUtils.split(inputMut.toString());
                int i = 0;
                for (String string : split) {
                    int start = inputMut.indexOf(string);
                    int end = start + string.length();
                    inputMut.replace(start, end, StringUtils.repeat(" ", string.length()));
                    document.setCharacterAttributes(start, string.length(), styles[i++ % styles.length], true);
                }
            }
        }
        );
    }

    private void configTable() {
        //Get the table from the floating panel so that configure the crisscross
        table = panel.getTable();
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getSelectionModel().addListSelectionListener(PickerView.this);

        bindingGroup = new BindingGroup();

//        if (jTableBinding != null) {
//            bindingGroup.removeBinding(jTableBinding);
//        }
        list = ObservableCollections.observableList(new ArrayList());
        jTableBinding = SwingBindings.createJTableBinding(UpdateStrategy.READ_WRITE, list, table);

        for (BeanColumn beanColumn : crisscross) {
            ColumnBinding columnBinding = jTableBinding.addColumnBinding(ELProperty.create("${" + beanColumn.getProperty() + "}"));
            columnBinding.setColumnName(beanColumn.getColName());
            columnBinding.setColumnClass(beanColumn.getClase());
        }

        bindingGroup.addBinding(jTableBinding);
        bindingGroup.bind();

        TableColumn columnT = table.getColumnModel().getColumn(0);
        int tableWidth = 0;

        //TO DO esta mal remover la columna aqui se defasa net versus Column Model
        List<TableColumn> removeList = new ArrayList();
        for (int i = 0; i < crisscross.size(); i++) {
            BeanColumn beanColumn = crisscross.get(i);
            TableColumn column = table.getColumnModel().getColumn(i);
            if (beanColumn.isVisible()) {
                column.setCellRenderer(beanColumn.getRenderer());
                column.setPreferredWidth(beanColumn.getWidth());
                tableWidth += beanColumn.getWidth();
            } else {
                removeList.add(column);
            }
        }

        for (TableColumn tableColumn : removeList) {
            table.getColumnModel().removeColumn(tableColumn);
        }

        table.setPreferredScrollableViewportSize(new Dimension(500, 70));
        table.setFillsViewportHeight(true);

        table.setRowHeight(64);
    }

    public void startProgess() {
        this.position = -1;
        list.clear();
        panel.setProgress(true);
        panel.setMessage("Buscando...");
    }

    public void stopProgress() {
    }

    public void displayPopup(boolean flag) {
        if (flag) {
            popup.setVisible(true);
            popup.show(this.textComponent, 1, this.textComponent.getHeight());
        } else {
            popup.setVisible(false);
        }
    }

    private void changeListSelectedIndex(int i) {
        position += i;
        int rows = table.getRowCount();

        if (position < 0) {
            position = rows - 1;
        }

        if (position >= rows) {
            position = 0;
        }

        table.setRowSelectionInterval(position, position);
    }

    private void selectRow(int amount) {
        if (table.getSelectedRow() == -1) {
            return;
        }

        int i = table.getSelectedRow();
        i = table.convertRowIndexToModel(i);

        Item item = (Item) list.get(i);
        i = item.getSelection() == null ? amount == 0 ? 1 : amount : item.getSelection() + amount;
        if (i > 0) {
            item.setSelection(i);
        } else {
            item.setSelection(null);
        }

        //In order to test the bind
        //Producto p = (Producto) item;
        //p.setDescripcion(p.getDescripcion() + ".");
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (e.getValueIsAdjusting()) {
            return;
        }

        Rectangle rect = table.getCellRect(table.getSelectedRow(), table.getSelectedColumn(), true);
        table.scrollRectToVisible(rect);

    }

    private class MultipleListener extends KeyAdapter implements DocumentListener, FocusListener, PropertyChangeListener {

        @Override
        public void keyPressed(KeyEvent e) {
            PickerView.this.keyPressedInTextComponent(e);
        }

        @Override
        public void insertUpdate(DocumentEvent e) {
            PickerView.this.documentChangeInTextComponent(e);
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            PickerView.this.documentChangeInTextComponent(e);
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
        }

        @Override
        public void focusGained(FocusEvent e) {
        }

        @Override
        public void focusLost(FocusEvent e) {
            PickerView.this.focusLostInTextComponent(e);
        }

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            PickerView.this.browseByClick(evt);
        }
    }
}

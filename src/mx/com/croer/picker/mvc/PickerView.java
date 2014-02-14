/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.com.croer.picker.mvc;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.text.JTextComponent;
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
public class PickerView implements BrowseListener {

    private JTextComponent textComponent;
    private PickerViewPanel panel;
    private JTable table;
    private PickerController controller;
    private PickerModel model;
    private JPopupMenu popup;
    private boolean done = true;
    private List list;
    private int count;
    private final BindingGroup bindingGroup;
    private JTableBinding jTableBinding;
    private List<BeanColumn> net;
    private int position;
    private final List<Integer> selected;
    private int gomez;

    public PickerView(JTextComponent textComponent, PickerController controller, PickerModel model, final List<BeanColumn> net) {
        this.textComponent = textComponent;
        this.controller = controller;
        this.model = model;
        this.net = net;
        this.panel = createPanel();

        this.model.addBrowseListener(PickerView.this);

        MultipleListener ml = new MultipleListener();
        this.textComponent.addKeyListener(ml);
        this.textComponent.addFocusListener(ml);
        this.textComponent.getDocument().addDocumentListener(ml);
        this.panel.addPropertyChangeListener(ml);

        popup = new JPopupMenu();
        popup.setFocusable(false);
        popup.add(panel);

        bindingGroup = new BindingGroup();
        list = ObservableCollections.observableList(new ArrayList());
        table = this.panel.getTable();
        table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        table.setModel(new AbstractTableModel() {

            @Override
            public int getRowCount() {
                return 0;
            }

            @Override
            public int getColumnCount() {
                return net.size();
            }

            @Override
            public Object getValueAt(int rowIndex, int columnIndex) {
                return null;
            }
        });
        TableColumn column = table.getColumnModel().getColumn(0);
        selected = new ArrayList<>();

//        column.setCellRenderer(new TableCellRendererX(selected));
        selected.add(0);
        selected.add(2);
        jTableBinding = SwingBindings.createJTableBinding(UpdateStrategy.READ_WRITE, list, this.panel.getTable());
        bindingGroup.addBinding(jTableBinding);
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
                        //setEntityWithoutNotification();
                        this.controller.makeSelection(list);
                        e.consume();
                        break;
                    }
                    case KeyEvent.VK_ESCAPE: {
                        displayPopup(false);
                        break;
                    }
                    case KeyEvent.VK_PAGE_UP: {
                        this.controller.backward();
                        e.consume();
                        break;
                    }
                    case KeyEvent.VK_PAGE_DOWN: {
                        this.controller.forward();
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
                        selectRow();
//                        boolean isSelected = table.isCellSelected(1, 1);
//                        DefaultTableCellRenderer defRender = (DefaultTableCellRenderer) table.getCellRenderer(1, 1);
//                        Component cellRenderer = defRender.getTableCellRendererComponent(table,
//                                "Huml", isSelected, false, 1, 1);
//                        cellRenderer.setBackground(Color.MAGENTA);

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
                configTable();
                list.addAll((Collection) value);
                count = 0;
                panel.setProgress(count);
                panel.setMessage("Listando...");
                break;
            case "image":
                int positionk = (int) value;
                panel.setProgress(++count % list.size());
                if (count == list.size()) {
                    panel.setMessage("Registros encontrados");
                }
                break;
            case "backward":
                panel.setEnableBackward((boolean) value);
                break;
            case "forward":
                panel.setEnableForward((boolean) value);
                break;
            default:
                throw new AssertionError();
        }
    }

    private void configTable() {

        bindingGroup.removeBinding(jTableBinding);

        list = ObservableCollections.observableList(new ArrayList());
        jTableBinding = SwingBindings.createJTableBinding(UpdateStrategy.READ_WRITE, list, table);

        for (BeanColumn beanColumn : net) {
            ColumnBinding columnBinding = jTableBinding.addColumnBinding(ELProperty.create("${" + beanColumn.getProperty() + "}"));
            columnBinding.setColumnName(beanColumn.getColName());
            columnBinding.setColumnClass(beanColumn.getClase());
        }

        bindingGroup.addBinding(jTableBinding);
        bindingGroup.bind();

        TableColumn columnT = table.getColumnModel().getColumn(0);
//        columnT.setCellRenderer(new TableCellRendererX(list));
        System.out.println("Cellis " + " column " + columnT.getIdentifier() + " dd " + columnT.getCellRenderer());
        table.updateUI();

        System.out.println("jTable.getModel() " + table.getSelectionModel());

        int tableWidth = 0;

        //TO DO esta mal remover la columna aqui se defasa net versus Column Model
        for (int i = 0; i < net.size(); i++) {
            BeanColumn beanColumn = net.get(i);
            TableColumn column = table.getColumnModel().getColumn(i);
            if (beanColumn.isVisible()) {
                column.setCellRenderer(beanColumn.getRenderer());
                TableCellRenderer cellRenderer = column.getCellRenderer();
                System.out.println("col " + i + " cellop " + cellRenderer);
                column.setPreferredWidth(beanColumn.getWidth());
                tableWidth += beanColumn.getWidth();
            } else {
                table.getColumnModel().removeColumn(column);
            }
        }

        table.setRowHeight(64);
    }

    public void startProgess() {
        this.position = -1;
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
        this.position += i;
        int rows = table.getRowCount();

        if (position < 0) {
            position = rows - 1;
        }

        if (position >= rows) {
            position = 0;
        }

        this.table.setRowSelectionInterval(position, position);
//        table.clearSelection();
    }

    private void selectRow() {
        int selectedRow = table.getSelectedRow();
        Item item = (Item) list.get(selectedRow);
        System.out.println("gomez " + gomez);
        item.setSelection(gomez++);
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

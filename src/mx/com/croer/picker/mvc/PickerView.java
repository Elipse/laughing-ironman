/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.com.croer.picker.mvc;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import javax.swing.JPopupMenu;
import javax.swing.JProgressBar;
import javax.swing.JTable;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
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
    private PickerController controller;
    private PickerModel model;
    private JPopupMenu popup;
    private boolean done = true;
    private List list;
    private int count;
    private final BindingGroup bindingGroup;
    private JTableBinding jTableBinding;
    private List<BeanColumn> net;

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
        JTable tabla = this.panel.getTable();
        tabla.setModel(new AbstractTableModel() {

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
                        
                        //Enviar los índices de el Modelo subyacente
                        int[] indexes = new int[5];
                        this.controller.makeSelection(indexes);
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
//                    changeListSelectedIndex(-1);
                        break;
                    }

                    case KeyEvent.VK_DOWN: {
//                    changeListSelectedIndex(1);
                        break;
                    }
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
        System.out.println("evt " + evt.getPropertyName());
        switch (evt.getPropertyName()) {
            case "UP":
                this.controller.backward();
                break;
            case "DOWN":
                this.controller.forward();
                break;
        }
    }

    @Override
    public void update(BrowseEvent e) {

        if (e.getPosition() == null) {
            configTable();
            System.out.println("MEXICO3211");
            list.addAll(e.getBeanList());
            count = 0;
        } else {
            int position = e.getPosition();
            setProgress(++count % list.size());
        }
    }

    public void displayPopup(boolean flag) {
        if (flag) {
            popup.setVisible(true);
            popup.show(this.textComponent, 1, this.textComponent.getHeight());
        } else {
            popup.setVisible(false);
        }
    }

    public void startProgess() {
        JProgressBar lpb = this.panel.getProgressBar();
        lpb.setIndeterminate(true);
    }

    public void setProgress(int progress) {
        JProgressBar lpb = this.panel.getProgressBar();
        lpb.setValue(progress);
    }

    public void stopProgress() {
        this.panel.getProgressBar().setIndeterminate(false);
    }

    private void configTable() {

        JTable jTable = this.panel.getTable();

        bindingGroup.removeBinding(jTableBinding);

        list = ObservableCollections.observableList(new ArrayList());
        jTableBinding = SwingBindings.createJTableBinding(UpdateStrategy.READ_WRITE, list, jTable);

        for (BeanColumn beanColumn : net) {
            ColumnBinding columnBinding = jTableBinding.addColumnBinding(ELProperty.create("${" + beanColumn.getProperty() + "}"));
            columnBinding.setColumnName(beanColumn.getColName());
            columnBinding.setColumnClass(beanColumn.getClase());
        }

        bindingGroup.addBinding(jTableBinding);
        bindingGroup.bind();
        
        javax.swing.DefaultListSelectionModel d;

        System.out.println("jTable.getModel() " + jTable.getSelectionModel());        
        
        int tableWidth = 0;

        //TO DO esta mal remover la columna aqui se defasa net versus Column Model
        for (int i = 0; i < net.size(); i++) {
            BeanColumn beanColumn = net.get(i);
            TableColumn column = jTable.getColumnModel().getColumn(i);
            if (beanColumn.isVisible()) {
                column.setCellRenderer(beanColumn.getRenderer());
                column.setPreferredWidth(beanColumn.getWidth());
                tableWidth += beanColumn.getWidth();
            } else {
                jTable.getColumnModel().removeColumn(column);
            }
        }

        jTable.setRowHeight(64);
    }

    private class MultipleListener implements KeyListener, DocumentListener, FocusListener, PropertyChangeListener {

        @Override
        public void keyTyped(KeyEvent e) {
        }

        @Override
        public void keyPressed(KeyEvent e) {
            PickerView.this.keyPressedInTextComponent(e);
        }

        @Override
        public void keyReleased(KeyEvent e) {
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

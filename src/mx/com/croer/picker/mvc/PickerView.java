/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.com.croer.picker.mvc;

import mx.com.croer.picker.model.PickerModel;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JPopupMenu;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;

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

    public PickerView(JTextComponent textComponent, PickerController controller, PickerModel model) {
        this.textComponent = textComponent;
        this.panel = createPanel();
        this.controller = controller;
        this.model = model;

        this.model.addBrowseListener(PickerView.this);

        MultipleListener ml = new MultipleListener();
        this.textComponent.addKeyListener(ml);
        this.textComponent.addFocusListener(ml);
        this.textComponent.getDocument().addDocumentListener(ml);
        this.panel.addPropertyChangeListener(ml);

        popup = new JPopupMenu();
        popup.setFocusable(false);
        popup.add(panel);
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
                        //                    setEntityWithoutNotification();
                        e.consume();
                        break;
                    }
                    case KeyEvent.VK_ESCAPE: {
                        popup.setVisible(false);
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
                        popup.setVisible(true);
                        popup.show(this.textComponent, 1, this.textComponent.getHeight());
                        break;
                }
            }
        } else {
            if (popup.isShowing()) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_ESCAPE:
                        this.controller.cancel();
                        popup.setVisible(false);
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
                        popup.setVisible(true);
                        popup.show(this.textComponent, 1, this.textComponent.getHeight());
                        break;
                }
            }
        }
    }

    private void documentChangeInTextComponent(DocumentEvent e) {
        this.controller.fetch(this.textComponent.getText());
        popup.setVisible(true);
        popup.show(this.textComponent, 1, this.textComponent.getHeight());
    }

    private void focusLostInTextComponent(FocusEvent e) {
        this.controller.cancel();
        popup.setVisible(false);
    }

    private void browseByClick(PropertyChangeEvent evt) {
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
        System.out.println("e " + e.getType());
        switch (e.getType()) {
            case BrowseEvent.PAGE:
                break;
            default:
                throw new AssertionError();
        }
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

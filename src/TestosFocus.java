
import java.awt.EventQueue;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JComponent;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author elialva
 */
public class TestosFocus {

    private TestosMediator testosMediator;
    private final KeyboardFocusManager manager;

    public TestosFocus(TestosMediator testosMediator) {
        this.testosMediator = testosMediator;
        manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        manager.addPropertyChangeListener("focusOwner", new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getNewValue() instanceof JComponent) {
                    JComponent component = (JComponent) evt.getNewValue();
                    TestosFocus.this.testosMediator.focusGained(component);
                }

                if (evt.getOldValue() instanceof JComponent) {
                    //focusLost event
                }
            }
        });
        manager.addKeyEventDispatcher(new KeyEventDispatcher() {

            @Override
            public boolean dispatchKeyEvent(KeyEvent e) {
                if (e.getID() == KeyEvent.KEY_PRESSED && (e.getKeyCode() == KeyEvent.VK_CONTROL || e.getKeyCode() == KeyEvent.VK_TAB)) {
                    TestosFocus.this.testosMediator.toogleView((JComponent) manager.getFocusOwner(), e);
                }
                return false;
            }
        });
    }

    public void nextFocus() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                manager.focusNextComponent();
            }
        });
    }

    public void previousFocus() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                manager.focusPreviousComponent();
            }
        });
    }
}

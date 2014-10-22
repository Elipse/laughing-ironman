
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.KeyEvent;
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
public class TestosMediator {

    private final TestosFocus testosFocus;
    private final TestosDialog testosDialog;
    private final TestosPoper testosPoper;
    private JComponent oldComponent;

    private TestosMediator() {
        this.testosFocus = new TestosFocus(this);
        this.testosDialog = new TestosDialog(this);
        this.testosPoper = new TestosPoper(this);
    }

    public static void install() {
        TestosMediator testosMediator = new TestosMediator();
    }

    public void focusGained(JComponent jComponent) {
        if (jComponent.getTopLevelAncestor() == testosDialog || oldComponent == jComponent) {
            return;
        }

        oldComponent = jComponent;

        if (jComponent.getName().equals("popero")) {
            testosPoper.show(jComponent, 1, 1 + jComponent.getHeight());
            return;
        }

        Point location = jComponent.getLocationOnScreen();
        Dimension size = jComponent.getSize();
        Point xy = new Point(location.x, location.y + size.height);

        testosDialog.setLocation(xy);
        testosDialog.requestSpecificFocus();
        testosDialog.pack();
        testosDialog.setVisible(true);
    }

    public void toogleView(JComponent jComponent, KeyEvent e) {

        switch (e.getKeyCode()) {
            case KeyEvent.VK_CONTROL:
                if (jComponent.getTopLevelAncestor() == testosDialog) {
                    testosDialog.setVisible(false);
                } else {
                    testosDialog.setVisible(true);
                }
                break;
            case KeyEvent.VK_TAB:
                if (jComponent.getTopLevelAncestor() == testosDialog) {
                    testosDialog.setVisible(false);
                    if (KeyEvent.getKeyModifiersText(e.getModifiers()).equals("Shift")) {
                        testosFocus.previousFocus();
                    } else {
                        testosFocus.nextFocus();
                    }
                }
                break;
            default:
        }
    }
}

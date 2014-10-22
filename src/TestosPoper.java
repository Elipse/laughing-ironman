
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JLabel;
import javax.swing.JPopupMenu;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author elialva
 */
public class TestosPoper extends JPopupMenu {

    private final TestosMediator testosMediator;

    public TestosPoper(TestosMediator testosMediator) {
        this.testosMediator = testosMediator;
        add(new JLabel("eam"));
        setFocusable(false);
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.awt.AWTException;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.FocusManager;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

/**
 *
 * @author elialva
 */
public class FocusRules extends javax.swing.JFrame {

    /**
     * Creates new form NewJFrame1
     */
    public FocusRules() {
        initComponents();
        InputMap inputMap = jTextField1.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        InputMap inputMap2 = jTextField2.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);

        if (inputMap == inputMap2) {
            System.out.println("iguanas");
        } else {
            System.out.println("Dofer");
        }

        InputMap ipan = jPanel1.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        InputMap it1 = jTextField1.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        if (ipan == it1) {
            System.out.println("gonoas " + ipan + " it1 " + it1);
        } else {
            System.out.println("difer " + ipan + " it1 " + it1);
        }

        //Hijack the keyboard manager
        KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        manager.addKeyEventDispatcher(new KeyEventDispatcher() {

            @Override
            public boolean dispatchKeyEvent(KeyEvent e) {
                System.out.println("eF5 " + e);
                if (e.getID() == KeyEvent.KEY_RELEASED) {
                    if (e.getModifiersEx() == 0x80 && e.getKeyCode() == KeyEvent.VK_F5) {
                        System.out.println("eF5 " + e);
//                        System.out.println("e. " + e.getExtendedKeyCode());
                        Component focusOwner = FocusManager.getCurrentManager().getFocusOwner();
                        JOptionPane.showConfirmDialog(focusOwner, "Moj");
////                    System.out.println("typed" + focusOwner); //Allow the event to be redispatched
                    }
                }
                return false;
            }
        });

        manager.addPropertyChangeListener(
                new PropertyChangeListener() {
                    JComponent jc = new JCheckBox();
                    final JDialog d = new JDialog((Frame) null);

                    @Override
                    public void propertyChange(PropertyChangeEvent e) {
                        String prop = e.getPropertyName();

                        d.getRootPane().registerKeyboardAction(new ActionListener() {

                            @Override
                            public void actionPerformed(ActionEvent e) {
                                System.out.println("esti " + e.getSource());
                                d.setVisible(false);
                            }
                        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
                        d.setUndecorated(true);
//                        d.add(new JTextField("chetos"));
                        d.add(new JTextField("chetos2"));

                        d.setMinimumSize(new Dimension(200, 200));
                        if (("focusOwner".equals(prop)) && ((e.getNewValue()) instanceof JComponent)) {

                            final JComponent comp = (JComponent) (Component) e.getNewValue();
                            Component padre = comp;
                            while (padre != null) {
                                padre = padre.getParent();
                                if (padre == d) {
                                    System.out.println("En el Seer");
                                    return;
                                }
                            }

//                            comp.dispatchEvent(new KeyEvent(comp, KeyEvent.KEY_TYPED, System.currentTimeMillis(), 0, 0, 'A'));
//                            comp.dispatchEvent(new KeyEvent(comp, KeyEvent.KEY_RELEASED, System.currentTimeMillis(), 0, 65, 'A'));
                            System.out.println("comp " + comp.hashCode() + "***" + jc.hashCode());
                            if (comp == jc) {
                                System.out.println("iguanas");
                            } else {
                                jc = comp;
                                d.setVisible(true);
                            }
                        }
                    }
                }
        );

        jPanel1.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("control F1"), "global");
        jPanel1.getActionMap().put("global", new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                Component focusOwner = FocusManager.getCurrentManager().getFocusOwner();
                System.out.println("cOn F1 " + focusOwner);
                JOptionPane.showConfirmDialog(focusOwner, "Teos");
            }
        });

        //Instalar una action global que viene en el jar y pasarle un Listener        Seer.install(SeerListener, XMLFile);
        //La acción global maneja el foco, el ctrl-space y el control declarativo
        // envía a los Listener's el foco y la entidad (unmanaged)
        jTextField1.getInputMap().put(KeyStroke.getKeyStroke("control SPACE"), "doSomething");
        jTextField1.getActionMap().put("doSomething", new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                Component focusOwner = FocusManager.getCurrentManager().getFocusOwner();
                System.out.println("MeViys1 " + focusOwner);
            }
        });
        jTextField2.getInputMap().put(KeyStroke.getKeyStroke("control SPACE"), "doSomething");
        jTextField2.getActionMap().put("doSomething", new AbstractAction() {

            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("MeViys2 " + e.getSource());
            }
        });

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jComboBox1 = new javax.swing.JComboBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jTextField1.setText("jTextField1");

        jTextField2.setText("jTextField1");

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(331, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(203, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(FocusRules.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FocusRules.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FocusRules.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FocusRules.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FocusRules().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    // End of variables declaration//GEN-END:variables
}

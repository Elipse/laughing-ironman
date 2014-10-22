
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JTextField;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author elialva
 */
public class TestosText extends JTextField {

    String text = "byDex";
    private Graphics gd;

    public TestosText() {

        addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
                text = "";
//                paintComponent(gd);
            }

            @Override
            public void keyPressed(KeyEvent e) {
                text = "";
//                paintComponent(gd);
            }

            @Override
            public void keyReleased(KeyEvent e) {
                text = "";
//                paintComponent(gd);
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        gd = g;
        super.paintComponent(g); //To change body of generated methods, choose Tools | Templates.
        g.setColor(Color.GRAY);
        g.drawString(text, 8, 20);
    }

}

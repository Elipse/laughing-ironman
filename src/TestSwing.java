

import java.awt.event.*;

import javax.swing.*;

public class TestSwing {

    public static void main(String[] args) throws Exception {
        JFrame frame = new JFrame("Parent");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setVisible(true);

        final JDialog dialog = new JDialog(frame, "Child", true);
        dialog.setSize(300, 200);
        dialog.setLocationRelativeTo(frame);
        JButton button = new JButton("Button");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });
        dialog.add(button);
        dialog.setUndecorated(true);
        dialog.setVisible(true);
    }
}

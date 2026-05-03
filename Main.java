import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import componets.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("My First Window");

            TurnButton turnbutton = new TurnButton();
            frame.add(turnbutton);
            
            frame.setSize(1000, 600);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        });
    }
}
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.BoxLayout;
import javax.swing.SwingUtilities;

import componets.*;
import game.Loop;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("My First Window");

            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

            Loop gameLoop = new Loop();
            gameLoop.initGame(4);  // Initialize game with 4 players
            TurnButton turnbutton = new TurnButton(gameLoop);
            panel.add(turnbutton);
            
            frame.add(panel);
            frame.setSize(1000, 600);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        });
    }
}
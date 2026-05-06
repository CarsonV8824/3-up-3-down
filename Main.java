import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.BoxLayout;
import javax.swing.SwingUtilities;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import java.awt.Component;

import componets.*;
import game.Loop;
import game.Player;
import game.Card;

import java.util.ArrayList;

public class Main {
    private static int currentPlayerSetupIndex = 0;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("3 Up 3 Down - Card Setup");

            Loop gameLoop = new Loop();
            gameLoop.initGame(4); // Initialize game with 4 players

            if (gameLoop.isInitCardsDown) {
                showCardSetup(frame, gameLoop);
            } else {
                showGamePlay(frame, gameLoop);
            }

            frame.setSize(1000, 600);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        });
    }

    private static void showCardSetup(JFrame frame, Loop gameLoop) {
        currentPlayerSetupIndex = 0;
        showPlayerCardSetup(frame, gameLoop);
    }

    private static void showPlayerCardSetup(JFrame frame, Loop gameLoop) {
        // Clear the frame
        frame.getContentPane().removeAll();

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        Player currentPlayer = gameLoop.playerList.get(currentPlayerSetupIndex);
        ArrayList<Card> handCards = currentPlayer.getCards();

        // Title
        JLabel titleLabel = new JLabel("Player " + (currentPlayerSetupIndex + 1) + " - Select 3 Cards for Top and 3 for Bottom");
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(titleLabel);

        panel.add(new JLabel(""));

        // Top Cards Section
        JLabel topCardLabel = new JLabel("Select 3 Cards for TOP:");
        topCardLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(topCardLabel);

        ArrayList<JCheckBox> topCheckboxes = new ArrayList<>();
        for (int i = 0; i < handCards.size(); i++) {
            Card card = handCards.get(i);
            JCheckBox checkbox = new JCheckBox(card.getColor() + " - " + card.getSymbol());
            checkbox.setAlignmentX(Component.LEFT_ALIGNMENT);
            topCheckboxes.add(checkbox);
            panel.add(checkbox);
        }

        panel.add(new JLabel(""));

        // Bottom Cards Section
        JLabel bottomCardLabel = new JLabel("Select 3 Cards for BOTTOM:");
        bottomCardLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(bottomCardLabel);

        ArrayList<JCheckBox> bottomCheckboxes = new ArrayList<>();
        for (int i = 0; i < handCards.size(); i++) {
            Card card = handCards.get(i);
            JCheckBox checkbox = new JCheckBox(card.getColor() + " - " + card.getSymbol());
            checkbox.setAlignmentX(Component.LEFT_ALIGNMENT);
            bottomCheckboxes.add(checkbox);
            panel.add(checkbox);
        }

        panel.add(new JLabel(""));

        // Error label
        JLabel errorLabel = new JLabel("");
        errorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(errorLabel);

        // Submit Button
        JButton submitButton = new JButton("Confirm Selection");
        submitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        submitButton.addActionListener(e -> {
            // Collect selected indices for top cards
            ArrayList<Integer> topIndices = new ArrayList<>();
            for (int i = 0; i < topCheckboxes.size(); i++) {
                if (topCheckboxes.get(i).isSelected()) {
                    topIndices.add(i);
                }
            }

            // Collect selected indices for bottom cards
            ArrayList<Integer> bottomIndices = new ArrayList<>();
            for (int i = 0; i < bottomCheckboxes.size(); i++) {
                if (bottomCheckboxes.get(i).isSelected()) {
                    bottomIndices.add(i);
                }
            }

            // Validate selections
            if (topIndices.size() != 3) {
                errorLabel.setText("Please select exactly 3 cards for TOP");
                frame.getContentPane().revalidate();
                frame.getContentPane().repaint();
                return;
            }

            if (bottomIndices.size() != 3) {
                errorLabel.setText("Please select exactly 3 cards for BOTTOM");
                frame.getContentPane().revalidate();
                frame.getContentPane().repaint();
                return;
            }

            // Check for overlap
            for (int topIdx : topIndices) {
                if (bottomIndices.contains(topIdx)) {
                    errorLabel.setText("Same card cannot be in both TOP and BOTTOM");
                    frame.getContentPane().revalidate();
                    frame.getContentPane().repaint();
                    return;
                }
            }

            // Add selected cards to player's topCards and bottomCards
            for (int idx : topIndices) {
                currentPlayer.addTopCard(handCards.get(idx));
            }
            for (int idx : bottomIndices) {
                currentPlayer.addBottomCard(handCards.get(idx));
            }

            // Move to next player or finish setup
            currentPlayerSetupIndex++;
            if (currentPlayerSetupIndex < gameLoop.playerList.size()) {
                showPlayerCardSetup(frame, gameLoop);
            } else {
                // Setup complete
                gameLoop.isInitCardsDown = false;
                showGamePlay(frame, gameLoop);
            }
        });
        panel.add(submitButton);

        frame.add(panel);
        frame.getContentPane().revalidate();
        frame.getContentPane().repaint();
    }

    private static void showGamePlay(JFrame frame, Loop gameLoop) {
        frame.getContentPane().removeAll();

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        PlayerText playerDisplay = new PlayerText();
        playerDisplay.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(playerDisplay);

        DrawButton drawButton = new DrawButton(gameLoop, playerDisplay);
        drawButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(drawButton);

        frame.add(panel);
        frame.getContentPane().revalidate();
        frame.getContentPane().repaint();
    }
}
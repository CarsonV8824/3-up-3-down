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
import java.lang.Exception;

public class Main {
    private static int currentPlayerSetupIndex = 0;
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("3 Up 3 Down - Card Setup");

            Loop gameLoop = new Loop();
            gameLoop.initGame(2); // Initialize game with 4 players

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

            // Validate selections
            if (topIndices.size() != 3) {
                errorLabel.setText("Please select exactly 3 cards for TOP");
                frame.getContentPane().revalidate();
                frame.getContentPane().repaint();
                return;
            }

            // Add selected cards to player's topCards 
            for (int idx : topIndices) {
                currentPlayer.addTopCard(handCards.get(idx));
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
        playerDisplay.updatePlayerDisplay(gameLoop.playerList.get(gameLoop.currentIndex).getName());
        playerDisplay.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(playerDisplay);

        JLabel deckLabel = new JLabel("Cards in pile:");
        deckLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(deckLabel);

        DrawButton drawButton = new DrawButton(gameLoop, playerDisplay);
        drawButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(drawButton);

        PlaceButton placeButton = new PlaceButton();
        placeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(placeButton);

        

        drawButton.addActionListener(e -> {
            gameLoop.nextTurn();
            playerDisplay.updatePlayerDisplay(gameLoop.playerList.get(gameLoop.currentIndex).getName());
            showGamePlay(frame, gameLoop);
        });

        placeButton.addActionListener(e -> {
            try {
                Card highestCard = gameLoop.playedCards.get(-1);
                Card selectedCard;
                Integer highestNum = Integer.parseInt(highestCard.getSymbol());
            } catch (Exception exc) {
                return;
            }
            
            gameLoop.nextTurn();
            playerDisplay.updatePlayerDisplay(gameLoop.playerList.get(gameLoop.currentIndex).getName());
            showGamePlay(frame, gameLoop);
        });

        frame.add(panel);
        frame.getContentPane().revalidate();
        frame.getContentPane().repaint();
    }
}
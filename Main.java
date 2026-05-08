import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.BoxLayout;
import javax.swing.SwingUtilities;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import java.awt.Component;

import componets.*;
import game.Loop;
import game.Player;
import game.Card;

import java.util.ArrayList;
import java.util.Random;

public class Main {
    private static int currentPlayerSetupIndex = 0;
    public static final String[] symbols = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "Clear", "Clear +1", "Clear +2"};
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

            // Add selected cards to player's topCards and remove from hand
            for (int idx : topIndices) {
                currentPlayer.addTopCard(handCards.get(idx));
            }
            
            // Remove in reverse order to avoid index shifting
            for (int i = topIndices.size() - 1; i >= 0; i--) {
                handCards.remove((int) topIndices.get(i));
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

    private static void playAdditionalCards(Player player, Loop gameLoop, int numCards, JFrame frame) {
        for (int i = 0; i < numCards; i++) {
            ArrayList<Card> playerCards = player.getCards();
            ArrayList<String> items = new ArrayList<>();
            
            for (Card card : playerCards) {
                items.add(card.getColor() + " " + card.getSymbol());
            }
            
            if (items.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "No cards left to play!", "No Cards", JOptionPane.INFORMATION_MESSAGE);
                break;
            }
            
            String selectedCard = (String) JOptionPane.showInputDialog(
                frame,
                "Play additional card " + (i + 1) + " of " + numCards + ":",
                "Additional Card",
                JOptionPane.QUESTION_MESSAGE,
                null,
                items.toArray(new String[0]),
                items.get(0)
            );
            
            if (selectedCard == null) {
                break; // User cancelled
            }
            
            String[] colorAndSymbol = selectedCard.split(" ", 2);
            int currentCardSymbolIndex = gameLoop.getSymbolIndex(colorAndSymbol[1]);
            
            // Check if card is higher than the last played card
            if (gameLoop.playedCards.size() > 0) {
                Card lastCard = gameLoop.playedCards.get(gameLoop.playedCards.size() - 1);
                int lastCardSymbolIndex = gameLoop.getSymbolIndex(lastCard.getSymbol());
                
                if (currentCardSymbolIndex <= lastCardSymbolIndex) {
                    JOptionPane.showMessageDialog(frame, "Your card must be higher than the last played card!", "Invalid Card", JOptionPane.WARNING_MESSAGE);
                    i--; // Retry this card
                    continue;
                }
            }
            
            player.removeCardFromHand(colorAndSymbol[0], colorAndSymbol[1]);
            gameLoop.playedCards.add(new Card(colorAndSymbol[0], colorAndSymbol[1]));
        }
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

        for (Card card : gameLoop.playedCards) {
            String symbolText = card.getSymbol();
            JLabel symText = new JLabel(symbolText);
            symText.setAlignmentX(Component.CENTER_ALIGNMENT);
            panel.add(symText);
        }

        ArrayList<String> items = new ArrayList<>();
        Player currentPlayer = gameLoop.getPlayer();
        
        ArrayList<Card> playerCards = currentPlayer.getCards();
        for (Card card : playerCards) { 
            items.add(card.getColor() + " " + card.getSymbol());
        }
        
        // Check if player has cards to play
        if (items.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Player has no cards! Skipping turn.", "No Cards", JOptionPane.INFORMATION_MESSAGE);
            gameLoop.nextTurn();
            playerDisplay.updatePlayerDisplay(gameLoop.playerList.get(gameLoop.currentIndex).getName());
            showGamePlay(frame, gameLoop);
            return;
        }
        
        JComboBox<String> selectedCardToPlay = new JComboBox<String>(items.toArray(new String[0]));
        selectedCardToPlay.setMaximumSize(selectedCardToPlay.getPreferredSize());
        selectedCardToPlay.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(selectedCardToPlay);

        PlaceButton placeButton = new PlaceButton();
        placeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(placeButton);

        placeButton.addActionListener(e -> {

            String selectedCard = (String) selectedCardToPlay.getSelectedItem();
            
            // Safety check
            if (selectedCard == null || selectedCard.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please select a card to play!", "No Card Selected", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            String[] colorAndSymbol = selectedCard.split(" ", 2);

            String symbol = colorAndSymbol[1];




            if (currentPlayer.getHighestSymbol() < gameLoop.getHighestSymbolforPlaced()) {
                ArrayList<Card> cards = gameLoop.clearAndGetAllPlayedCards();
                for (Card card : cards) {
                    currentPlayer.addHandCard(card);
                }
                gameLoop.nextTurn();
                playerDisplay.updatePlayerDisplay(gameLoop.playerList.get(gameLoop.currentIndex).getName());
                showGamePlay(frame, gameLoop);
                return;
                
            }
            
            // Check if card symbol is higher than the last played card
            if (gameLoop.playedCards.size() > 0) {
                Card lastCard = gameLoop.playedCards.get(gameLoop.playedCards.size() - 1);
                int currentCardSymbolIndex = gameLoop.getSymbolIndex(colorAndSymbol[1]);
                int lastCardSymbolIndex = gameLoop.getSymbolIndex(lastCard.getSymbol());
                
                // Handle case where symbol is not found
                if (currentCardSymbolIndex == -1 || lastCardSymbolIndex == -1) {
                    JOptionPane.showMessageDialog(frame, "Error: Invalid card symbol!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                if (currentCardSymbolIndex <= lastCardSymbolIndex) {
                    JOptionPane.showMessageDialog(frame, "Your card must be higher than the last played card!", "Invalid Card", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            }

            switch (symbol) {
                case "Clear":
                    gameLoop.clearAndGetAllPlayedCards();
                    break;
                case "Clear +1":
                    gameLoop.clearAndGetAllPlayedCards();
                    // Allow playing 1 additional card
                    playAdditionalCards(currentPlayer, gameLoop, 1, frame);
                    break;
                case "Clear +2":
                    gameLoop.clearAndGetAllPlayedCards();
                    // Allow playing 2 additional cards
                    playAdditionalCards(currentPlayer, gameLoop, 2, frame);
                    break;
            }
            
            if (gameLoop.initCards.size() > 0) {
                Random rand = new Random();
                int index = rand.nextInt(gameLoop.initCards.size());
                Card choosenCard = gameLoop.initCards.remove(index);
                currentPlayer.addHandCard(choosenCard);
            } else { 

            }

            
            currentPlayer.removeCardFromHand(colorAndSymbol[0], colorAndSymbol[1]);
            gameLoop.playedCards.add(new Card(colorAndSymbol[0], colorAndSymbol[1]));

            gameLoop.nextTurn();
            playerDisplay.updatePlayerDisplay(gameLoop.playerList.get(gameLoop.currentIndex).getName());
            showGamePlay(frame, gameLoop);
        });

        frame.add(panel);
        frame.getContentPane().revalidate();
        frame.getContentPane().repaint();
    }

}
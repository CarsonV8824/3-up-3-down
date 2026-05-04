package componets;

import javax.swing.JComboBox;

import java.util.ArrayList;

import game.Card;

public class PlayerDeck extends JComboBox<String>{
    private ArrayList<Card> currentPlayerHand;
    
    public PlayerDeck() {
        super();
    }

}

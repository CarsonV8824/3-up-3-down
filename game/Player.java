package game;
import java.util.ArrayList;


public class Player {
    private ArrayList<Card> handCards;
    private ArrayList<Card> topCards;
    private ArrayList<Card> bottomCards;

    public Player() {
        this.handCards = new ArrayList<Card>();
        this.topCards = new ArrayList<Card>();
        this.bottomCards = new ArrayList<Card>();
    }

    public void addStartCards(Card card) {
        this.handCards.add(card);
    }

}

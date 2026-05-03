package game;
import java.util.ArrayList;


public class Player {
    private String name;
    private ArrayList<Card> handCards;
    private ArrayList<Card> topCards;
    private ArrayList<Card> bottomCards;

    public Player(String n) {
        this.name = n;
        this.handCards = new ArrayList<Card>();
        this.topCards = new ArrayList<Card>();
        this.bottomCards = new ArrayList<Card>();
    }

    public void addStartCard(Card card) {
        this.handCards.add(card);
    }

    public ArrayList<Card> getCards() {
        return this.handCards;
    }

    public String getName() {
        return this.name;
    }

}

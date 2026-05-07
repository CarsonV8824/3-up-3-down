package game;
import java.util.ArrayList;


public class Player {
    public final String[] symbols = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "Clear", "Clear +1", "Clear +2"};
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

    public void addTopCard(Card c) {
        this.topCards.add(c);
    }

    public void addBottomCard(Card c) {
        this.bottomCards.add(c);
    }

    public void addHandCard(Card c) {
        this.handCards.add(c);
    }

    public Card removeCardFromHand(String color, String symbol) {
        for (Card card : this.handCards) {
            if (card.getSymbol().equals(symbol) && card.getColor().equals(color)) {
                int index = this.handCards.lastIndexOf(card);
                Card choosenCard = this.handCards.get(index);
                this.handCards.remove(index);
                return choosenCard;
                
            }
        }
        return null;
    }

    public int getSymbolIndex(String symbol) {
        for (int i = 0; i < symbols.length; i++) {
            if (symbols[i].equals(symbol)) {
                return i;
            }
        }
    return -1;  // Return -1 if not found
}

    public String getHighestSymbol() {
        int highestIndex = 0;
        int tempIndex;
        for (Card card : this.handCards) {
            tempIndex = getSymbolIndex(card.getSymbol());
            if (tempIndex > highestIndex) {
                highestIndex = tempIndex;
            }
            
        }
        return symbols[highestIndex];
    }

}

package game;

import java.util.ArrayList;
import java.util.Random;

public class Loop {
    public ArrayList<Player> playerList;
    public ArrayList<Card> initCards;
    public Integer currentIndex;
    
    public Loop() {
        this.playerList = new ArrayList<Player>();
        this.initCards = new ArrayList<Card>();
        this.currentIndex = 0;
        this.initializeCards();
    }

    private void initializeCards() {
        String[] colors = {"Green", "Blue", "Red"};
        
        // Add number cards (1-10): 2 copies per color per number
        for (String color : colors) {
            for (int number = 1; number <= 10; number++) {
                this.initCards.add(new Card(color, String.valueOf(number)));
                this.initCards.add(new Card(color, String.valueOf(number)));
            }
        }
        
        // Add Clear cards: 2 copies per color
        for (String color : colors) {
            this.initCards.add(new Card(color, "Clear"));
            this.initCards.add(new Card(color, "Clear"));
        }
        
        // Add Clear +1 cards: 2 copies per color
        for (String color : colors) {
            this.initCards.add(new Card(color, "Clear +1"));
            this.initCards.add(new Card(color, "Clear +1"));
        }
        
        // Add Clear +2 cards: 2 copies (Green only)
        this.initCards.add(new Card("Green", "Clear +2"));
        this.initCards.add(new Card("Green", "Clear +2"));
    }

    public void initGame(int playerCount) {
        for (int i = 0; i < playerCount; i++) {
            String playerNum = String.valueOf(i + 1);

            this.playerList.add(new Player("Player " + playerNum));
        }
        for (int i=0; i< this.playerList.size();i++) {
            Random rand = new Random();
            int index = rand.nextInt(this.initCards.size());
            Card tempCard = this.initCards.remove(index);
            Player p = this.playerList.get(i);
            p.addStartCard(tempCard);
        }
    }

    public void nextTurn(){
        if (this.currentIndex >= this.playerList.size() - 1){
            this.currentIndex = 0;
        }
        else {
            this.currentIndex += 1;
        }
        System.out.println(this.currentIndex);
    }

    public int getIndex(){
        return this.currentIndex;
    }
}

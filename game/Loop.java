package game;

import java.util.ArrayList;

public class Loop {
    public ArrayList<Player> playerList;
    public Loop() {
        this.playerList = new ArrayList<Player>();
    }

    public void initGame(int playerCount) {
        for (int i = 0; i < playerCount; i++) {
            String playerNum = String.valueOf(i + 1);

            this.playerList.add(new Player("Player " + playerNum));
        }
    }


}

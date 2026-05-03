package componets;

import javax.swing.JLabel;

public class PlayerText extends JLabel{
    public PlayerText(){
        super("Player 1");
    }
    
    public void updatePlayerDisplay(String playerName) {
        this.setText(playerName);
    }
}

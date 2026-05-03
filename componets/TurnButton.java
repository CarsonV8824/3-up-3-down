package componets;

import javax.swing.JButton;
import game.Loop;

public class TurnButton extends JButton{
    public TurnButton(Loop loop) {
        super("Next Turn");
        this.addActionListener(e -> loop.nextTurn());
    }
}

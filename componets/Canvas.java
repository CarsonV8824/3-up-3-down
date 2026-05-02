package componets;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

public class Canvas extends JPanel{
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLUE);

        int panelWidth = getWidth();
        
        int rectWidth = panelWidth / 2;  // rectangle width is half the panel width
        
        int x = (panelWidth - rectWidth) / 2;  // center horizontally
        int y = 0;  // top of panel
        g.fillRect(x, y, 700, 700);
    }
}
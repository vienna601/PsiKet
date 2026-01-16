package view;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JPanel;

import model.Gate;

public class CircuitPanel extends JPanel{
	public CircuitPanel() {
	}
	//customize the paint component method
	//soruce: https://stackoverflow.com/questions/
	//34596194/how-to-draw-a-line-on-a-existing-jpanel-in-swing-java
	@Override
    public void paintComponent(Graphics g) {
		//call parent method
        super.paintComponent(g);
        //draw a horizontal line using coordinates of endpoints
        //use default color black
        g.drawLine(150, 150, 700, 150);
        //set formatting
        g.setFont(new Font("Times New Roman", Font.ITALIC, 50));
        //label the line with qubit number
        //source: https://stackoverflow.com/questions/1830603/
        //drawing-text-within-a-jpanel
        g.drawString("q0",80,170);
        
        //change color from default black to gray
        g.setColor(Color.gray);
        //label the line
        g.drawString("c:", 80,270);
        //draw parallel lines representing the 
        //classical measurement line (displays measurement results
        //which are classical bits)
        g.drawLine(150, 260, 700, 260);
        g.drawLine(150, 280, 700, 280);
    }
}

package view;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;

import controller.AssetsController;

public class CircuitImportFrame extends JFrame implements MouseListener{
	//declare components
	//use an ordered map to load circuit information from file
	private TreeMap<String,String> circuitInfo;
	private ArrayList<JLabel> infoLabels;
	private JLabel homeButton;
	private JLabel backButton;
	private JLabel bgLabel;
	private JLabel titleLabel;
	private JPanel infoPanel;
	private JScrollPane scroll;
	
	//constructor
	public CircuitImportFrame() {
		//create background label
		bgLabel = new JLabel(new ImageIcon("src/main/assets/images/importBg.jpg"));
		//create button labels
		homeButton = new JLabel(new ImageIcon("src/main/assets/images/homeButton.png"));
		homeButton.addMouseListener(this);
		backButton = new JLabel(new ImageIcon("src/main/assets/images/backButton.png"));
		backButton.addMouseListener(this);
		//create the title label
		titleLabel = new JLabel("Saved Circuits");
		titleLabel.setFont(new Font("Times New Roman",Font.BOLD, 40));
		titleLabel.setBackground(Color.white);
		titleLabel.setOpaque(true);
		
		//initialize panel for holding circuit info
		infoPanel = new JPanel();
		infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
		//initialize the map using the method in the assets controller
		circuitInfo = AssetsController.loadCircuits();
		//loop through the map and create JLabels
		//Source: https://stackoverflow.com/questions/1318980/how-to-iterate-over-a-treemap
		infoLabels = new ArrayList<JLabel>();
		for(Entry<String, String> entry : circuitInfo.entrySet()) {
			//add buffer space
			infoPanel.add(Box.createRigidArea(new Dimension(0,30)));
			//set label text to contain key and value of map entry
			JLabel label = new JLabel("<html>Circuit name: "+entry.getKey()+
					"<br>Click to import!</html>");
			label.setFont(new Font("Times New Roman",Font.PLAIN, 20));
			label.setBackground(Color.white);
			label.setOpaque(true);
			label.addMouseListener(this);
			//change to a hand cursor when the user hovers over the label
	        //source: https://stackoverflow.com/questions/7359189/how-to-
			//change-the-mouse-cursor-in-java
			label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			infoLabels.add(label);
			//add to panel
			label.setAlignmentX(CENTER_ALIGNMENT);
            infoPanel.add(label);
		}
		
		//add scroll bar
		scroll = new JScrollPane(infoPanel, 
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		//increase scroll speed
		scroll.getVerticalScrollBar().setUnitIncrement(15);
		scroll.setOpaque(false);
		infoPanel.setBackground(new Color(0,0,0,0));
		
		//set bounds
		titleLabel.setBounds(700,100,250,80);
		homeButton.setBounds(30,30,150,150);
		backButton.setBounds(200,30,150,150);
		scroll.setBounds(300,250,1000,650);
		//add to background label
		bgLabel.add(titleLabel);
		bgLabel.add(homeButton);
		bgLabel.add(backButton);
		bgLabel.add(scroll);
		//add background to frame
		add(bgLabel);
				
		//set the frame's cursor to the custom cursor
		setCursor(AssetsController.getCursor());
		//set the properties of the frame
        setTitle("Circuit Imports");
        setSize(1600, 1000);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        pack();
        setVisible(true);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		//check if the home button was clicked
		if((JLabel)e.getSource() == homeButton) {
			//dispose the current frame and show the home frame
			new HomeFrame();
			this.dispose();
		}
		//check if the back button was clicked
		else if((JLabel)e.getSource() == backButton) {
			//empty circuit
			new CircuitFrame();
			this.dispose();
		}
		//check if an info label was clicked
		else {
			for(int i = 0; i < infoLabels.size(); i++) 
				//check if label was clicked
				if((JLabel)e.getSource() == infoLabels.get(i)) {
					//get list of keys to get key value pair using index
					//Source: https://www.tutorialspoint.com/how-to-get-
					//treemap-key-or-value-using-index-in-java
					ArrayList<String>keyList = new ArrayList(circuitInfo.keySet());
					String key = keyList.get(i);
					//pass the key and value to the circuit frame constructor and
					//dispose the current frame
					new CircuitFrame(key,circuitInfo.get(key));
					this.dispose();
					//show success message
					JOptionPane.showMessageDialog(this, 
							"Circuit Imported!",
							"Import success", JOptionPane.INFORMATION_MESSAGE);
				}
		}//end of else
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
	}
}

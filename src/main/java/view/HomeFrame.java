package view;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import controller.AssetsController;

public class HomeFrame extends JFrame implements MouseListener{
	//declare components
	private JLabel bgLabel;
	private JLabel titleButton;
	//create labels containing navigation headings
	private String[] headings = {
			"|00⟩ Bloch Sphere", "|01⟩ Circuits",
			"|10⟩ Algorithms", "|11⟩ Resources"};
	private JPanel mainPanel;
	//navigation panels
	private JPanel[] navPanels;
	
	public HomeFrame() {
		//create background label
		bgLabel = new JLabel(new ImageIcon("src/main/assets/images/homeBg.jpg"));
		//create button that lets the user go back to the title frame
		titleButton = new JLabel(new ImageIcon("src/main/assets/images/titleButton.png"));
		//add tooltip
		titleButton.setToolTipText("Back to Title Frame");
		//add mouse listener
		titleButton.addMouseListener(this);
		//change the cursor to a hand cursor when the user hovers over the label
        //source: https://stackoverflow.com/questions/7359189/how-to-change-the-mouse-cursor-in-java
        titleButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        //create panel for storing navigation labels and buttons
        mainPanel = new JPanel(new GridLayout(2,2,200,30));
        mainPanel.setOpaque(false);
        
        //create navigation panels
        navPanels = new JPanel[headings.length];
        for(int i = 0; i < headings.length; i++) {
        	//create heading label
        	JLabel headingLabel = new JLabel(headings[i]);
        	//set the format
        	//alternate between red and blue headings
        	if(i%2 == 0)
        		//blue text
        		headingLabel.setForeground(new Color(0x6699cd));
        	else
        		//red text
        		headingLabel.setForeground(new Color(0xbb4c4c));
        	headingLabel.setFont(new Font("Times New Roman", Font.BOLD, 30));
        	headingLabel.setBackground(Color.black);
        	headingLabel.setOpaque(true);
        	
        	//create image label
        	//use index 5 since all heading strings have 5 characters in front
        	//that will not be in the image file path
        	JLabel imageLabel = new JLabel(new ImageIcon("src/main/assets/images/"+
        				headings[i].substring(5)+".png"));
        	
        	//create panel and set layout
        	navPanels[i] = new JPanel();
        	navPanels[i].setLayout(new BoxLayout(navPanels[i], BoxLayout.Y_AXIS));
        	//add tool tip and set cursor icon
        	navPanels[i].setToolTipText("Go to "+headings[i]+" frame");
        	navPanels[i].setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        	//set opacity
        	navPanels[i].setOpaque(false);
        	
        	//set component alignment
        	headingLabel.setAlignmentX(CENTER_ALIGNMENT);
        	imageLabel.setAlignmentX(CENTER_ALIGNMENT);
        	//add components
        	navPanels[i].add(headingLabel);
        	navPanels[i].add(imageLabel);
        	//add mouse listener
        	navPanels[i].addMouseListener(this);
        	//add navigation panel to the main panel
        	mainPanel.add(navPanels[i]);
        }
        
        //set bounds of main panel
        mainPanel.setBounds(100,100,1400,900);
        bgLabel.add(mainPanel);
        //set bounds of title button label
        titleButton.setBounds(630,400,300,100);
        bgLabel.add(titleButton);
        add(bgLabel);
        
        //set the home frame's cursor to the custom cursor
      	setCursor(AssetsController.getCursor());
        
		//set the properties of the frame
        setTitle("Home");
        setSize(1600, 1000);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        pack();
        setVisible(true);
	}


	@Override
	public void mouseClicked(MouseEvent e) {
		//check which frame the user wants to navigate to
		//if the user clicked the title button
		if(e.getSource() instanceof JLabel && (JLabel)e.getSource() == titleButton) {
			//open the title frame and dispose the current frame
			new TitleFrame();
			this.dispose();
		}
		//if the user clicked the first panel
		else if((JPanel)e.getSource() == navPanels[0]) {
			//open the bloch frame and dispose the current frame
			new BlochFrame();
			this.dispose();
		}
		//if the user clicked the second panel
		else if((JPanel)e.getSource() == navPanels[1]) {
			//open the circuit frame and dispose the current frame
			new CircuitFrame();
			this.dispose();
		}
		//if the user clicked the third panel
		else if((JPanel)e.getSource() == navPanels[2]) {
			//open the algorithm frame and dispose the current frame
			new AlgorithmFrame();
			this.dispose();
		}
		//if the user clicked the last panel
		else if((JPanel)e.getSource() == navPanels[3]) {
			//open the resources frame and dispose the current frame
			new ResourcesFrame();
			this.dispose();
		}
	}


	@Override
	public void mousePressed(MouseEvent e) {
		//unused method
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		//unused method
	}
	@Override
	public void mouseEntered(MouseEvent e) {
		//unused method
	}
	@Override
	public void mouseExited(MouseEvent e) {
		//unused method
	}
}

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

/*
 * This program is responsible for configuring the layout
 * of the home frame where the user can choose which specific
 * feature they want to access (e.g. bloch sphere, algorithms, etc.)
 */
public class HomeFrame extends JFrame{
	//declare components
	//check if already disposed
	private boolean isDisposed = false;
	private JLabel bgLabel;
	private JLabel titleButton;
	//create labels containing navigation headings
	private String[] headings = {
			"|00⟩ Bloch Sphere", "|01⟩ Circuits",
			"|10⟩ Algorithms", "|11⟩ Resources"};
	private JPanel mainPanel;
	//navigation panels
	private JPanel[] navPanels;
	
	//getters and setters for controller to access components
	public JLabel getTitleButton() {
		return titleButton;
	}

	public void setTitleButton(JLabel titleButton) {
		this.titleButton = titleButton;
	}

	public JPanel[] getNavPanels() {
		return navPanels;
	}

	public void setNavPanels(JPanel[] navPanels) {
		this.navPanels = navPanels;
	}
	public boolean isDisposed() {
		return isDisposed;
	}
	public void setDisposed(boolean isDisposed) {
		this.isDisposed = isDisposed;
	}

	//constructor
	public HomeFrame() {
		//create background label
		bgLabel = new JLabel(new ImageIcon("src/main/assets/images/homeBg.jpg"));
		//create button that lets the user go back to the title frame
		titleButton = new JLabel(new ImageIcon("src/main/assets/images/titleButton.png"));
		//add tooltip
		titleButton.setToolTipText("Back to Title Frame");
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
	
	//dispose the frame
	@Override
	public void dispose() {
		super.dispose();
		setDisposed(true);
	}
}

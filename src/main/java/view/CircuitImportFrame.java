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

/*
 * This program is responsible for configuring the layout
 * of the frame where the user can choose to import one of
 * their saved circuits.
 */

public class CircuitImportFrame extends JFrame{
	//declare components
	//check if frame has been disposed
	private boolean isDisposed = false;
	//use an ordered map to load circuit information from file
	private TreeMap<String,String> circuitInfo;
	private ArrayList<JLabel> infoLabels;
	private JLabel homeButton;
	private JLabel backButton;
	private JLabel bgLabel;
	private JLabel titleLabel;
	private JPanel infoPanel;
	private JScrollPane scroll;
	
	//getters and setters for circuit controller to access components
	public ArrayList<JLabel> getInfoLabels() {
		return infoLabels;
	}
	public void setInfoLabels(ArrayList<JLabel> infoLabels) {
		this.infoLabels = infoLabels;
	}
	public JLabel getHomeButton() {
		return homeButton;
	}
	public void setHomeButton(JLabel homeButton) {
		this.homeButton = homeButton;
	}
	public JLabel getBackButton() {
		return backButton;
	}
	public void setBackButton(JLabel backButton) {
		this.backButton = backButton;
	}
	public TreeMap<String, String> getCircuitInfo() {
		return circuitInfo;
	}
	public void setCircuitInfo(TreeMap<String, String> circuitInfo) {
		this.circuitInfo = circuitInfo;
	}
	public JPanel getInfoPanel() {
		return infoPanel;
	}
	public void setInfoPanel(JPanel infoPanel) {
		this.infoPanel = infoPanel;
	}
	public boolean isDisposed() {
		return isDisposed;
	}
	public void setDisposed(boolean isDisposed) {
		this.isDisposed = isDisposed;
	}
	
	//constructor
	public CircuitImportFrame() {
		//create background label
		bgLabel = new JLabel(new ImageIcon("src/main/assets/images/importBg.jpg"));
		//create button labels
		homeButton = new JLabel(new ImageIcon("src/main/assets/images/homeButton.png"));
		backButton = new JLabel(new ImageIcon("src/main/assets/images/backButton.png"));
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
		//the controller will loop through the circuit info tree map and create
		//JLabels for displaying information
		
		//initialize list
		infoLabels = new ArrayList<JLabel>();
		//add scroll bar
		scroll = new JScrollPane(infoPanel, 
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		//increase scroll speed
		scroll.getVerticalScrollBar().setUnitIncrement(15);
		//make the background transparent
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
	
	//dispose this frame
	@Override
	public void dispose() {
		super.dispose();
		setDisposed(true);
	}
}

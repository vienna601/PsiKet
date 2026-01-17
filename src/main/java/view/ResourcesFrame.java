package view;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import controller.AssetsController;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;

/*
 * This program is responsible for configuring the layout of the
 * frame that displays useful links for quantum learners.
 */
public class ResourcesFrame extends JFrame{
	//declare components
	//check if frame disposed
	private boolean isDisposed = false;
	private ArrayList<JLabel> resourceLabels;
	private ArrayList<String> URIList;
	private JLabel bgLabel;
	private JLabel titleLabel;
	private JPanel resourcePanel;
	private JScrollPane scroll;
	private JLabel homeButton;
	
	//getters and setters for controller
	public ArrayList<JLabel> getResourceLabels() {
		return resourceLabels;
	}

	public void setResourceLabels(ArrayList<JLabel> resourceLabels) {
		this.resourceLabels = resourceLabels;
	}

	public ArrayList<String> getURIList() {
		return URIList;
	}

	public void setURIList(ArrayList<String> uRIList) {
		URIList = uRIList;
	}

	public JLabel getHomeButton() {
		return homeButton;
	}

	public void setHomeButton(JLabel homeButton) {
		this.homeButton = homeButton;
	}
	public boolean isDisposed() {
		return isDisposed;
	}
	public void setDisposed(boolean isDisposed) {
		this.isDisposed = isDisposed;
	}

	//constructor
	public ResourcesFrame() {
		//create background and title label
		bgLabel = new JLabel(new ImageIcon("src/main/assets/images/resourcesBg.png"));
		titleLabel = new JLabel("Resources");
		titleLabel.setFont(new Font("Times New Roman",Font.BOLD, 40));
		titleLabel.setBackground(Color.white);
		titleLabel.setOpaque(true);
		//create home button label
		homeButton = new JLabel(new ImageIcon("src/main/assets/images/homeButton.png"));
		//change to a hand cursor when the user hovers over the label
        //source: https://stackoverflow.com/questions/7359189/how-to-
		//change-the-mouse-cursor-in-java
		homeButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		
		//make panel for displaying labels
		resourcePanel = new JPanel();
		resourcePanel.setLayout(new BoxLayout(resourcePanel, BoxLayout.Y_AXIS));
		
		//load resource info
		try {
            //make scanner for file containing resources info
            Scanner fileInput = new Scanner(new File("src/main/assets/file/resources.txt")); 
            //determine delimiter
            //- set the separating factor to commas
            //- go to the left side of the file on the next line after scanning the current line
			//Source: https://stackoverflow.com/questions/1761051/difference-between-n-and-r 
            fileInput.useDelimiter(",|\r\n");
            //initialize label list and link list
            resourceLabels = new ArrayList<JLabel>();
            URIList = new ArrayList<String>();
            //check if there is a next line
            while(fileInput.hasNext()){
            	//add information to the lists
                JLabel label = new JLabel(fileInput.next());
                label.setFont(new Font("Times New Roman", Font.PLAIN, 30));
                label.setBackground(new Color(0xeaddff));
                label.setOpaque(true);
                URIList.add(fileInput.next());
                //change the cursor to a hand cursor when the user hovers over the label
                //source: https://stackoverflow.com/questions/7359189/how-to-change-the-mouse-cursor-in-java
                label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                
                //add the new label to the array list
                resourceLabels.add(label);
                //add label to panel
                label.setAlignmentX(CENTER_ALIGNMENT);
                resourcePanel.add(Box.createRigidArea(new Dimension(0,30)));
                resourcePanel.add(label);
            }
            //close scanner
            fileInput.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
		//add a scroll pane to the panel
        scroll = new JScrollPane(resourcePanel, 
            JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        //increase scrollspeed
        scroll.getVerticalScrollBar().setUnitIncrement(15);
        scroll.setPreferredSize(new Dimension(1000,500));
        scroll.setOpaque(false);
        
        //add components to background label
        titleLabel.setBounds(700,100,180,60);
        homeButton.setBounds(20,20,150,150);
        scroll.setBounds(300,200,1000,500);
        bgLabel.add(titleLabel);
        bgLabel.add(homeButton);
        bgLabel.add(scroll);
        //add label to frame
        add(bgLabel);
		
        //set the frame's cursor to the custom cursor
      	setCursor(AssetsController.getCursor());
		//set the properties of the frame
        setTitle("Resources");
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

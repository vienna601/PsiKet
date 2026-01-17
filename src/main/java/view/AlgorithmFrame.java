package view;

import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;

import controller.AssetsController;

/*
 * This program is responsible for configuring
 * the layout of the frame where the user can look
 * at visualizations of major steps in Grover's algorithm.
 */
public class AlgorithmFrame extends JFrame{
	//declare components
	//check if frame is disposed
	private boolean isDisposed = false;
	private JLabel bgLabel;
	private JLabel homeButton;
	private JLabel stepLabel;
	private JLabel stepImageLabel;
	//step explanation section (right side of frame)
	private JLabel explanationLabel;
	private JLabel backButton;
	private JLabel nextButton;
	private int curStep;
	
	//getters and setters for controller
	public JLabel getHomeButton() {
		return homeButton;
	}

	public void setHomeButton(JLabel homeButton) {
		this.homeButton = homeButton;
	}

	public JLabel getStepLabel() {
		return stepLabel;
	}

	public void setStepLabel(JLabel stepLabel) {
		this.stepLabel = stepLabel;
	}

	public JLabel getStepImageLabel() {
		return stepImageLabel;
	}

	public void setStepImageLabel(JLabel stepImageLabel) {
		this.stepImageLabel = stepImageLabel;
	}

	public JLabel getExplanationLabel() {
		return explanationLabel;
	}

	public void setExplanationLabel(JLabel explanationLabel) {
		this.explanationLabel = explanationLabel;
	}

	public JLabel getBackButton() {
		return backButton;
	}

	public void setBackButton(JLabel backButton) {
		this.backButton = backButton;
	}

	public JLabel getNextButton() {
		return nextButton;
	}

	public void setNextButton(JLabel nextButton) {
		this.nextButton = nextButton;
	}

	public int getCurStep() {
		return curStep;
	}

	public void setCurStep(int curStep) {
		this.curStep = curStep;
	}
	public boolean isDisposed() {
		return isDisposed;
	}
	public void setDisposed(boolean isDisposed) {
		this.isDisposed = isDisposed;
	}

	//constructor
	public AlgorithmFrame() {
		//set to initial step
		curStep = 0;
		//create background label
		bgLabel = new JLabel(new ImageIcon("src/main/assets/images/algorithmBg.jpg"));
		//call the set algorithm info method in assets controller (reads info from txt file)
		AssetsController.setAlgorithmInfo();
		//create step label with starting step
		stepLabel = new JLabel(AssetsController.getAlgorithmInfo()[curStep]);
		stepLabel.setFont(new Font("Times New Roman",Font.BOLD,30));
		stepLabel.setBackground(Color.white);
		stepLabel.setOpaque(true);
		//create image label with first step
		stepImageLabel = new JLabel(new ImageIcon("src/main/assets/images/step0.gif"));
		
		//create buttons
		homeButton = new JLabel(new ImageIcon("src/main/assets/images/homeButton.png"));
		backButton = new JLabel(new ImageIcon("src/main/assets/images/previousButton.png"));
		nextButton = new JLabel(new ImageIcon("src/main/assets/images/nextButton2.png"));

		//change to a hand cursor when the user hovers over the label
        //source: https://stackoverflow.com/questions/7359189/how-to-
		//change-the-mouse-cursor-in-java
		homeButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		backButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		nextButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		
		//explanation section
		explanationLabel = new JLabel(AssetsController.getAlgorithmInfo()[curStep+1]);
		explanationLabel.setFont(new Font("Times New Roman",Font.PLAIN,20));
		explanationLabel.setBackground(Color.white);
		explanationLabel.setOpaque(true);
		explanationLabel.setPreferredSize(new Dimension(550,500));
		
		//set bounds of components
		stepLabel.setBounds(100,200,800,80);
		stepImageLabel.setBounds(100,300,800,600);
		explanationLabel.setBounds(950,200,550,500);
		backButton.setBounds(1100,700,150,150);
		nextButton.setBounds(1300,700,150,150);
		homeButton.setBounds(20,20,150,150);
		//add to background label
		bgLabel.add(stepLabel);
		bgLabel.add(stepImageLabel);
		bgLabel.add(explanationLabel);
		bgLabel.add(backButton);
		bgLabel.add(nextButton);
		bgLabel.add(homeButton);
		add(bgLabel);
		
		//set the frame's cursor to the custom cursor
		setCursor(AssetsController.getCursor());
		//set the properties of the frame
        setTitle("Algorithms");
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

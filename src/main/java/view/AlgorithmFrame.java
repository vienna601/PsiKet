package view;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;

import controller.AssetsController;

public class AlgorithmFrame extends JFrame implements MouseListener{
	//declare components
	private JLabel bgLabel;
	private JLabel homeButton;
	private JLabel stepLabel;
	private JLabel stepImageLabel;
	//step explanation section (right side of frame)
	private JLabel explanationLabel;
	private JLabel backButton;
	private JLabel nextButton;
	private int curStep;
	
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
		//add mouse listener
		homeButton.addMouseListener(this);
		backButton.addMouseListener(this);
		nextButton.addMouseListener(this);
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

	@Override
	public void mouseClicked(MouseEvent e) {
		//check if the home button was clicked
		if((JLabel)e.getSource() == homeButton) {
			//dispose current frame and open home
			new HomeFrame();
			this.dispose();
		}
		else if((JLabel)e.getSource() == backButton) {
			//update step number with validation
			curStep = Math.max(0, curStep-2);
			//update labels
			stepLabel.setText(AssetsController.getAlgorithmInfo()[curStep]);
			stepImageLabel.setIcon(new ImageIcon("src/main/assets/images/step"+curStep/2+".gif"));
			explanationLabel.setText(AssetsController.getAlgorithmInfo()[curStep+1]);
			//reload 
			stepImageLabel.revalidate();
		}
		else if((JLabel)e.getSource() == nextButton) {
			//update step number with validation
			curStep = Math.min(10, curStep + 2);
			//update labels
			stepLabel.setText(AssetsController.getAlgorithmInfo()[curStep]);
			stepImageLabel.setIcon(new ImageIcon("src/main/assets/images/step"+curStep/2+".gif"));
			explanationLabel.setText(AssetsController.getAlgorithmInfo()[curStep+1]);
			//reload 
			stepImageLabel.revalidate();
		}
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

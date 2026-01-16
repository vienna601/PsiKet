package view;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import controller.AssetsController;

public class TitleFrame extends JFrame implements MouseListener{
	//declare components
	private JLabel bgLabel;
	private JLabel titleLabel;
	private JLabel homeButton;
	//for keeping track of audio
	private static boolean started = false;
	
	//constructor
	public TitleFrame() {
		//play music in the background
		//- use wav format since mp3 is not supported
		//Source: https://www.geeksforgeeks.org/play-audio-file-using-java/
		//check if the music is playing (since it will be looping,
		//it should not be started a second time)
		if (!started) {
			AudioInputStream audioInputStream;
			try {
				//
				audioInputStream = AudioSystem.getAudioInputStream(
						new File("src/main/assets/audio/startingMusic.wav").getAbsoluteFile());
				Clip clip = AudioSystem.getClip();
				//open the audio clip
				clip.open(audioInputStream);
				//lower the volume since it should just run in the background
				//Source: https://stackoverflow.com/questions/953598/audio-
				//volume-control-increase-or-decrease-in-java
				FloatControl volume = (FloatControl)clip.getControl(
						FloatControl.Type.MASTER_GAIN);
				//reduce volume by 20 decibels
				volume.setValue(-20.0f);
				//start playing the clip
				clip.start();
				//loop the audio clip continuously
				//since I want it to play in the background as long as 
				//the game is still active
				clip.loop(Clip.LOOP_CONTINUOUSLY);
				started = true;
			} //the catch prevents the program from being terminated even
			//if there is an error
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		//create background label
		bgLabel = new JLabel(new ImageIcon("src/main/assets/images/titleBg.jpg"));
		//create button that lets the user proceed to the next page
		homeButton = new JLabel(new ImageIcon("src/main/assets/images/nextButton.png"));
		//add tooltip
		homeButton.setToolTipText("Enter the tiny world!");
		//add mouse listener
		homeButton.addMouseListener(this);
		//change the cursor to a hand cursor when the user hovers over the label
        //source: https://stackoverflow.com/questions/7359189/how-to-change-the-mouse-cursor-in-java
        homeButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
		//create title label with html formatting
		titleLabel = new JLabel("<html><strong>PsiKet |𝜓⟩</strong><br>"
				+ "<br>Your portal to the tiny world.</html>");
		titleLabel.setForeground(Color.white);
		titleLabel.setFont(new Font("Times New Roman", Font.PLAIN,45));
		
		//set bounds for components
		homeButton.setBounds(963,209,300,100);
		titleLabel.setBounds(100,300,500,400);
		//add components
		bgLabel.add(homeButton);
		bgLabel.add(titleLabel);
		add(bgLabel);
		
		//run the method in the assets controller that creates the custom cursor
		//- I won't need to run the setter again in other frames since the title 
		//  frame is always the first frame
		AssetsController.setCursor();
		//set the title frame's cursor to the custom cursor
		setCursor(AssetsController.getCursor());
		
		//set the properties of the frame
        setTitle("PsiKet |ψ⟩");
        setSize(1600, 1000);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        pack();
        setVisible(true);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		//check if user wants to proceed to the next page
		if((JLabel)e.getSource() == homeButton) {
			//construct a home frame and dispose the current
			new HomeFrame();
			this.dispose();
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// unused method
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		// unused method
	}
	@Override
	public void mouseEntered(MouseEvent e) {
		// unused method
	}
	@Override
	public void mouseExited(MouseEvent e) {
		//	unused method
	}

}

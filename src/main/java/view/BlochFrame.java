package view;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLJPanel;

import controller.AssetsController;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;

public class BlochFrame extends JFrame{
	//declare components
	private JLabel bgLabel;
	private JLabel homeButton;
	private JLabel curCoordsLabel;
	//default options section
	private JLabel[] options;
	private JPanel optionsPanel;
	//angle customization section
	private JTextField[] angleFields;
	private JRadioButton[] piButtons;
	private JButton confirmButton;
	private JLabel angleLabel1;
	private JLabel angleLabel2;
	private JPanel anglePanel;
	private JPanel mainPanel;
	private double[] angle;
	//for the sphere panel
	private int lastX;
    private int lastY;
    private SphereEventListener sphereEvent;
    private GLJPanel spherePanel;
    private JLabel axisLabel;
	
    //getters and setters for setting the info about the qubit representation
    public SphereEventListener getSphereEvent() {
		return sphereEvent;
	}
	public void setSphereEvent(SphereEventListener sphereEvent) {
		this.sphereEvent = sphereEvent;
	}
	public double[] getAngle() {
		return angle;
	}
	public void setAngle(double[] angle) {
		this.angle = angle;
	}
	//for the controller to access components
	public JLabel getHomeButton() {
		return homeButton;
	}
	public void setHomeButton(JLabel homeButton) {
		this.homeButton = homeButton;
	}
	public JLabel getCurCoordsLabel() {
		return curCoordsLabel;
	}
	public void setCurCoordsLabel(JLabel curCoordsLabel) {
		this.curCoordsLabel = curCoordsLabel;
	}
	public JLabel[] getOptions() {
		return options;
	}
	public void setOptions(JLabel[] options) {
		this.options = options;
	}
	public JTextField[] getAngleFields() {
		return angleFields;
	}
	public void setAngleFields(JTextField[] angleFields) {
		this.angleFields = angleFields;
	}
	public JRadioButton[] getPiButtons() {
		return piButtons;
	}
	public void setPiButtons(JRadioButton[] piButtons) {
		this.piButtons = piButtons;
	}
	public JButton getConfirmButton() {
		return confirmButton;
	}
	public void setConfirmButton(JButton confirmButton) {
		this.confirmButton = confirmButton;
	}
	public JLabel getAngleLabel1() {
		return angleLabel1;
	}
	public void setAngleLabel1(JLabel angleLabel1) {
		this.angleLabel1 = angleLabel1;
	}
	public JLabel getAngleLabel2() {
		return angleLabel2;
	}
	public void setAngleLabel2(JLabel angleLabel2) {
		this.angleLabel2 = angleLabel2;
	}
	public int getLastX() {
		return lastX;
	}
	public void setLastX(int lastX) {
		this.lastX = lastX;
	}
	public int getLastY() {
		return lastY;
	}
	public void setLastY(int lastY) {
		this.lastY = lastY;
	}
	public GLJPanel getSpherePanel() {
		return spherePanel;
	}
	public void setSpherePanel(GLJPanel spherePanel) {
		this.spherePanel = spherePanel;
	}

	//constructor
	public BlochFrame() {
		//make background label
		bgLabel = new JLabel(new ImageIcon("src/main/assets/images/blochBg.png"));
		//make home button label
		homeButton = new JLabel(new ImageIcon("src/main/assets/images/homeButton.png"));
		//create an axis image label that indicates which values are for which colors
		axisLabel = new JLabel(new ImageIcon("src/main/assets/images/axis.png"));
		//create a label that shows the current qubit representation's coordinates
		curCoordsLabel = new JLabel("<html>Cartesian coordinates: (x,y,z)<br>"+
					"Spherical coordinates: (r,θ,φ)</html>");
		//set format
		curCoordsLabel.setFont(new Font("Times New Roman",Font.BOLD, 30));
		curCoordsLabel.setBackground(Color.white);
		curCoordsLabel.setOpaque(true);
		
		//===================== sphere section =======================
		//Use OpenGL API through the JOGL library
		//Maven repository: https://mvnrepository.com/artifact/
		//org.jogamp.gluegen/gluegen-rt-main/2.6.0-rc-20250628
		//create a profile and configure context
		//Source: https://mbien.dev/blog/entry/jogl_2_opengl_profiles_explained
		GLProfile profile = GLProfile.get(GLProfile.GL2);
        GLCapabilities caps = new GLCapabilities(profile);
        //create open gl drawing interface
        //- panel instead of frame since it will only 
        //  occupy the left side of the page
        spherePanel = new GLJPanel(caps);
        //create instance of renderer object
        sphereEvent = new SphereEventListener();
        //add subclass of GLEventListener (helps render the object visually)
        spherePanel.addGLEventListener(sphereEvent);
        spherePanel.setPreferredSize(new Dimension(700,600));

        //===================== default options section =======================
		//make option labels and set the background (they have different colours)
		options = new JLabel[3];
		options[0] = new JLabel("{|0⟩,|1⟩}");
		options[0].setBackground(new Color(0xace5ff));
		options[1] = new JLabel("{|+⟩,|−⟩}");
		options[1].setBackground(new Color(0xffd3d3));
		options[2] = new JLabel("{|+i⟩,|−i⟩}");
		options[2].setBackground(new Color(0xeaddff));
		//make options panel
		optionsPanel = new JPanel();
		optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.Y_AXIS));
		optionsPanel.setPreferredSize(new Dimension(300,600));
		optionsPanel.setBackground(new Color(255,255,255,100));
		//loop through options
		for(JLabel option : options) {
			option.setFont(new Font("Times New Roman",Font.BOLD, 45));
			option.setOpaque(true);
			//add options to the panel
			option.setAlignmentX(CENTER_ALIGNMENT);
			//add buffer space
			optionsPanel.add(Box.createRigidArea(new Dimension(0,40)));
			optionsPanel.add(option);
		}
		//add the axis label to the options panel
		optionsPanel.add(Box.createRigidArea(new Dimension(0,20)));
		axisLabel.setAlignmentX(CENTER_ALIGNMENT);
		optionsPanel.add(axisLabel);
		
		//===================== angle customization section =======================
		//initialize angle array (for changing qubit representation later)
		setAngle(new double[2]);
		//make label that tells the user what they're customizing with the angle
		angleLabel1 = new JLabel("<html>Enter α∈[0,π) for<br>∣ψα⟩=cos(α)∣0⟩+sin(α)∣1⟩</html>");
		angleLabel1.setFont(new Font("Times New Roman",Font.BOLD, 25));
		angleLabel1.setOpaque(true);
		//make text field for the user to enter the angle
		angleFields= new JTextField[3];
		angleFields[0] = new JTextField(20);
		angleFields[0].setText("Enter angle α in radians");
		
		//second set of label and field
		angleLabel2 = new JLabel("<html>Enter 𝜃 ∈[0,π), 𝜑 ∈[0,π) for<br>"+
						"|𝜓⟩=cos(𝜃/2)|0⟩+<br>e^(𝑖𝜑)sin(𝜃/2)|1⟩</html>");
		angleLabel2.setFont(new Font("Times New Roman",Font.BOLD, 25));
		angleLabel2.setOpaque(true);
		//make text fields for the user to enter the angles
		angleFields[1] = new JTextField(20);
		angleFields[1].setText("Enter angle 𝜃 in radians");
		angleFields[2] = new JTextField(20);
		angleFields[2].setText("Enter angle 𝜑 in radians");
		//settings for all text fields
		for(JTextField field : angleFields) {
			field.setFont(new Font("Times New Roman",Font.PLAIN, 25));
			field.setEditable(true);
			field.setPreferredSize(new Dimension(350,60));
			field.setAlignmentX(CENTER_ALIGNMENT);
		}
		
		//make radio buttons for selecting whether the user wants 
		//to multiply by the exact value of pi (angle is in radians)
		piButtons = new JRadioButton[3];
		for(int i = 0; i < piButtons.length; i++) {
			piButtons[i] = new JRadioButton("Multiply by π");
			piButtons[i].setFont(new Font("Times New Roman",Font.ITALIC, 20));
			piButtons[i].setAlignmentX(CENTER_ALIGNMENT);
		}
		//make confirm button
		confirmButton = new JButton("Confirm");
		confirmButton.setFont(new Font("Times New Roman",Font.PLAIN, 20));
		
		//make the angle customization panel
		anglePanel = new JPanel();
		anglePanel.setBackground(new Color(255,255,255,100));
		anglePanel.setLayout(new BoxLayout(anglePanel, BoxLayout.Y_AXIS));
		//set alignment of components in the angle panel
		angleLabel1.setAlignmentX(CENTER_ALIGNMENT);
		angleLabel2.setAlignmentX(CENTER_ALIGNMENT);
		confirmButton.setAlignmentX(CENTER_ALIGNMENT);
		//add components to the panel
		anglePanel.add(angleLabel1);
		anglePanel.add(Box.createRigidArea(new Dimension(0,30)));
		//same length for both arrays
		for(int i = 0; i < angleFields.length; i++) {
			//after the first set of label and button
			if(i == 1) {
				//add the second angle label
				anglePanel.add(angleLabel2);
			}
			//add text field and buffer space
			anglePanel.add(angleFields[i]);
			anglePanel.add(piButtons[i]);
			anglePanel.add(Box.createRigidArea(new Dimension(0,30)));
		}
		anglePanel.add(confirmButton);
		anglePanel.setPreferredSize(new Dimension(350,600));
		
		//===================== combining everything section =======================
		//add the panels to the main panel
		mainPanel = new JPanel(new FlowLayout(FlowLayout.LEFT,50,50));
		mainPanel.add(spherePanel);
		mainPanel.add(optionsPanel);
		mainPanel.add(anglePanel);
		mainPanel.setOpaque(false);
		//add components to the background
		bgLabel.setLayout(null);
		homeButton.setBounds(20,20,150,150);
		bgLabel.add(homeButton);
		curCoordsLabel.setBounds(200,50,570,120);
		bgLabel.add(curCoordsLabel);
		mainPanel.setBounds(20,150,1550,900);
		bgLabel.add(mainPanel);
		//add the background to the frame
		add(bgLabel);
		
		//set the frame's cursor to the custom cursor
		setCursor(AssetsController.getCursor());
		//set the properties of the frame
        setTitle("Bloch Sphere");
        setSize(1600, 1000);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        pack();
        setVisible(true);
	}
	
	//dispose the frame
	public void dispose() {
		this.dispose();
	}
}

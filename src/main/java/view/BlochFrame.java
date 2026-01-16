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

public class BlochFrame extends JFrame implements MouseListener, MouseMotionListener{
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

	//constructor
	public BlochFrame() {
		//make background label
		bgLabel = new JLabel(new ImageIcon("src/main/assets/images/blochBg.png"));
		//make home button label
		homeButton = new JLabel(new ImageIcon("src/main/assets/images/homeButton.png"));
		homeButton.addMouseListener(this);
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
        //add listeners (helps detect mouse actions)
        spherePanel.addMouseListener(this);
        spherePanel.addMouseMotionListener(this);
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
			option.addMouseListener(this);
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
		confirmButton.addMouseListener(this);
		
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
	
	//calculate coordinates for display on the curCoordsLabel
	//- I will already have the cartesian coordinates since I will
	//  call this method just after updating the sphere
	public void setCoordText(String x, String y, String z, String theta, String phi) {
		//get indices for substring endpoints
		String str = curCoordsLabel.getText();
		int end1 = str.indexOf("(");
		int start1 = str.indexOf(")<br>");
		int end2 = str.indexOf("Spherical")+23;
		//construct new string
		String newStr = str.substring(0,end1+1) + x + "," + y +" ," + 
				z + str.substring(start1,end2+1) + "1," + theta + ","+ phi + ")";
		//set text to the new string
		curCoordsLabel.setText(newStr);
	}
	
	//validate angle input
	public boolean isValidAngle(int index) {
		//use index to get field and button values
		//assign to text in text field
		String angleText = angleFields[index].getText().trim();
		//check if entered text is not empty and purely numerical 
		//(with the exception of a period for decimals)
		//- ^(beginning of string), $ (end of string)
		//SOurce: https://stackoverflow.com/questions/
		//10575624/how-do-i-check-if-a-string-contains-only
		//-numbers-and-not-letters
		if(!angleText.isEmpty() && angleText.matches("^[0-9.]+$")) {
			//convert string to double
			double angle = Double.parseDouble(angleText);
			//check if the "multiply by π" radio button is checked
			if(piButtons[index].isSelected()) {
				//multiply by exact value of pi
				angle *= Math.PI;
			}
			//initialize array for updating angle
			double[] angles = getAngle();
			//check if the angle is within range
			if(index > 1) {
				//set to second index
				angles[1] = angle;
				setAngle(angles);
				//the azimuthal angle has a different range than other angles
				return angle >= 0 && angle < 2 * Math.PI;
			}
			else {
				//set to first index
				angles[0] = angle;
				setAngle(angles);
				//return whether value is within range
				return angle >= 0 && angle < Math.PI;
			}
		}
		else
			//empty or not completely numerical
			return false;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		//check if the action source is an instance of JLabel 
		//(avoid type cast error)
		if(e.getSource() instanceof JLabel) {
			//check if the user clicked the home button label
			if((JLabel)e.getSource() == homeButton) {
				//open the home frame and dispose the current
				new HomeFrame();
				this.dispose();
			}
			//check if user clicked any of the default options
			//- these points are on the ends of the axes
			//Sources: 
			//- https://www.mathworks.com/help/matlab/math/
			//introduction-to-quantum-computing.html
			//- https://quantum.cloud.ibm.com/learning/en/courses/
			//general-formulation-of-quantum-information/density-matrices/bloch-sphere
			else if((JLabel)e.getSource() == options[0]) {
				//user clicked computational basis
				//update fields in sphere event listener to display
				//tiny spheres representing the states
				float[] temp = {0.05f,0,0,1};
				getSphereEvent().setQubitInfo(temp);
				float[] temp2 = {0.05f,0,0,-1};
				getSphereEvent().setQubitInfo2(temp2);
				//reload the panel
				spherePanel.repaint();
				//set coordinate label text
				//- I will put N/A for the theta coordinate since they has
				//  different values (not just positive/negative difference) for
				//  each qubit displayed. Phi stays the same for both.
				//I used this calculator: https://www.omnicalculator.com/math/spherical-
				//coordinates#converting-cartesian-to-spherical
				setCoordText("+-1", "0", "0", "N/A", "0");
			}
			else if((JLabel)e.getSource() == options[1]) {
				//user clicked hadamard basis
				//update fields in sphere event listener to display
				//tiny spheres representing the states
				float[] temp = {0.05f,1,0,0};
				getSphereEvent().setQubitInfo(temp);
				float[] temp2 = {0.05f,-1,0,0};
				getSphereEvent().setQubitInfo2(temp2);
				//reload the panel
				spherePanel.repaint();
				//set coordinate label text
				//- I will put N/A for the phi coordinate since it has
				//  different values (not just positive/negative difference) for
				//  each qubit displayed. Theta is the same for both.
				//I used this calculator: https://www.omnicalculator.com/math/spherical-
				//coordinates#converting-cartesian-to-spherical
				setCoordText("+-1", "0", "0", "0.5", "N/A");
			}
			else if((JLabel)e.getSource() == options[2]) {
				//user clicked circular basis
				//update fields in sphere event listener to display
				//tiny spheres representing the states
				float[] temp = {0.05f,0,1,0};
				getSphereEvent().setQubitInfo(temp);
				float[] temp2 = {0.05f,0,-1,0};
				getSphereEvent().setQubitInfo2(temp2);
				//reload the panel
				spherePanel.repaint();
				//set coordinate label text
				//I used this calculator: https://www.omnicalculator.com/math/spherical-
				//coordinates#converting-cartesian-to-spherical
				setCoordText("0", "+-1", "0", "π/2", "+-π/2");
			}
		}
		else if((JButton)e.getSource() == confirmButton) {
			/* The standard representation of an arbitrary qubit on the
			 * Bloch Sphere |𝜓⟩ is |𝜓⟩=cos(𝜃/2)|0⟩+e𝑖𝜑sin(𝜃/2)|1⟩
			 * where 𝜃 is the polar angle and 𝜑 is the azimuthal 
			 * angle. The Cartesian coordinates (x,y,z) are related 
			 * to these angles (used in spherical coordinates) by: 
			 * x = sin(𝜃)cos(𝜑), y = sin(𝜃)sin(𝜑), z = cos(𝜃). 
			 * - Note: the relationship is based on a sphere with radius 1
			 *   (unit sphere)
			 * 
			 * When the given state is ∣𝜓α⟩=cos(α)∣0⟩+sin(α)∣1⟩, I can map
			 * this to the standard qubit representation. For example,
			 * we can equate the amplitudes ("coefficients of states"):
			 * cos(𝜃/2) = cos(α) -> 𝜃 = 2α
			 * Same for the amplitude for ∣1⟩:
			 * e𝑖𝜑sin(𝜃/2) = sin(α) -> using 𝜃 = 2α, we can find that
			 * e𝑖𝜑sin(𝜃/2) = sin(𝜃/2) and thus e𝑖𝜑 = 1, 𝜑 = 0
			 * 
			 * Now, I can substitute these findings into the formula
			 * - x = sin(𝜃)cos(𝜑) = sin(2α)
			 * - y = sin(𝜃)sin(𝜑) = 0
			 * - z = cos(𝜃) = cos(2α)
			 * 
			 * Note that the angle should be within range of α∈[0,π).
			 * It needs to be within this range due to phase in quantum mechanics.
			 * Values beyond pi do not create new states in this given state form.
			 * - Source: https://physics.stackexchange.com/questions/552796/the-
			 *   importance-of-the-phase-in-quantum-mechanics
			 * 
			 * Also, both formats of states guarantees normalization since the length 
			 * is the sum of squared magnitudes of amplitudes (sin^2 + cos^2 = 1 and
			 * magnitude of e𝑖𝜑 for all 𝜑)
			 * 
			 * Sources: 
			 * - https://quantum.cloud.ibm.com/learning/en/courses/general-
			 *   formulation-of-quantum-information/density-matrices/bloch-sphere
			 * - https://mathworld.wolfram.com/SphericalCoordinates.html
			 * - https://mathworld.wolfram.com/PolarAngle.html
			 * - https://mathworld.wolfram.com/Azimuth.html
			 * - https://en.wikipedia.org/wiki/Spherical_coordinate_system
			 */
			
			//only one qubit should be displayed 
			//(set radius of second qubit sphere to negative)
			float[] temp2 = {-1,0,0,-1};
			getSphereEvent().setQubitInfo2(temp2);
			//check if the user chose the first or second state form
			if(isValidAngle(0)) {
				//first state form: ∣𝜓α⟩=cos(α)∣0⟩+sin(α)∣1⟩
				//calculate coordinates (float type since openGL uses
				//floats for transformations and coordinates)
				float x = (float) Math.sin(2 * angle[0]);
				float z = (float) Math.cos(2 * angle[0]);
				//update field in sphere event listener to display
				//tiny sphere representing the state
				float[] temp = {0.05f, x, 0, z};
				getSphereEvent().setQubitInfo(temp);
				//reload the panel
				spherePanel.repaint();
				
				//update the coordinate label
				//𝜃 = 2α, 𝜑 = 0, r(radius) = 1 (since it is a unit sphere)
				//round to 3 decimals
				//Source: https://www.baeldung.com/java-round-decimal-number
				String theta = String.format("%.3f",2 * angle[0]);
				setCoordText(String.format("%.3f",x), "0", String.format("%.3f",z), theta, "0");
			}
			else if (isValidAngle(1) && isValidAngle(2)) {
				//second state form: |𝜓⟩=cos(𝜃/2)|0⟩+e𝑖𝜑sin(𝜃/2)|1⟩
				//coordinates: x = sin(𝜃)cos(𝜑), y = sin(𝜃)sin(𝜑), z = cos(𝜃)
				float x = (float) (Math.sin(angle[0]) * Math.cos(angle[1]));
				float y = (float) (Math.sin(angle[0]) * Math.sin(angle[1]));
				float z = (float) Math.cos(angle[0]);
				//update field in sphere event listener to display
				//tiny sphere representing the state
				float[] temp = {0.05f, x, y, z};
				getSphereEvent().setQubitInfo(temp);
				//reload the panel
				spherePanel.repaint();
				
				//round angles to 3 decimals and update coordinate label
				setCoordText(String.format("%.3f",x), String.format("%.3f",y), 
						String.format("%.3f",z), String.format("%.3f",angle[0]), 
						String.format("%.3f",angle[1]));
			}
			//none of the values in the field combinations were valid
			else {
				//display error message
				//Source: https://docs.oracle.com/javase/8/
				//docs/api/javax/swing/JOptionPane.html
				JOptionPane.showMessageDialog(this, 
						"Please ensure that numerical value(s) are entered.\n"+
						"P.S. Pay attention to the range of each angle!",
						"Input Error", JOptionPane.ERROR_MESSAGE);
			}
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

	@Override
	public void mouseDragged(MouseEvent e) {
		//get the current position
		int x = e.getX();
        int y = e.getY();
        //calculate change in position
        int dx = x - lastX;
        int dy = y - lastY;
        // 
        sphereEvent.addRotation(dy * 0.5f, dx * 0.5f);
        //update the variables that keep track 
        //of the previous position
        lastX = x;
        lastY = y;
        //reload the appearance of the panel
        spherePanel.repaint();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// unused method
	}
	
}

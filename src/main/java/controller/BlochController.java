package controller;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import view.BlochFrame;
import view.HomeFrame;

public class BlochController implements MouseListener, MouseMotionListener{
	//declare fields
	private BlochFrame blochFrame;
	
	//constructor
	public BlochController() {
		//empty constructor since I don't need to create any frames
		//until the user clicks on the corresponding button on the home frame
	}
	//getters and setters
	public BlochFrame getBlochFrame() {
		return blochFrame;
	}


	public void setBlochFrame() {
		this.blochFrame = new BlochFrame();
		//add mouse and mouse motion listeners to components
		getBlochFrame().getHomeButton().addMouseListener(this);
		getBlochFrame().getSpherePanel().addMouseListener(this);
		getBlochFrame().getSpherePanel().addMouseMotionListener(this);
		for(JLabel option : getBlochFrame().getOptions()) {
			option.addMouseListener(this);
		}
		getBlochFrame().getConfirmButton().addMouseListener(this);
	}

	//calculate coordinates for display on the curCoordsLabel
	//- I will already have the cartesian coordinates since I will
	//  call this method just after updating the sphere
	public void setCoordText(String x, String y, String z, String theta, String phi) {
		//get indices for substring endpoints
		String str = getBlochFrame().getCurCoordsLabel().getText();
		int end1 = str.indexOf("(");
		int start1 = str.indexOf(")<br>");
		int end2 = str.indexOf("Spherical")+23;
		//construct new string
		String newStr = str.substring(0,end1+1) + x + "," + y +" ," + 
				z + str.substring(start1,end2+1) + "1," + theta + ","+ phi + ")";
		//set text to the new string
		getBlochFrame().getCurCoordsLabel().setText(newStr);
	}
	
	//validate angle input
	public boolean isValidAngle(int index) {
		//use index to get field and button values
		//assign to text in text field
		String angleText = getBlochFrame().getAngleFields()[index].getText().trim();
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
			if(getBlochFrame().getPiButtons()[index].isSelected()) {
				//multiply by exact value of pi
				angle *= Math.PI;
			}
			//initialize array for updating angle
			double[] angles = getBlochFrame().getAngle();
			//check if the angle is within range
			if(index > 1) {
				//set to second index
				angles[1] = angle;
				getBlochFrame().setAngle(angles);
				//the azimuthal angle has a different range than other angles
				return angle >= 0 && angle < 2 * Math.PI;
			}
			else {
				//set to first index
				angles[0] = angle;
				getBlochFrame().setAngle(angles);
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
			if((JLabel)e.getSource() == getBlochFrame().getHomeButton()) {
				//open the home frame and dispose the current
				new HomeFrame();
				getBlochFrame().dispose();
			}
			//check if user clicked any of the default options
			//- these points are on the ends of the axes
			//Sources: 
			//- https://www.mathworks.com/help/matlab/math/
			//introduction-to-quantum-computing.html
			//- https://quantum.cloud.ibm.com/learning/en/courses/
			//general-formulation-of-quantum-information/density-matrices/bloch-sphere
			else if((JLabel)e.getSource() == getBlochFrame().getOptions()[0]) {
				//user clicked computational basis
				//update fields in sphere event listener to display
				//tiny spheres representing the states
				float[] temp = {0.05f,0,0,1};
				getBlochFrame().getSphereEvent().setQubitInfo(temp);
				float[] temp2 = {0.05f,0,0,-1};
				getBlochFrame().getSphereEvent().setQubitInfo2(temp2);
				//reload the panel
				getBlochFrame().getSpherePanel().repaint();
				//set coordinate label text
				//- I will put N/A for the theta coordinate since they has
				//  different values (not just positive/negative difference) for
				//  each qubit displayed. Phi stays the same for both.
				//I used this calculator: https://www.omnicalculator.com/math/spherical-
				//coordinates#converting-cartesian-to-spherical
				setCoordText("+-1", "0", "0", "N/A", "0");
			}
			else if((JLabel)e.getSource() == getBlochFrame().getOptions()[1]) {
				//user clicked hadamard basis
				//update fields in sphere event listener to display
				//tiny spheres representing the states
				float[] temp = {0.05f,1,0,0};
				getBlochFrame().getSphereEvent().setQubitInfo(temp);
				float[] temp2 = {0.05f,-1,0,0};
				getBlochFrame().getSphereEvent().setQubitInfo2(temp2);
				//reload the panel
				getBlochFrame().getSpherePanel().repaint();
				//set coordinate label text
				//- I will put N/A for the phi coordinate since it has
				//  different values (not just positive/negative difference) for
				//  each qubit displayed. Theta is the same for both.
				//I used this calculator: https://www.omnicalculator.com/math/spherical-
				//coordinates#converting-cartesian-to-spherical
				setCoordText("+-1", "0", "0", "0.5", "N/A");
			}
			else if((JLabel)e.getSource() == getBlochFrame().getOptions()[2]) {
				//user clicked circular basis
				//update fields in sphere event listener to display
				//tiny spheres representing the states
				float[] temp = {0.05f,0,1,0};
				getBlochFrame().getSphereEvent().setQubitInfo(temp);
				float[] temp2 = {0.05f,0,-1,0};
				getBlochFrame().getSphereEvent().setQubitInfo2(temp2);
				//reload the panel
				getBlochFrame().getSpherePanel().repaint();
				//set coordinate label text
				//I used this calculator: https://www.omnicalculator.com/math/spherical-
				//coordinates#converting-cartesian-to-spherical
				setCoordText("0", "+-1", "0", "π/2", "+-π/2");
			}
		}
		else if((JButton)e.getSource() == getBlochFrame().getConfirmButton()) {
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
			getBlochFrame().getSphereEvent().setQubitInfo2(temp2);
			//check if the user chose the first or second state form
			if(isValidAngle(0)) {
				//first state form: ∣𝜓α⟩=cos(α)∣0⟩+sin(α)∣1⟩
				//calculate coordinates (float type since openGL uses
				//floats for transformations and coordinates)
				float x = (float) Math.sin(2 * getBlochFrame().getAngle()[0]);
				float z = (float) Math.cos(2 * getBlochFrame().getAngle()[0]);
				//update field in sphere event listener to display
				//tiny sphere representing the state
				float[] temp = {0.05f, x, 0, z};
				getBlochFrame().getSphereEvent().setQubitInfo(temp);
				//reload the panel
				getBlochFrame().getSpherePanel().repaint();
				
				//update the coordinate label
				//𝜃 = 2α, 𝜑 = 0, r(radius) = 1 (since it is a unit sphere)
				//round to 3 decimals
				//Source: https://www.baeldung.com/java-round-decimal-number
				String theta = String.format("%.3f",2 * getBlochFrame().getAngle()[0]);
				setCoordText(String.format("%.3f",x), "0", String.format("%.3f",z), theta, "0");
			}
			else if (isValidAngle(1) && isValidAngle(2)) {
				//second state form: |𝜓⟩=cos(𝜃/2)|0⟩+e𝑖𝜑sin(𝜃/2)|1⟩
				//coordinates: x = sin(𝜃)cos(𝜑), y = sin(𝜃)sin(𝜑), z = cos(𝜃)
				float x = (float) (Math.sin(getBlochFrame().getAngle()[0]) * 
						Math.cos(getBlochFrame().getAngle()[1]));
				float y = (float) (Math.sin(getBlochFrame().getAngle()[0]) * 
						Math.sin(getBlochFrame().getAngle()[1]));
				float z = (float) Math.cos(getBlochFrame().getAngle()[0]);
				//update field in sphere event listener to display
				//tiny sphere representing the state
				float[] temp = {0.05f, x, y, z};
				getBlochFrame().getSphereEvent().setQubitInfo(temp);
				//reload the panel
				getBlochFrame().getSpherePanel().repaint();
				
				//round angles to 3 decimals and update coordinate label
				setCoordText(String.format("%.3f",x), String.format("%.3f",y), 
						String.format("%.3f",z), 
						String.format("%.3f",getBlochFrame().getAngle()[0]), 
						String.format("%.3f",getBlochFrame().getAngle()[1]));
			}
			//none of the values in the field combinations were valid
			else {
				//display error message
				//Source: https://docs.oracle.com/javase/8/
				//docs/api/javax/swing/JOptionPane.html
				JOptionPane.showMessageDialog(getBlochFrame(), 
						"Please ensure that numerical value(s) are entered.\n"+
						"P.S. Pay attention to the range of each angle!",
						"Input Error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		//get the current position
		int x = e.getX();
        int y = e.getY();
        //calculate change in position
        int dx = x - getBlochFrame().getLastX();
        int dy = y - getBlochFrame().getLastY();
        // 
        getBlochFrame().getSphereEvent().addRotation(dy * 0.5f, dx * 0.5f);
        //update the variables that keep track 
        //of the previous position
        getBlochFrame().setLastX(x);
        getBlochFrame().setLastY(y);
        //reload the appearance of the panel
        getBlochFrame().getSpherePanel().repaint();
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
	public void mouseMoved(MouseEvent e) {
		// unused method
	}
}

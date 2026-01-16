package controller;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Comparator;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import model.Basis;
import model.Gate;
import model.StateVector;
import view.CircuitFrame;
import view.CircuitImportFrame;
import view.HomeFrame;

public class CircuitController implements MouseListener, KeyListener, ActionListener{
	//declare fields
	private CircuitFrame circuitFrame;
	private CircuitImportFrame importFrame;
	
	//constructor
	public CircuitController() {
		//empty constructor since I don't need to create any frames
		//until the user clicks on the corresponding button on the home frame
	}
	
	//getters and setters
	public CircuitFrame getCircuitFrame() {
		return circuitFrame;
	}

	public void setCircuitFrame(CircuitFrame circuitFrame) {
		this.circuitFrame = circuitFrame;
	}
	
	public CircuitImportFrame getImportFrame() {
		return importFrame;
	}

	public void setImportFrame(CircuitImportFrame importFrame) {
		this.importFrame = importFrame;
	}

	//utility methods
	//add gates to the GUI JPanel
	public void addGateToGUI(Gate gate) {
		//add the gate as an image button to the panel
		JButton newGate = new JButton(new ImageIcon(gate.getImagePath()));
		//change to a hand cursor when the user hovers over the label
        //source: https://stackoverflow.com/questions/7359189/how-to-
		//change-the-mouse-cursor-in-java
		newGate.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		//add tooltip
		newGate.setToolTipText("Click to learn more about the "+ gate.getName() + " gate!");
		getCircuitFrame().getGatePanel().add(newGate);
	}
	
	//gate search method
	public ArrayList<Gate> gateSearch(String input){
		//change to lower case for comparison later
		input = input.toLowerCase();
		//load gates using the assets controller
		AssetsController.setGateMap();
		//convert all values to an arraylist
		ArrayList<Gate> gates = new ArrayList<Gate>(
				AssetsController.getGateMap().values());
		//sort the arraylist in alphabetical order by name
		//Source: https://stackoverflow.com/questions/2784514/
		//sort-arraylist-of-custom-objects-by-property
		gates.sort(Comparator.comparing(Gate :: getName));
		
		//get the lower bound
		int lower = 0;
		int higher = gates.size();
		int mid = 0;
		String midString = "";
		//check if lower bound is less than higher bound
		while (lower < higher) {
			//get the string at the midpoint of the array
			mid = (lower + higher)/2;
			midString = gates.get(mid).getName();
			//I can use the compareTo method to check if 
			//the input string has a greater unicode value in
			//each character (I can use this since I made everything
			//lowercase so ASCII values have fair comparisons)
			//SOurce: https://www.geeksforgeeks.org/java/java-
			//string-compareto-method-with-examples/
			if(midString.compareTo(input) < 0)
				lower = mid + 1;
			//the characters in the middle string are greater
			//or equal to the input string's
			else
				higher = mid;
		}
		
		//get the higher bound
		//- start at the lower bound found earlier
		for(int i = lower; i < gates.size(); i++) {
			//update higher bound as the current index
			//(since higher bound is exclusive in sublist)
			higher = i;
			//check if gate still starts with the input (prefix)
			if(!gates.get(i).getName().startsWith(input))
				//break out of the loop
				break;
		}
		//check if index reached the end of gate and if the
		//last gate matches (since it would be omitted in gates.subList()
		//if gates.size() isn't used as the upper bound
		if(higher == gates.size()-1 && gates.get(higher).getName().startsWith(input))
			higher++;
		//return sub array using bounds
		return new ArrayList<Gate>(gates.subList(lower,higher));
	}	

	@Override
	public void keyPressed(KeyEvent e) {
		//Check if the enter key was pressed
	    //https://forum.inductiveautomation.com/t/simulate-a-button-press-event-
		//using-the-enter-key-no-matter-what-is-in-focus/80462/3
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            //get the text
            String searchText = getCircuitFrame().getSearchField().getText().trim();
            System.out.println(searchText);
            //check if input is valid
            if(!searchText.isEmpty()){
                //get results from binary search
                ArrayList<Gate>results = gateSearch(searchText);
                //reset current search results
                getCircuitFrame().getResultsLabels().clear();
                //remove components that are already shown
                //source: https://forums.oracle.com/ords/apexds/post/
                //removing-components-from-a-jpanel-3091
                getCircuitFrame().getResultsPanel().removeAll();
                //add gates to results panel
                for(int i = 0; i < results.size(); i++) {
                	JLabel label = new JLabel(results.get(i).getName());
                	System.out.println(results.get(i).getName());
                	//set the format
                	label.setFont(new Font("Times New Roman",Font.PLAIN, 40));
                	label.setBackground(new Color(0xeaddff));
                	label.setOpaque(true);
                	//set tool tip text
                	label.setToolTipText("Add the "+results.get(i).getName()+
                			" to the circuit!");
                	label.addMouseListener(this);
                	//change to a hand cursor when the user hovers over the label
                    //source: https://stackoverflow.com/questions/7359189/how-to-
            		//change-the-mouse-cursor-in-java
            		label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            		
            		getCircuitFrame().getResultsPanel().add(Box.createRigidArea(new Dimension(0,50)));
            		label.setAlignmentX(Component.CENTER_ALIGNMENT);
            		//add to panel and list (used for checking which label
            		//was clicked in mouse listener)
            		getCircuitFrame().getResultsLabels().add(label);
            		getCircuitFrame().getResultsPanel().add(label);
                }
                //reload the panel
                getCircuitFrame().getResultsPanel().repaint();
                getCircuitFrame().getResultsPanel().revalidate();
            }
        }
		
	}
	
	//detecting actions with search result labels and
	//probability panel labels
	@Override
	public void mouseClicked(MouseEvent e) {
		//check if the probability label was pressed
		if((JLabel)e.getSource() == getCircuitFrame().getProbLabels()[1]) {
			//get the qubit in the circuit and its measurement
			//probabilities
			double[] probs = getCircuitFrame().getCircuit().getQubit().calcProbs();
			//get the basis and set the indices used to get
			//string representations
			int index = 0;
			Basis basis = Basis.COMPUTATIONAL;
			if(getCircuitFrame().getBasisButton().isSelected()) {
				basis = Basis.HADAMARD;
				index += 2;
			}
			//set the text and convert probabilities to percentages
			getCircuitFrame().getProbLabels()[1].setText("<html>"+
					getCircuitFrame().getStrRep()[index]+": "+
					String.format("%.3f", probs[0] * 100)+"%<br>"+
					getCircuitFrame().getStrRep()[index+1]+": "+
					String.format("%.3f", probs[1] * 100)+"%");
		}
		//check if the measure label was pressed
		else if((JLabel)e.getSource() == getCircuitFrame().getProbLabels()[3]) {
			//check if qubit state is collapsed
			if(getCircuitFrame().getCircuit().getQubit().isCollapsed())
				JOptionPane.showMessageDialog(getCircuitFrame(), 
						"State has been collapsed!\nRevert your measurement"+
						"using the red button to measure again.",
						"Measurement error", JOptionPane.ERROR_MESSAGE);
			else {
				//store current state as previous state in circuit
				getCircuitFrame().getCircuit().setPrevState(getCircuitFrame().getCircuit().getQubit());
				//get the qubit in the circuit and measure its state
				StateVector state = getCircuitFrame().getCircuit().getQubit().measure();
				//get the basis
				Basis basis = Basis.COMPUTATIONAL;
				if(getCircuitFrame().getBasisButton().isSelected())
					basis = Basis.HADAMARD;
				//check which string representation to display
				//- default first string
				int index = 0;
				if(state.equals(basis.getState2())) 
					index = 1;
				//second pair of string representations
				if(getCircuitFrame().getBasisButton().isSelected())
					index += 2;
				
				//display the measured state
				getCircuitFrame().getProbLabels()[3].setText("<html>Measured State:<br>"+
						getCircuitFrame().getStrRep()[index]+"<br>Decimal Values:<br>["+
						String.format("%.3f",state.getComponent1())+","+
						String.format("%.3f",state.getComponent2())+"]</html");
				//update the collapsed boolean attribute
				getCircuitFrame().getCircuit().getQubit().setCollapsed(true);
			}
		}
		//check if any gate labels were clicked
		else if(!getCircuitFrame().getResultsLabels().isEmpty()) {
			//check which label was clicked
			for(int i = 0; i < getCircuitFrame().getResultsLabels().size(); i++) {
				if((JLabel)e.getSource() == getCircuitFrame().getResultsLabels().get(i)) {
					//get gate objectaccording to the name
					Gate gate = AssetsController.getGateMap().
							get(getCircuitFrame().getResultsLabels().get(i).getText());
					//add gate object to the circuit object's gate array
					getCircuitFrame().getCircuit().getGates().add(gate);
					//from the gate, get the image path
					JLabel label = new JLabel(new ImageIcon("src/main/"+gate.getImagePath()));
					//add label to the panel
					getCircuitFrame().getGatePanel().add(label);
					//reload panel
					getCircuitFrame().getGatePanel().repaint();
					getCircuitFrame().getGatePanel().revalidate();
					//break out of loop
					break;
				}
			}
		}
	}
	
	//tool bar buttons
	@Override
	public void actionPerformed(ActionEvent e) {
		//check which button was pressed
		if(e.getSource() == getCircuitFrame().getButtons()[0]) {
			//home frame and dispose current
			new HomeFrame();
			getCircuitFrame().dispose();
		}
		else if(e.getSource() == getCircuitFrame().getButtons()[1]) {
			//check if circuit name is empty or the default name
			String name = getCircuitFrame().getNameField().getText().trim();
			if(name.isEmpty() || name.equals("Enter Circuit Name")) {
				//show error message
				JOptionPane.showMessageDialog(getCircuitFrame(), 
						"Name your circuit before saving!"+
						"using the red button to measure again.",
						"Measurement error", JOptionPane.ERROR_MESSAGE);
			}
			else {
				//save the names of the current gates
				ArrayList<String> gateNames = new ArrayList<String>();
				for(Gate gate : getCircuitFrame().getCircuit().getGates()) {
					gateNames.add(gate.getName());
				}
				//call method from assets controller
				AssetsController.saveCircuitToFile(name, getCircuitFrame()
						.getCircuit().getQubit(), gateNames);
			}
		}
		else if(e.getSource() == getCircuitFrame().getButtons()[2]) {
			//import frame and dispose current
			new CircuitImportFrame();
			getCircuitFrame().dispose();
		}
		else if(e.getSource() == getCircuitFrame().getButtons()[3]) {
			//revert to previous state
			getCircuitFrame().getCircuit().setQubit(
					getCircuitFrame().getCircuit().getPrevState());
			//clear probability panel labels
			getCircuitFrame().getProbLabels()[1].setText("Press to calculate probabilities");
			getCircuitFrame().getProbLabels()[3].setText("Press to measure state");
		}
	}
	
	//unused methods below
	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
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
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
	}
}

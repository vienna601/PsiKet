package view;

import java.util.ArrayList;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Comparator;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.ImageIcon;

import controller.AssetsController;
import model.Basis;
import model.Circuit;
import model.ComplexNumber;
import model.Gate;
import model.Qubit;
import model.StateVector;

/*
 * This program is responsible for configuring the layout of
 * the frame where the user can visualize quantum circuits. It
 * contains features such as gate search and state measurement.
 */
public class CircuitFrame extends JFrame{
	//declare components
	//for checking if frame has been disposed
	private boolean isDisposed = false;
	private JLabel bgLabel;
	//button tool bar at the top
	private JToolBar buttonBar;
	private JButton[] buttons;
	//circuit section
	private Circuit circuit;
	private JTextField nameField;
	private CircuitPanel componentPanel;
	private JPanel gatePanel;
	private JScrollPane scroll;
	private JPanel circuitPanel;
	//search panel
	private JTextField searchField;
	private JPanel resultsPanel;
	private ArrayList<JLabel> resultsLabels;
	private JPanel searchPanel;
	//probability panel
	private JRadioButton basisButton;
	private JPanel probPanel;
	private JLabel[] probLabels;
	//used for explaining probability results
	private final String[] strRep = {"|0⟩ = [1,0]","|1⟩ = [0,1]",
			"|+⟩ = [1/√2,1/√2]", "|-⟩ = [1/√2,-1/√2]"};
	//main panels
	private JPanel leftPanel;
	private JPanel rightPanel;
	
	//constructor
	public CircuitFrame() {
		//set up components
		setUp();
		//set text in text field
		nameField.setText(circuit.getName());
	}
	
	//overload constructor for loading circuits from saved file
	public CircuitFrame(String name, String details) {
		setUp();
		//call the create circuit method in the assets controller
		circuit = AssetsController.createCircuit(name, details);
		//set text field text
		nameField.setText(circuit.getName());
		//add all gates from saved circuit to the panel
		//check if gate is null
		for(Gate gate : circuit.getGates()) {
			if(gate!=null)
				addGateToGUI(gate);
		}
	}
	
	//getters and setters for the controller to access components
	public JButton[] getButtons() {
		return buttons;
	}
	public void setButtons(JButton[] buttons) {
		this.buttons = buttons;
	}
	public Circuit getCircuit() {
		return circuit;
	}
	public void setCircuit(Circuit circuit) {
		this.circuit = circuit;
	}
	public JTextField getNameField() {
		return nameField;
	}
	public void setNameField(JTextField nameField) {
		this.nameField = nameField;
	}
	public JPanel getGatePanel() {
		return gatePanel;
	}
	public void setGatePanel(JPanel gatePanel) {
		this.gatePanel = gatePanel;
	}
	public JTextField getSearchField() {
		return searchField;
	}
	public void setSearchField(JTextField searchField) {
		this.searchField = searchField;
	}
	public JPanel getResultsPanel() {
		return resultsPanel;
	}
	public void setResultsPanel(JPanel resultsPanel) {
		this.resultsPanel = resultsPanel;
	}
	public ArrayList<JLabel> getResultsLabels() {
		return resultsLabels;
	}
	public void setResultsLabels(ArrayList<JLabel> resultsLabels) {
		this.resultsLabels = resultsLabels;
	}
	public JRadioButton getBasisButton() {
		return basisButton;
	}
	public void setBasisButton(JRadioButton basisButton) {
		this.basisButton = basisButton;
	}
	public JLabel[] getProbLabels() {
		return probLabels;
	}
	public void setProbLabels(JLabel[] probLabels) {
		this.probLabels = probLabels;
	}
	public String[] getStrRep() {
		return strRep;
	}
	public JToolBar getButtonBar() {
		return buttonBar;
	}
	public void setButtonBar(JToolBar buttonBar) {
		this.buttonBar = buttonBar;
	}
	public boolean isDisposed() {
		return isDisposed;
	}
	public void setDisposed(boolean isDisposed) {
		this.isDisposed = isDisposed;
	}

	//method for initializing GUI components 
	//(will be used in both constructors)
	public void setUp() {
		//create background label
		bgLabel = new JLabel(new ImageIcon("src/main/assets/images/circuitBg.png"));
		
		//create button bar
		//source: https://docs.oracle.com/javase/tutorial/uiswing/components/toolbar.html
		buttonBar = new JToolBar();
		//add a visual indicator for the buttons
		buttonBar.setRollover(true);
		//allow the tool bar to be moved and dragged
		buttonBar.setFloatable(true);
		//make background transparent
		buttonBar.setOpaque(false);
		
		//initialize buttons on the screen
		buttons = new JButton[4];
		//create array with image file names
		String[] imageNames = {"home", "save", "import","previous"};
		//set images and tool tip of buttons in tool bar based on image names array
		for(int i = 0; i < buttons.length; i++) {
			//create button and set image
			buttons[i] = new JButton(new ImageIcon("src/main/assets/images/"+
						imageNames[i]+"Button.png"));
			buttons[i].setSize(new Dimension(50,50));
			//set tool tip
			buttons[i].setToolTipText(imageNames[i]);
			//set focusable to false so that the space bar won't trigger a button click
			buttons[i].setFocusable(false);
			
			//make button background transparent
			//source: https://stackoverflow.com/questions/4585867/transparent-jbutton
			buttons[i].setOpaque(false);
			buttons[i].setContentAreaFilled(false);
			buttons[i].setBorderPainted(false);
			buttons[i].setFocusPainted(false);
			
			//add to tool bar
			getButtonBar().add(buttons[i]);
		}
		
		//create text field
		nameField = new JTextField(40);
		nameField.setFont(new Font("Times New Roman", Font.PLAIN, 30));
		//let user customize name
		nameField.setEditable(true);
		//set maximum size so that box layout (from circuit panel later on)
		//can't stretch the text field to a super big height
		nameField.setMaximumSize(new Dimension(500,100));
		//initialize empty circuit
		circuit = new Circuit("Enter Circuit Name", new Qubit("q0", 
				new ComplexNumber(1/Math.sqrt(2),0), 
				new ComplexNumber(1/Math.sqrt(2),0), false, 
				Basis.COMPUTATIONAL), new ArrayList<Gate>(), 
				null);
		//initialize panel with circuit components
		componentPanel = new CircuitPanel();
		componentPanel.setMaximumSize(new Dimension(800,500));
		//start with no gates added
		gatePanel = new JPanel(new FlowLayout(FlowLayout.LEFT,30,0));
		gatePanel.setOpaque(false);
		//add scroll bar
		scroll = new JScrollPane(gatePanel, 
				JScrollPane.VERTICAL_SCROLLBAR_NEVER,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		//increase scroll speed
		scroll.getHorizontalScrollBar().setUnitIncrement(15);
		//make the scroll pane transparent, including the viewport
		//which is the layer between the scroll pane and the panel
		//Source: https://coderanch.com/t/588650/java/JScrollbar-transparent
		scroll.setOpaque(false);
		scroll.getViewport().setOpaque(false);
		//- keep the component panel opaque
		//add the scrollable gates panel to the component panel
		//and align it with the circuit lines
		componentPanel.setLayout(null);
		scroll.setBounds(150,100,550,110);
		componentPanel.add(scroll);
		
		//create circuit panel
		circuitPanel = new JPanel();
		circuitPanel.setLayout(new BoxLayout(circuitPanel, BoxLayout.Y_AXIS));
		//add components
		circuitPanel.add(nameField);
		//add buffer space
		//source: https://docs.oracle.com/javase/tutorial/uiswing/layout/box.html
		circuitPanel.add(Box.createRigidArea(new Dimension(0,30)));
		circuitPanel.add(componentPanel);
		circuitPanel.setOpaque(false);
		circuitPanel.setPreferredSize(new Dimension(800,800));
		
		//create search bar, similar to creating the text field for setting the name
		searchField = new JTextField(40);
		searchField.setFont(new Font("Times New Roman", Font.PLAIN, 30));
		searchField.setEditable(true);
		searchField.setText("Search quantum gates here");
		//set maximum size to limit width (prevent cutting off first part of text)
		searchField.setMaximumSize(new Dimension(400,100));
		
		//create panel for search results
		resultsPanel = new JPanel();
		resultsPanel.setLayout(new BoxLayout(resultsPanel, BoxLayout.Y_AXIS));
		resultsPanel.setOpaque(false);
		//initialize result labels
		resultsLabels = new ArrayList<JLabel>();
		//create larger panel that contains both the results panel and the search field
		searchPanel = new JPanel(new BorderLayout(10,30));
		searchPanel.add(searchField,BorderLayout.NORTH);
		searchPanel.add(resultsPanel,BorderLayout.CENTER);
		searchPanel.setOpaque(false);
		searchPanel.setPreferredSize(new Dimension(400,800));
		
		//create panel for displaying information about probabilities
		probPanel = new JPanel();
		probPanel.setLayout(new BoxLayout(probPanel, BoxLayout.Y_AXIS));
		//create button for changing basis of qubit
		basisButton = new JRadioButton("<html>Click to change to<br>hadamard basis {|+⟩,|−⟩}</html>");
		basisButton.setFont(new Font("Times New Roman", Font.ITALIC, 20));
		basisButton.setBackground(new Color(0xeaddff));
		basisButton.setOpaque(true);
		//add the button to the panel
		basisButton.setAlignmentX(CENTER_ALIGNMENT);
		probPanel.add(basisButton);
		probPanel.add(Box.createRigidArea(new Dimension(0,20)));
		
		//initialize label array
		probLabels = new JLabel[4];
		//create heading labels and set the default text
		probLabels[0] = new JLabel("Probabilities");
		probLabels[2] = new JLabel("Measure");
		probLabels[1] = new JLabel("Press to calculate probabilities");
		probLabels[3] = new JLabel("Press to measure state");
		//set the font and add to panel
		for(int i = 0; i < probLabels.length; i++) {
			//check if the current label is a heading label or results label
			if(i%2 == 0)
				//heading label formatting
				probLabels[i].setFont(new Font("Times New Roman", Font.BOLD, 30));
			else {
				//result label formatting
				probLabels[i].setFont(new Font("Times New Roman", Font.PLAIN, 20));
				
				//change to a hand cursor when the user hovers over the label
	            //source: https://stackoverflow.com/questions/7359189/how-to-
				//change-the-mouse-cursor-in-java
				probLabels[i].setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			}
			//check if label is in top or bottom section
			if(i < 2)
				probLabels[i].setBackground(new Color(0xffd3d3));
			else
				probLabels[i].setBackground(new Color(0xace5ff));
			probLabels[i].setOpaque(true);
			//add the label to the panel
			probLabels[i].setAlignmentX(CENTER_ALIGNMENT);
			probPanel.add(probLabels[i]);
			//add buffer space
			//source: https://docs.oracle.com/javase/tutorial/uiswing/layout/box.html
			probPanel.add(Box.createRigidArea(new Dimension(0,20)));
		}
		probPanel.setPreferredSize(new Dimension(300,600));
		probPanel.setOpaque(false);
		
		//putting everything together
		//left side: button bar and circuit panel
		//- button bar dragging only works when the layout is border layout
		//- source: https://docs.oracle.com/javase/tutorial/uiswing/components/toolbar.html
		leftPanel = new JPanel(new BorderLayout(10,50));
		leftPanel.setPreferredSize(new Dimension(800,1000));
		leftPanel.add(circuitPanel, BorderLayout.CENTER);
		leftPanel.add(buttonBar, BorderLayout.NORTH);
		leftPanel.setOpaque(false);
		
		//right side: gate search panel and probability panel
		rightPanel = new JPanel();
		rightPanel.setLayout(new FlowLayout(FlowLayout.LEFT,50,0));
		rightPanel.add(searchPanel);
		rightPanel.add(probPanel);
		rightPanel.setOpaque(false);
		
		//add to background label
		bgLabel.setLayout(null);
		leftPanel.setBounds(0,0,800,1000);
		rightPanel.setBounds(750,200,800,600);
		bgLabel.add(leftPanel);
		bgLabel.add(rightPanel);
		add(bgLabel);
		
		//set the frame's cursor to the custom cursor
		setCursor(AssetsController.getCursor());
		
		//set the properties of the frame
        setTitle("Circuits");
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
	
	//add gates to the GUI JPanel
	public void addGateToGUI(Gate gate) {
		System.out.println(gate.getImagePath());
		//add the gate as an image button to the panel
		JLabel newGate = new JLabel(new ImageIcon("src/main/"+gate.getImagePath()));
		newGate.setPreferredSize(new Dimension(100,100));
		gatePanel.add(newGate);
		//reload panel
		gatePanel.revalidate();
		gatePanel.repaint();
	}
	
}

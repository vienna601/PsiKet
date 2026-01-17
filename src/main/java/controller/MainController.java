package controller;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Map.Entry;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import view.AlgorithmFrame;
import view.CircuitImportFrame;
import view.HomeFrame;
import view.ResourcesFrame;
import view.TitleFrame;

/*
 * This program is responsible for driving the entire program. Other view
 * classes and controllers are its attributes and the main logic for frames
 * (except for bloch and circuit) is stored here.
 */
public class MainController implements MouseListener, ActionListener{
	//declare fields
	//controllers
	private AssetsController assetsCtrl;
	private BlochController blochCtrl;
	private CircuitController circuitCtrl;
	//frames
	private TitleFrame titleFrame;
	private HomeFrame homeFrame;
	private AlgorithmFrame algoFrame;
	private ResourcesFrame resFrame;
	private CircuitImportFrame importFrame;
	
	//getters and setters
	public AssetsController getAssetsCtrl() {
		return assetsCtrl;
	}

	public void setAssetsCtrl(AssetsController assetsCtrl) {
		this.assetsCtrl = assetsCtrl;
	}

	public BlochController getBlochCtrl() {
		return blochCtrl;
	}

	public void setBlochCtrl(BlochController blochCtrl) {
		this.blochCtrl = blochCtrl;
	}

	public CircuitController getCircuitCtrl() {
		return circuitCtrl;
	}

	public void setCircuitCtrl(CircuitController circuitCtrl) {
		this.circuitCtrl = circuitCtrl;
	}

	public TitleFrame getTitleFrame() {
		return titleFrame;
	}
	public CircuitImportFrame getImportFrame() {
		return importFrame;
	}

	//initialize and set up frames
	public void setImportFrame() {
		this.importFrame = new CircuitImportFrame();
		//add mouse listeners
		getImportFrame().getHomeButton().addMouseListener(this);
		getImportFrame().getBackButton().addMouseListener(this);
		//loop through the circuit info map in the import frame 
		//object and create JLabels for displaying saved circuitss
		//Source: https://stackoverflow.com/questions/1318980/how-to-iterate-over-a-treemap
		for(Entry<String, String> entry : getImportFrame().getCircuitInfo().entrySet()) {
			//add buffer space
			getImportFrame().getInfoPanel().add(Box.createRigidArea(new Dimension(0,30)));
			//set label text to contain key and value of map entry
			JLabel label = new JLabel("<html>Circuit name: "+entry.getKey()+
					"<br>Click to import!</html>");
			label.setFont(new Font("Times New Roman",Font.PLAIN, 20));
			label.setBackground(Color.white);
			label.setOpaque(true);
			label.addMouseListener(this);
			//change to a hand cursor when the user hovers over the label
	        //source: https://stackoverflow.com/questions/7359189/how-to-
			//change-the-mouse-cursor-in-java
			label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			getImportFrame().getInfoLabels().add(label);
			//add to panel
			label.setAlignmentX(Component.CENTER_ALIGNMENT);
			getImportFrame().getInfoPanel().add(label);
		}
	}
	
	public void setTitleFrame() {
		//create title frame as it should be the first screen
		this.titleFrame = new TitleFrame();
		//add mouse listener
		getTitleFrame().getHomeButton().addMouseListener(this);
	}

	public HomeFrame getHomeFrame() {
		return homeFrame;
	}

	public void setHomeFrame() {
		this.homeFrame = new HomeFrame();
		//add mouse listeners
		getHomeFrame().getTitleButton().addMouseListener(this);
		for(int i = 0; i < getHomeFrame().getNavPanels().length; i++) {
        	getHomeFrame().getNavPanels()[i].addMouseListener(this);
		}
	}

	public AlgorithmFrame getAlgoFrame() {
		return algoFrame;
	}

	public void setAlgoFrame() {
		this.algoFrame = new AlgorithmFrame();
		//add mouse listeners
		getAlgoFrame().getHomeButton().addMouseListener(this);
		getAlgoFrame().getBackButton().addMouseListener(this);
		getAlgoFrame().getNextButton().addMouseListener(this);
	}

	public ResourcesFrame getResFrame() {
		return resFrame;
	}

	public void setResFrame() {
		this.resFrame = new ResourcesFrame();
		//add mouse listeners
		getResFrame().getHomeButton().addMouseListener(this);
		//add mouse listener to label for redirecting the user to the corresponding website
        //(can't use action listener on JLabel)
		for(JLabel label : getResFrame().getResourceLabels())
			label.addMouseListener(this);
	}
	
	//constructor
	public MainController() {
		setTitleFrame();
		//initialize controllers
		assetsCtrl = new AssetsController();
		blochCtrl = new BlochController();
		circuitCtrl = new CircuitController();
	}
	
	//method for opening resource website on user's default browser
    public void openWebsite(URI uri) {
        //determine whether desktop API is available for opening websites
        //https://stackoverflow.com/questions/9977221/open-url-in-java-to-get-the-content
        if (Desktop.isDesktopSupported()) {
            //try catch structure for browsing uri
            try {
                //open the website using the desktop 
                Desktop.getDesktop().browse(uri);
            } 
            catch (IOException e) {
                e.printStackTrace(); 
            }
        } 
    }
	
	@Override
	public void mouseClicked(MouseEvent e) {
		//check if a JPanel was clicked
		//- check if home frame has been instantiated/not disposed
		if(getHomeFrame()!=null && !getHomeFrame().isDisposed()&&
				e.getSource() instanceof JPanel) {
			//in the home frame
			//if the user clicked the first panel
			if((JPanel)e.getSource() == getHomeFrame().getNavPanels()[0]) {
				//open the bloch frame and dispose the current frame
				getBlochCtrl().setBlochFrame();
				getBlochCtrl().getBlochFrame().getHomeButton().addMouseListener(this);
				//check if frame has been disposed
				if(!getHomeFrame().isDisposed())
					getHomeFrame().dispose();
			}
			//if the user clicked the second panel
			else if((JPanel)e.getSource() == getHomeFrame().getNavPanels()[1]) {
				//open the circuit frame and dispose the current frame
				getCircuitCtrl().setCircuitFrame();
				getCircuitCtrl().getCircuitFrame().getButtons()[0].addActionListener(this);
				getCircuitCtrl().getCircuitFrame().getButtons()[2].addActionListener(this);
				//check if frame has been disposed
				if(!getHomeFrame().isDisposed())
					getHomeFrame().dispose();
			}
			//if the user clicked the third panel
			else if((JPanel)e.getSource() == getHomeFrame().getNavPanels()[2]) {
				//open the algorithm frame and dispose the current frame
				setAlgoFrame();
				//check if frame has been disposed
				if(!getHomeFrame().isDisposed())
					getHomeFrame().dispose();
			}
			//if the user clicked the last panel
			else if((JPanel)e.getSource() == getHomeFrame().getNavPanels()[3]) {
				//open the resources frame and dispose the current frame
				setResFrame();
				//check if frame has been disposed
				if(!getHomeFrame().isDisposed())
					getHomeFrame().dispose();
			}
		}else {
			//check if title frame has been instantiated/not disposed and
			//check if user wants to proceed to the next page
			if(getTitleFrame()!=null && !getTitleFrame().isDisposed() &&
					(JLabel)e.getSource() == getTitleFrame().getHomeButton()) {
				//construct a home frame and dispose the current
				setHomeFrame();
				getTitleFrame().dispose();
			}
			//check which frame the user wants to navigate to
			//if the user clicked the title button
			//- check if home frame has been instantiated/not disposed
			else if(getHomeFrame()!=null && !getHomeFrame().isDisposed() &&
					(JLabel)e.getSource() == getHomeFrame().getTitleButton()) {
				//open the title frame and dispose the current frame
				setTitleFrame();
				//check if frame has been disposed
				if(!getHomeFrame().isDisposed())
					getHomeFrame().dispose();
			}
			//check if bloch frame has been instantiated/not disposed
			if(getBlochCtrl().getBlochFrame()!=null &&!getBlochCtrl().getBlochFrame().isDisposed()) {
				//check if the user clicked the home button label
				if((JLabel)e.getSource() == getBlochCtrl().getBlochFrame().getHomeButton()) {
					//open the home frame and dispose the current
					setHomeFrame();
					getBlochCtrl().getBlochFrame().dispose();
				}
			}
			//check if import frame has been instantiated/not disposed
			if(getImportFrame()!=null && !getImportFrame().isDisposed()) {
				if((JLabel)e.getSource() == getImportFrame().getHomeButton()) {
					setHomeFrame();
					getImportFrame().dispose();
				}
				else if((JLabel)e.getSource() == getImportFrame().getBackButton()) {
					//open the circuit frame and dispose the current frame
					getCircuitCtrl().setCircuitFrame();
					getCircuitCtrl().getCircuitFrame().getButtons()[0].addActionListener(this);
					getCircuitCtrl().getCircuitFrame().getButtons()[2].addActionListener(this);
					getImportFrame().dispose();
				}
				else {
					//check if an info label was clicked
					for(int i = 0; i < getImportFrame().getInfoLabels().size(); i++) 
						//check if label was clicked
						if((JLabel)e.getSource() == getImportFrame().getInfoLabels().get(i)) {
							//get list of keys to get key value pair using index
							//Source: https://www.tutorialspoint.com/how-to-get-
							//treemap-key-or-value-using-index-in-java
							ArrayList<String>keyList = new ArrayList(getImportFrame().getCircuitInfo().keySet());
							String key = keyList.get(i);
							//pass the key and value to the circuit frame constructor and
							//dispose the current frame
							getCircuitCtrl().setCircuitFrame(key,getImportFrame().getCircuitInfo().get(key));
							getImportFrame().dispose();
							//show success message
							JOptionPane.showMessageDialog(getImportFrame(), 
									"Circuit Imported!",
									"Import success", JOptionPane.INFORMATION_MESSAGE);
						}
				}
			}
			//check if algorithms frame has been instantiated/not disposed
			if(getAlgoFrame()!=null && !getAlgoFrame().isDisposed()) {
				//check if the home button was clicked
				if((JLabel)e.getSource() == getAlgoFrame().getHomeButton()) {
					//dispose current frame and open home
					setHomeFrame();
					getAlgoFrame().dispose();
				}
				else if((JLabel)e.getSource() == getAlgoFrame().getBackButton()) {
					//update step number with validation
					getAlgoFrame().setCurStep(Math.max(0, getAlgoFrame().getCurStep()-2));
					//update labels
					getAlgoFrame().getStepLabel().setText(AssetsController.
							getAlgorithmInfo()[getAlgoFrame().getCurStep()]);
					getAlgoFrame().getStepImageLabel().setIcon(new ImageIcon("src/main/assets/images/step"+
					getAlgoFrame().getCurStep()/2+".gif"));
					getAlgoFrame().getExplanationLabel().setText(AssetsController.
							getAlgorithmInfo()[getAlgoFrame().getCurStep()+1]);
					//reload 
					getAlgoFrame().getStepImageLabel().revalidate();
				}
				else if((JLabel)e.getSource() == getAlgoFrame().getNextButton()) {
					//update step number with validation
					getAlgoFrame().setCurStep(Math.min(10, getAlgoFrame().getCurStep()+2));
					//update labels
					getAlgoFrame().getStepLabel().setText(AssetsController.
							getAlgorithmInfo()[getAlgoFrame().getCurStep()]);
					getAlgoFrame().getStepImageLabel().setIcon(new ImageIcon("src/main/assets/images/step"+
					getAlgoFrame().getCurStep()/2+".gif"));
					getAlgoFrame().getExplanationLabel().setText(AssetsController.
							getAlgorithmInfo()[getAlgoFrame().getCurStep()+1]);
					//reload 
					getAlgoFrame().getStepImageLabel().revalidate();
				}
			}
			//check if resources frame has been instantiated/not disposed
			if(getResFrame()!=null && !getResFrame().isDisposed()) {
				//check if the user clicked the home button
				if((JLabel)e.getSource() == getResFrame().getHomeButton()) {
					//open the home frame and dispose the current
					setHomeFrame();
					getResFrame().dispose();
				}
				//check if the user clicked any of the resource labels
				else {
					for(int i = 0; i < getResFrame().getResourceLabels().size(); i++) {
						//check if any of the labels were clicked
						if((JLabel)e.getSource() == getResFrame().getResourceLabels().get(i)) {
							//try catch structure for browsing url
			                try{
			                    //open the corresponding website
			                    openWebsite(new URI(getResFrame().getURIList().get(i)));
			                } catch (Exception ex){
			                    ex.printStackTrace();
			                }
							
						}
					}
				}
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		//check if circuit frame has been instantiated/not disposed
		if(getCircuitCtrl().getCircuitFrame()!=null && !getCircuitCtrl().getCircuitFrame().isDisposed()) {
			//check which button was pressed
			if(e.getSource() == getCircuitCtrl().getCircuitFrame().getButtons()[0]) {
				//home frame and dispose current
				setHomeFrame();
				getCircuitCtrl().getCircuitFrame().dispose();
			}
			else if(e.getSource() == getCircuitCtrl().getCircuitFrame().getButtons()[2]) {
				//import frame and dispose current
				System.out.println("import");
				setImportFrame();
				getCircuitCtrl().getCircuitFrame().dispose();
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
	
}

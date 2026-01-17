package controller;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Stack;
import java.util.TreeMap;

import javax.imageio.ImageIO;

import model.Basis;
import model.Circuit;
import model.ComplexNumber;
import model.Gate;
import model.Qubit;

/*
 * This controller is responsible for helping load contents
 * such as gates and saved circuits.
 */

public class AssetsController {
	//fields
	//gate hashmap for getting corresponding gate objects with their names
	//- static as it will be accessed in the circuit frame for gate search
	public static HashMap<String,Gate> gateMap;
	//static since it will be used in all frames
	private static Cursor cursor;
	//for checking duplicate names
	public static ArrayList<String> circuitNames;
	//for displaying explanations of each step in the algorithm
	public static String[] algorithmInfo;
	
	//constructor
	public AssetsController() {
		super();
		//call setter to load gates
		setGateMap();
		setCursor();
		setAlgorithmInfo();
		setCircuitNames();
	}
	
	//getters and setters
	public static ArrayList<String> getCircuitNames() {
		return circuitNames;
	}

	public static void setCircuitNames() {
		//initialize array list
		circuitNames = new ArrayList<String>();
		//call load circuits method to populate array list
		loadCircuits();
	}

	public static HashMap<String, Gate> getGateMap() {
		return gateMap;
	}
	
	public static void setGateMap() {
		//initialize gate hashmap
		gateMap = new HashMap<String,Gate>();
		//load gates from file
		try {
			//create scanner for file
			//gate source: https://www.mathworks.com/help/matlab/math/types-of-quantum-gates.html
			Scanner sc = new Scanner(new File("src/main/assets/file/gates.txt"));
			//use commas as the separating factor
			sc.useDelimiter(",|\r\n");
			
			//loop until the end of the file
			while(sc.hasNextLine()) {
				//get attributes for gate object
				String name = sc.next().trim();
				String path = sc.next();
				//create matrix representation
				ComplexNumber[][] matrixRep = new ComplexNumber[2][2];
				//all the gates I'm using will be 2x2
				for(int i = 0; i < 2; i++)
					for(int j = 0; j < 2; j++) {
						//check if the first amplitude contains "sqrt2"
						String num = sc.next();
						double value;
						if(num.contains("sqrt2")) {
							value = Math.sqrt(2);
							//check if it is negative
							if(num.contains("-"))
								value *= -1;
						}
						else {
							//keep original value
							value = Double.parseDouble(num);
						}
						//parse string representation of the real and imaginary parts
						matrixRep[i][j] = new ComplexNumber(
								value, Double.parseDouble(sc.next()));
					}
				//create gate and add it to the hashmap
				gateMap.put(name, new Gate(name, path, matrixRep));
			}//end of while loop
			sc.close();
		} catch (FileNotFoundException e) {
			//print error
			e.printStackTrace();
		}
	}
	
	public static Cursor getCursor() {
		return cursor;
	}

	public static void setCursor() {
		//create a custom cursor for the application
		//Sources: 
		//- https://stackoverflow.com/questions/51333423/
		//how-to-create-a-large-size-custom-cursor-in-java
		//- https://www.geeksforgeeks.org/java/java-awt-cursor-class-with-examples/
		try {
			//load the image for the custom cursor
			BufferedImage image = ImageIO.read(new File("src/main/assets/images/cursor.png"));
			//define hotspot (the pixel that acts as the cursor's clicking point)
			Point hotspot = new Point(16,16);
			//make the custom cursor
			cursor = Toolkit.getDefaultToolkit().
					createCustomCursor(image, hotspot, "custom cursor");
		} catch (IOException e) {
			//print error
			e.printStackTrace();
		}
	}

	public static String[] getAlgorithmInfo() {
		return algorithmInfo;
	}

	public static void setAlgorithmInfo() {
		//initialize gate hashmap
		algorithmInfo = new String[12];
		//load gates from file
		try {
			//create scanner for file
			//grover's algorithm info source: https://www.youtube.com/watch?v=RQWpF2Gb-gU
			Scanner sc = new Scanner(new File("src/main/assets/file/explanation.txt"));
			//use squiggly line as the separating factor (div formatting uses semi colon)
			sc.useDelimiter("~|\r\n");
			
			//loop until the end of the file
			for(int i = 0; i < algorithmInfo.length; i++) {
				//store trimmed string from txt file into array
				algorithmInfo[i] = sc.next().trim();
			}
			//close scanner
			sc.close();
		} catch (FileNotFoundException e) {
			//print error
			e.printStackTrace();
		}
	}

	//save circuit information to file
	//the info will be passed from GUI
	public static void saveCircuitToFile(String name, Qubit initial, ArrayList<String> gateNames) {
		try {
			//create file writer and buffered reader for appending information
			FileWriter fw = new FileWriter("src/main/assets/file/circuits.txt", true);
			BufferedWriter bw = new BufferedWriter(fw);
			//construct string using semicolon as delimiter
			String info = name + ";" + initial.toString() + ";";
			//iterate through gate name arraylist
			for(String gateName : gateNames)
				//separate names by commas so that I can easily split them when loading
				//circuit information
				info += gateName + ",";
			
			//write to file
		    bw.write(info);
		    //start new line for next write
		    bw.newLine();
		    bw.close();
		} catch (IOException e) {
			//print error
			e.printStackTrace();
		}
	}

	//loading and sorting circuit info
	//I will use a treemap since it automatically sorts keys in alphabetical order
	public static TreeMap<String, String> loadCircuits() {
		//initialize a treemap (string key and string value) for storing circuit information
		TreeMap<String, String> circuitInfo = new TreeMap<String, String>();
		//try catch for scanning file
		try {
			//create scanner for file
			Scanner sc = new Scanner(new File("src/main/assets/file/circuits.txt"));
			//use semi colons as the delimiter since commas 
			//are used in array string representations
			sc.useDelimiter(";|\r\n");
			
			//clear circuit name array to update it
			if(circuitNames == null)
				circuitNames = new ArrayList<String>();
			
			//loop until the end of the file
			while(sc.hasNextLine()) {
				//store circuit information into the treemap
				String name = sc.next();
				//concatenate information about the qubit and gates
				//source: https://www.baeldung.com/java-string-concatenation
				String details1 = sc.next();
				String details2 = sc.next();
				//add name to list of circuit names for checking duplicates
				circuitNames.add(name);
				circuitInfo.put(name, details1 + "~" + details2);
				//circuit objects will only be loaded when actually 
				//selected on the circuit import screen
			}
			sc.close();
		} catch (FileNotFoundException e) {
			//print error
			e.printStackTrace();
		}
		return circuitInfo;
	}
	
	//recreate selected circuit 
	//parameter is essentially an entry of the treemap
	public static Circuit createCircuit(String name, String details) {
		//split circuit details into qubit object and gate name array
		String[] parts = details.split("~");
		System.out.println(details);
		//separate qubit string representation into its attributes
		String[]qubitParts = parts[0].split(",");
		//get indices where the real and imaginary parts are separated
		int index1 = qubitParts[1].indexOf(":");
		int index2 = qubitParts[2].indexOf(":");
		//since there are limited bases that I will work with,
		//I can just check which one the qubit uses
		//set default as the computational basis
		Basis basis = Basis.COMPUTATIONAL;
		if(qubitParts[4].equals("HADAMARD"))
			basis = Basis.HADAMARD;
		
		//create qubit object
		Qubit qubit = new Qubit(qubitParts[0], 
				//use the indices from earlier
				new ComplexNumber(Double.parseDouble(qubitParts[1].substring(0,index1)), 
						Double.parseDouble(qubitParts[1].substring(index1 + 1, 
								qubitParts[1].length()))),
				//same for the second amplitude attribute
				new ComplexNumber(Double.parseDouble(qubitParts[2].substring(0,index2)), 
						Double.parseDouble(qubitParts[1].substring(index2 + 1, 
								qubitParts[2].length()))),
				Boolean.parseBoolean(qubitParts[3]), basis);
		
		//get array of gate names
		String[] gateParts = parts[1].split(",");
		//declare array of gates
		ArrayList<Gate> gateList = new ArrayList<Gate>();
		
		//check if gate map has been initialized
		if(getGateMap()==null)
			setGateMap();
		//loop through the array of gate names
		for(String curName : gateParts) {
			//check if string is empty
			if(!curName.isEmpty())
				//use name of gate to get corresponding gate 
				//object from hashmap
				gateList.add(gateMap.get(curName));
		}
		
		//create the circuit and return it
		//- use empty stack for previous states since they are only stored for
		//  the current session
		return new Circuit(name, qubit, gateList, null);
	}
}

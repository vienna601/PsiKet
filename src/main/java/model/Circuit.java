package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;

public class Circuit {
	//fields
	private String name;
	private Qubit qubit;
	private ArrayList<Gate> gates;
	private Qubit prevState;
	
	//constructor
	public Circuit(String name, Qubit qubit, ArrayList<Gate> gates, Qubit prevState) {
		super();
		this.name = name;
		this.qubit = qubit;
		this.gates = gates;
		this.prevState = prevState;
	}
	
	//getters and setters
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Qubit getQubit() {
		return qubit;
	}
	public void setQubit(Qubit qubit) {
		this.qubit = qubit;
	}
	public ArrayList<Gate> getGates() {
		return gates;
	}
	public void setGates(ArrayList<Gate> gates) {
		this.gates = gates;
	}
	public Qubit getPrevState() {
		return prevState;
	}

	public void setPrevState(Qubit prevState) {
		this.prevState = prevState;
	}

	//display contents
	@Override
	public String toString() {
		return "Circuit [name=" + name + ", qubit=" + qubit + ", gates=" + gates + ", prevStates=" + prevState + "]";
	}
	
	//utility methods
	public void clearCircuit() {
		//reset components
		setQubit(new Qubit("default", new ComplexNumber(0, 0), 
				new ComplexNumber(0, 0), false, Basis.COMPUTATIONAL));
		setGates(new ArrayList<Gate>());
		setPrevState(null);
	}
	
	public void addGate(Gate newGate) {
		//get current gates and add the new gate
		ArrayList<Gate> curGates = getGates();
		curGates.add(newGate);
		setGates(curGates);
	}
	
	public void removeGate(int index) {
		//remove gate at specific index
		ArrayList<Gate> curGates = getGates();
		curGates.remove(index);
		setGates(curGates);
	}
	
	//shift gates around the circuit
	public void moveGate(int originalIndex, int newIndex) {
		//get current gates
		ArrayList<Gate> curGates = getGates();
		//check if indices are in range
		if(originalIndex < 0 || newIndex < 0 ||
				originalIndex >= curGates.size() || newIndex >= curGates.size())
			return;
		//change position of gate
		Gate target = curGates.remove(originalIndex);
		curGates.add(newIndex, target);
		setGates(curGates);
	}
	
	//run circuit and calculate state
	public void run() {
		for(int i = 0; i < getGates().size(); i++) {
			setQubit(getGates().get(i).calcResult(getQubit()));
		}
	}
}
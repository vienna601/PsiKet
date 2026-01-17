package model;

/*
 * This class defines the two constant basis objects
 * that I will use throughout my project for various qubits.
 */

//Use enum for a set of constants
//SOurce: https://www.baeldung.com/a-guide-to-java-enums
public enum Basis {
	//constants (separated with comma)
	COMPUTATIONAL(new StateVector(1.0,0.0), new StateVector(0.0,1.0)),
	HADAMARD(new StateVector(1/Math.sqrt(2), 1/Math.sqrt(2)), 
			new StateVector(1/Math.sqrt(2), -1/Math.sqrt(2)));
	
	//fields
	//can have more dimensions, but for this project I will
	//work with qubits in the 2 dimensional space
	private final StateVector state1;
	private final StateVector state2;
	
	//constructor
	private Basis(StateVector state1, StateVector state2) {
		this.state1 = state1;
		this.state2 = state2;
	}
	
	//getters and setters
	public StateVector getState1() {
		return state1;
	}
	public StateVector getState2() {
		return state2;
	}
	//toString is not applicable to enums
}

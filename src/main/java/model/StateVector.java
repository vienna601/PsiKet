package model;

public class StateVector {
	//fields
	//vector components in the state (2-dimensions in this case)
	private double component1;
	private double component2;
	
	//constructor
	public StateVector(double component1, double component2) {
		super();
		this.component1 = component1;
		this.component2 = component2;
	}
	
	//getters and setters
	public double getComponent1() {
		return component1;
	}
	public void setComponent1(double component1) {
		this.component1 = component1;
	}
	public double getComponent2() {
		return component2;
	}
	public void setComponent2(double component2) {
		this.component2 = component2;
	}
	
	//display contents
	@Override
	public String toString() {
		return "StateVector [component1=" + component1 + ", component2=" + component2 + "]";
	}
	
	//utility methods
	//compare two state vectors (used in 
	//circuit frame to check which string representation
	//to display)
	public boolean equals(StateVector state) {
		//check if components have same value
		return getComponent1() == state.getComponent1() && 
				getComponent2() == state.getComponent2();
	}
}

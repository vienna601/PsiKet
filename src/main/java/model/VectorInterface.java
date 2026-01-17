package model;

/*
 * This interface ensures that objects associated
 * with vectors implement a method that helps measure
 * the length.
 */
public interface VectorInterface {
	//ensure that the qubit, complex number, and
	//basis objects have a method to measure the length
	public double getMagnitude();
}

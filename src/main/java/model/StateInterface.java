package model;

/*
 * This interface ensures that objects associated
 * with states implement a method that helps normalize
 * the state.
 */
public interface StateInterface {
	//ensure that qubit and basis objects have the normalize
	//method as they contain state vectors
	public void normalize();
}

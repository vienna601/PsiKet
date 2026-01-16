package model;

import java.util.Random;

public class Qubit implements StateInterface, VectorInterface{
	//fields
	private String id;
	private ComplexNumber alpha;
	private ComplexNumber beta;
	private boolean isCollapsed;
	private Basis basis;
	
	//constructor
	public Qubit(String id, ComplexNumber alpha, ComplexNumber beta, 
			boolean isCollapsed, Basis basis) {
		super();
		this.id = id;
		this.alpha = alpha;
		this.beta = beta;
		this.isCollapsed = isCollapsed;
		this.basis = basis;
	}
	
	//getters and setters
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public ComplexNumber getAlpha() {
		return alpha;
	}
	public void setAlpha(ComplexNumber alpha) {
		this.alpha = alpha;
	}
	public ComplexNumber getBeta() {
		return beta;
	}
	public void setBeta(ComplexNumber beta) {
		this.beta = beta;
	}
	public boolean isCollapsed() {
		return isCollapsed;
	}
	public void setCollapsed(boolean isCollapsed) {
		this.isCollapsed = isCollapsed;
	}
	public Basis getBasis() {
		return basis;
	}
	public void setBasis(Basis basis) {
		this.basis = basis;
	}
	
	//display contents
	//customize to string method so that it is easier to parse
	//when converting saved circuits to GUI displays
	@Override
	public String toString() {
		return id + "," + alpha + "," + beta + "," + 
				isCollapsed + "," + basis;
	}

	//utility methods
	//calculate the norm (length) of the qubit, similar to how
	//you would get the modulus (length) of a complex number.
	//- this is useful because after normalizing, the total probability of measurement is 1
	@Override
	public void normalize() {
		//check if magnitude is 0
		if(getMagnitude() == 0)
			//print error message
			System.out.println("ERROR: cannot normalize qubit state of magnitude 0.");
		else {
			//get the magnitude and divide amplitudes
			double magnitude = getMagnitude();
			ComplexNumber newAlpha = getAlpha();
			newAlpha.setRealPart(newAlpha.getRealPart()/magnitude);
			newAlpha.setImaginaryPart(newAlpha.getImaginaryPart()/magnitude);
			//same for beta
			ComplexNumber newBeta = getBeta();
			newBeta.setRealPart(newBeta.getRealPart()/magnitude);
			newBeta.setImaginaryPart(newBeta.getImaginaryPart()/magnitude);
		}
	}

	@Override
	public double getMagnitude() {
		//using the length of the complex number amplitudes in the qubit,
		//we can calculate the norm (length) of the qubit
		return Math.sqrt(Math.pow(getAlpha().getMagnitude(), 2) + 
				Math.pow(getBeta().getMagnitude(), 2));
	}
	
	//calculate measurement probabilities
	public double[] calcProbs() {
		//check if qubit is normalized
		if(getMagnitude() != 1)
			normalize();
		//Using Born's rule, the measurement probabilities of
		//each basis component can be calculated by the
		//squared of the magnitude of the inner product, where
		//- the "bra"(left side) is the basis
		//- the "ket"(right side) is the qubit (amplitudes)
		//Source: https://link.springer.com/article/10.1007/s10773-025-05979-7
		double[] probs = new double[2];
		//loop through number of dimensions
		for(int i = 0; i < 2; i++) 
			//calculate respective probabilities
			probs[i] = Math.pow(innerProduct(i+1).getMagnitude(),2);
		//return results
		return probs;
	}
	
	//measure state of qubit
	public StateVector measure() {
		//normalize qubit so that the norm is 1 (sum of 
		//probabilities will also be 1)
		normalize();
		//get probabilities
		double[] probs = calcProbs();
		
		//generate a random number and check whether it is
		//less than the probability of the first state (if it is,
		//then collapse the qubit to that state)
		//- the higher the probability, the more likely the qubit will
		//  collapse into that state
		Random r = new Random();
		//qubit has collapsed after measurement
		setCollapsed(true);
		if(r.nextDouble() < probs[0]) {
			//change amplitudes to match those of the basis
			setAlpha(new ComplexNumber(getBasis().
					getState1().getComponent1(),0));
			setBeta(new ComplexNumber(getBasis().
					getState1().getComponent2(),0));
			//return the corresponding state
			return getBasis().getState1();
		}
		else {
			//change amplitudes to match those of the basis
			setAlpha(new ComplexNumber(getBasis().
					getState2().getComponent1(),0));
			setBeta(new ComplexNumber(getBasis().
					getState2().getComponent2(),0));
			//return the corresponding state
			return getBasis().getState2();
		}
	}
	
	//compute the inner product
	public ComplexNumber innerProduct(int stateNum) {
		//set default state
		StateVector state = getBasis().getState1();
		//check if it is the other state
		if(stateNum == 2)
			state = getBasis().getState2();
		//no need to compute complex conjugate since I don't have any 
		//bases with complex numbers yet
		
		//no need to check compatibility of computation since
		//the qubit is formed on the basis (all bases are 2x1 so 
		//the rotated is 1x2)
		//- the inner product is calculated with the conjugate transpose
		//  of the basis we are finding the probability for
		//- transpose is basically rotating a vector/matrix
		
		//calculate the inner product and return it
		//Source: https://quantum.cloud.ibm.com/learning/en/courses/
		//basics-of-quantum-information/quantum-circuits/inner-products-and-projections
		return new ComplexNumber(state.getComponent1(),0).multiply(getAlpha()).
				add(new ComplexNumber(state.getComponent2(),0).multiply(getBeta()));
	}
}

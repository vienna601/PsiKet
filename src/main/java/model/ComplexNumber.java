package model;

public class ComplexNumber implements VectorInterface{
	//fields
	private double realPart;
	private double imaginaryPart;
	
	//constructor
	public ComplexNumber(double realPart, double imaginaryPart) {
		super();
		this.realPart = realPart;
		this.imaginaryPart = imaginaryPart;
	}
	
	//getters and setters
	public double getRealPart() {
		return realPart;
	}
	public void setRealPart(double realPart) {
		this.realPart = realPart;
	}
	public double getImaginaryPart() {
		return imaginaryPart;
	}
	public void setImaginaryPart(double imaginaryPart) {
		this.imaginaryPart = imaginaryPart;
	}
	
	//display contents
	//customize to string method so that it is easier to parse
	//when converting saved circuits to GUI displays
	@Override
	public String toString() {
		//I will use colon as the separating factor when loading assets
		return realPart + ":" + imaginaryPart;
	}
	
	//utility methods
	public ComplexNumber add(ComplexNumber num) {
		//add corresponding parts and return new number
		return new ComplexNumber(getRealPart() + num.getRealPart(), 
				getImaginaryPart() + num.getImaginaryPart());
	}
	
	//complex multiplication
	//formula source: https://www.mathsisfun.com/algebra/complex-number-multiply.html
	public ComplexNumber multiply(ComplexNumber num) {
		//get new real part
		double newReal = getRealPart() * num.getRealPart() - 
				getImaginaryPart() * num.getImaginaryPart();
		//get new imaginary part
		double newImaginary = getRealPart() * num.getImaginaryPart() +
				getImaginaryPart() * num.getRealPart();
		//return the new complex number
		return new ComplexNumber(newReal, newImaginary);
	}
	
	public ComplexNumber getConjugate() {
		return new ComplexNumber(getRealPart(), getImaginaryPart() * -1);
	}
	
	//calculate modulus (length) of the complex number
	@Override
	public double getMagnitude() {
		//distance from the origin on the complex plane
		return Math.sqrt(Math.pow(getRealPart(), 2) + 
				Math.pow(getImaginaryPart(), 2));
	}
	
}

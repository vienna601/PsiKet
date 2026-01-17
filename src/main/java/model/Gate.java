package model;

import java.util.Arrays;

/*
 * This class models gates with a 2D complex number
 * array as its matrix representation and information such as 
 * its name and image path.
 */
public class Gate {
	//fields
	private String name;
	private String imagePath;
	private ComplexNumber[][] matrixRep;
	
	//constructor
	public Gate(String name, String imagePath, ComplexNumber[][] matrixRep) {
		super();
		this.name = name;
		this.imagePath = imagePath;
		this.matrixRep = matrixRep;
	}

	//getters and setters
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public ComplexNumber[][] getMatrixRep() {
		return matrixRep;
	}

	public void setMatrixRep(ComplexNumber[][] matrixRep) {
		this.matrixRep = matrixRep;
	}

	//display contents
	@Override
	public String toString() {
		return "Gate [name=" + name + ", imagePath=" + imagePath + 
				", matrixRep=" + Arrays.toString(matrixRep) + "]";
	}
	
	//utility methods
	public Qubit calcResult(Qubit qubit) {
		//check if qubit and gate are compatible
		ComplexNumber[][] gate = getMatrixRep();
		ComplexNumber[] qubitRep = {qubit.getAlpha(), qubit.getBeta()};
		//since qubits are always 2x1, there's no need to get the length
		if(gate[0].length != 2) 
			//display error message
			System.out.println("Invalid operation with: "+getName());
		else {
			//declare resulting 2x1 matrix
			ComplexNumber[][] result = new ComplexNumber[2][1];
			//matrix multiplication
			for(int i = 0; i < gate.length; i++) 
				//repeats until the end of the current column is reached
				for(int j = 0; j < gate[0].length; j++) {
					//get corresponding values and compute new complex number
					ComplexNumber newNum = gate[i][j].multiply(qubitRep[j]);
					//check if the value in the result array is null
					if(result[i][j] != null)
						result[i][j] = result[i][j].add(newNum);
					else
						result[i][j] = newNum;
				}//end of inner for loop
		}
		return qubit;
	}

}

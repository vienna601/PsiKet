package application;

import controller.MainController;
import view.TitleFrame;

/*
 * Name: Vienna Zhao
 * Date: 16th January 2026
 * Course Code: ICS4U1-01 Mr.Fernandes
 * Title: PsiKet
 * 
 * Description: 
 * This project is a quantum computing visualization tool aimed to help 
 * intermediate quantum learners gain a better understanding how qubits 
 * behave with quantum gates, which are fundamental operations, and how 
 * famous quantum algorithms such as Grover’s algorithm work behind the scenes. 
 * It utilizes a sphere and circuit GUI to demonstrate the characteristics 
 * of qubits including superposition, as well as the effects of various 
 * gate operations on them. This application is unique because it links 
 * three crucial representations together, with hardware components in the 
 * circuit diagram, geometric concepts in the Bloch sphere, and algebra in 
 * state measurement probabilities. Most existing tools only focus on one 
 * aspect, overlooking the importance of connecting fundamentals across 
 * different models of quantum computing.
 * 
 * Features:
 * - Background music
 * - Custom cursor
 * - 3D bloch sphere for visualizing qubit positions
 * - Calculated cartesian and spherical coordinates
 * - Default pairs of basis states that users can select
 * - Custom angles that control positions of qubits on the bloch sphere
 * - Searching and adding quantum gates to circuit
 * - Modifying basis used in circuit
 * - Calculating measurement probabilities and measuring superposed qubit
 * - Saving current circuit set up
 * - Importing saved circuits
 * - Break down of Grover's Algorithm into major steps with video 
 *   visualizations and explanations (Source: https://www.youtube.com/watch?v=RQWpF2Gb-gU)
 * - Resources and helpful websites for the user to explore
 * 
 * Major Skills:
 * - Modular programming (OOP principles and class hierarchies)
 * - Control structures
 * - Decision structures
 * - MCV file structure
 * - Data structures (e.g. Tree map, Array lists, etc.)
 * - Search algorithm
 * - String manipulation
 * 
 * Areas of Concern:
 * - Please download all assets, APIs, packages, etc.
 * 
 * Additional note:
 * - The project title is inspired by the greek alphabet character psi (ψ)
 *   commonly used to represent quantum states. I combined it with "ket" to reference
 *   the braket notation in quantum mechanics, with ⟨ψ∣ being bra and ∣ψ⟩ being ket.
`*/

/*
 * This program is responsible for starting the application with
 * the main controller class.
 */
public class FSPApplication {
	//main method
	public static void main(String args[]) {
		//create instance of main controller
		new MainController();
	}

}

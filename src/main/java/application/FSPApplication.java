package application;

import controller.MainController;
import view.TitleFrame;

/*
 * Name: Vienna Zhao
 * Date: 16th January 2026
 * Course Code: ICS4U1-01 Mr.Fernandes
 * Title: PsiKet
 * 
 * Description: This project is a quantum computing visualization tool 
 * aimed to help users gain a better understanding how qubits behave 
 * with quantum gates, which are fundamental operations, and how famous 
 * quantum algorithms such as Grover’s algorithm work behind the scenes. 
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
 * - 
 * 
 * Major Skills:
 * - 
 * Areas of Concern:
 * any notes to end users here (files, hardware, settings, imported APIs, instructions, etc.)
 * indicate incomplete or partial solutions (acknowledge errors)
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
		//new MainController();
		new TitleFrame();
	}

}

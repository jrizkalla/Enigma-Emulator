/**
 * 
 */
package enigma.machine;

/**
 * An interface that provides the ability to track changes in the individual steps of the
 * EnigmaMachine encryption
 * The Enigma machine code looks like this:
 * 		> call start
 * 		> rotate rotor3
 * 		> call afterRotate
 * 
 * 		> input := input through plugboard
 * 		> call afterPlugboard1
 * 
 * 		> input := input through rotor3 (right to left)
 * 		> call afterRightToLeftRotor3
 * 		> input := input through rotor2 (right to left)
 * 		> call afterRightToLeftRotor2
 * 		> input := input through rotor1 (right to left)
 * 		> call afterRightToLeftRotor1
 * 
 * 		> input := input through reflector
 * 		> call afterReflector
 * 
 * 		> input := input through rotor1 (left to right)
 * 		> call afterLeftToRightRotor1
 * 		> input := input through rotor2 (left to right)
 * 		> call afterLeftToRightRotor2
 * 		> input := input through rotor3 (left to right)
 * 		> call afterLeftToRightRotor3
 * 
 * 		> input := input through plugboard
 * 		> call afterPlugboard2
 * 
 * 		> call end
 * 
 * If any of these methods return false, the encryption process stops and returns the partial result
 * @author John Rizkalla
 */
public interface EnigmaMachineStepper {
	
	/**
	 * @param input unprocessed input to encrypt. If input is not a letter, no other method is called (encryption exits now) 
	 */
	public boolean start(char input);

	/**
	 * @param input processed input (capitalized letter)
	 */
	public boolean afterRotate(char input);
	
	/**
	 * @param input input to plugboard1
	 * @param output from plugboard1
	 */
	public boolean afterPlugboard1 (char input, char output);

	/**
	 * @param input input to rotor3
	 * @param rotorOutput full output from rotor3 (@see {@link Rotor#translateRightToLeftSteps(char)})
	 */
	public boolean afterRightToLeftRotor3(char input, char[] rotorOutput);
	/**
	 * @param input input to rotor2
	 * @param rotorOutput full output from rotor2 (@see {@link Rotor#translateRightToLeftSteps(char)})
	 */
	public boolean afterRightToLeftRotor2(char input, char[] rotorOutput);
	/**
	 * @param input input to rotor1
	 * @param rotorOutput full output from rotor1 (@see {@link Rotor#translateRightToLeftSteps(char)})
	 */
	public boolean afterRightToLeftRotor1(char input, char[] rotorOutput);

	/**
	 * @param input input to reflector
	 * @param reflectorOutput reflector output
	 */
	public boolean afterReflector(char input, char reflectorOutput);
	
	/**
	 * @param input input to rotor1
	 * @param rotorOutput full output from rotor1 (@see {@link Rotor#translateLeftToRightSteps(char)})
	 */
	public boolean afterLeftToRightRotor1(char input, char[] rotorOutput);
	/**
	 * @param input input to rotor2
	 * @param rotorOutput full output from rotor2 (@see {@link Rotor#translateLeftToRightSteps(char)})
	 */
	public boolean afterLeftToRightRotor2(char input, char[] rotorOutput);
	/**
	 * @param input input to rotor3
	 * @param rotorOutput full output from rotor3 (@see {@link Rotor#translateLeftToRightSteps(char)})
	 */
	public boolean afterLeftToRightRotor3(char input, char[] rotorOutput);
	
	/**
	 * @param input input to the plugboard
	 * @param output output from the plugboard
	 */
	public boolean afterPlugboard2 (char input, char output);
	
	/**
	 * @param output final processed output
	 */
	public void end(char output);
}

package enigma.machine;

/**
 * Simulates an Enigma machine with the following structure:
 * {@code || reflector | rotor1 | rotor2 | rotor3 ||} 
 * <p>
 * As with a standard Enigma machine, input goes through:
 * <ol>
 * 		<li> plugboard
 * 		<li> rotor[2] (RightToLeft)
 * 		<li> rotor[1] (RightToLeft)
 * 		<li> rotor[0] (RightToLeft)
 * 		<li> reflector
 * 		<li> rotor[1] (LeftToRight)
 * 		<li> rotor[2] (LeftToRight)
 * 		<li> rotor[3] (LeftToRight)
 * 		<li> plugboard
 * </ol>
 * <p>
 * Rotor3 turns before a char is encrypted
 * <p>
 * More information can be found at EnigmaMachineStepper
 * @see EnigmaMachineStepper
 * @author John Rizkalla
 *
 */
public class EnigmaMachine {
	private Rotor[] rotor; // 3 rotors
	private Reflector reflector;
	private Plugboard plugboard;
	
	private EnigmaMachineStepper stepper;
	
	private void connectRotors(){
		if (rotor[0] != null && rotor[1] != null && rotor[2] != null){
			rotor[2].setConnection(rotor[1]);
			rotor[1].setConnection(rotor[0]);
		}
	}
	
	/**
	 * Creates a new enigma machine with the parts passed in
	 * @param rotors {@code rotor[i]} has 3 rotors. {@code rotor[0]} is the one at the left and {@code rotor[2]} is the one at the right. They don't have to be connected
	 * @param reflector the reflector
	 * @param plugboard the plugboard
	 */
	public EnigmaMachine(Rotor[] rotors, Reflector reflector, Plugboard plugboard){
		if (rotors != null){
			if (rotors.length != 3)
				throw new IllegalArgumentException();
			rotor = rotors;
		} else{
			rotor = new Rotor[3]; // set to null
		}
		
		this.reflector = reflector;
		this.plugboard = plugboard;
	}
	
	/**
	 * Creates an empty EnigmaMachine
	 */
	public EnigmaMachine(){
		this(null, null, null);
	}
	
	/**
	 * @return the rotors
	 */
	public Rotor getRotor(int rotorNum) {
		return rotor[rotorNum];
	}

	/**
	 * @param rotor the rotor to set
	 */
	public void switchRotor(int rotorNum, Rotor rotor) {
		if (this.rotor == null)
			this.rotor = new Rotor[3];
		this.rotor[rotorNum] = rotor;
		this.connectRotors();
	}

	/**
	 * @return the reflector
	 */
	public Reflector getReflector() {
		return reflector;
	}

	/**
	 * @param reflector the reflector to set
	 */
	public void switchReflector(Reflector reflector) {
		this.reflector = reflector;
	}

	/**
	 * @return the plugboard
	 */
	public Plugboard getPlugboard() {
		return plugboard;
	}
	
	/**
	 * @param plugboard the plugboard to set
	 */
	public void switchPlugboard(Plugboard plugboard){
		this.plugboard = plugboard;
	}

	/**
	 * @return the stepper
	 */
	public EnigmaMachineStepper getStepper() {
		return stepper;
	}

	/**
	 * @param stepper the stepper to set
	 */
	public void setStepper(EnigmaMachineStepper stepper) {
		this.stepper = stepper;
	}

	/**
	 * Encryptes the input. For this to work, all parts must exist (not {@code null}).
	 * @param input the input to be encrypted
	 * @return the encrypted char
	 * @throws IllegalStateException if any of the parts are {@code null}
	 */
	public char encrypt(char input){
		if (rotor[0] == null || rotor[1] == null || rotor[2] == null)
			throw new IllegalStateException();
		if (plugboard == null || reflector == null)
			throw new IllegalStateException();
		
		// make sure that input is a letter
		if ((input >= 'A' && input <= 'Z') || (input >= 'a' && input <= 'z')){
			
			if (stepper != null){
				if (!stepper.start(input))
					return input;
			}
			
			rotor[2].rotate();
			
			if (stepper != null){
				if (!stepper.afterRotate(input))
					return input;
			}
			
			char prevInput = input;

			input = plugboard.map(input);
			if (stepper != null){
				if (!stepper.afterPlugboard1(prevInput, input))
					return input;
			}

			
			prevInput = input;
			char []rotorOut = rotor[2].translateRightToLeftSteps(input);
			input = rotorOut[rotorOut.length - 1];
			if (stepper != null){
				if (!stepper.afterRightToLeftRotor3(prevInput, rotorOut))
					return input;
			}
			prevInput = input;
			rotorOut = rotor[1].translateRightToLeftSteps(input);
			input = rotorOut[rotorOut.length - 1];
			if (stepper != null){
				if (!stepper.afterRightToLeftRotor2(prevInput, rotorOut))
					return input;
			}
			prevInput = input;
			rotorOut = rotor[0].translateRightToLeftSteps(input);
			input = rotorOut[rotorOut.length - 1];
			if (stepper != null){
				if (!stepper.afterRightToLeftRotor1(prevInput, rotorOut))
					return input;
			}
			
			prevInput = input;
			input = reflector.map(input);
			if (stepper != null){
				if (!stepper.afterReflector(prevInput, input))
					return input;
			}
			
			prevInput = input;
			rotorOut = rotor[0].translateLeftToRightSteps(input);
			input = rotorOut[rotorOut.length - 1];
			if (stepper != null){
				if (!stepper.afterLeftToRightRotor1(prevInput, rotorOut))
					return input;
			}
			prevInput = input;
			rotorOut = rotor[1].translateLeftToRightSteps(input);
			input = rotorOut[rotorOut.length - 1];
			if (stepper != null){
				if (!stepper.afterLeftToRightRotor2(prevInput, rotorOut))
					return input;
			}
			prevInput = input;
			rotorOut = rotor[2].translateLeftToRightSteps(input);
			input = rotorOut[rotorOut.length - 1];
			if (stepper != null){
				if (!stepper.afterLeftToRightRotor3(prevInput, rotorOut))
					return input;
			}
			
			prevInput = input;
			input = plugboard.map(input);
			if (stepper != null){
				if (!stepper.afterPlugboard2(prevInput, input))
					return input;
				stepper.end(input);
			}
		}
	
		return input;
	}
	
	/**
	 * Encrypts a whole string, this method uses encrypt(char)
	 * @param input the input to encrypt
	 * @return the encrypted string
	 * @throws IllegalStateException if any of the parts of the Machine is {@code null}
	 * @see EnigmaMachine#encrypt(char)
	 */
	public String encrypt(String input){
		char [] result = new char[input.length()];
		char [] inputArr = input.toCharArray();
		
		for (int i = 0; i < inputArr.length; i++){
			result[i] = encrypt(inputArr[i]);
		}
		
		return new String(result);
	}
	
	@Override
	public String toString(){
		if (rotor[0] == null || rotor[1] == null || rotor[2] == null)
			return "Empty";
		if (plugboard == null || reflector == null)
			return "Empty";
		
		String str = "";
		str += "1: " + (rotor[0].getCurrentPos() + 'A' - 1) + "\n";
		str += "2: " + (rotor[1].getCurrentPos() + 'A' - 1) + "\n";
		str += "3: " + (rotor[2].getCurrentPos() + 'A' - 1) + "\n";
		str += "Reflector: " + reflector.toString();
		str += "Plugboard: " + plugboard.toString();
		return str;
	}
}

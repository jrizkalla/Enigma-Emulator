package enigma.commandline.ui;

import enigma.machine.EnigmaMachine;

/**
 * Takes a string, encrypts it, and print it to the screnn
 * @author John Rizkalla
 */
public class UIPrinter {
	
	/**
	 * Tells encryptText what info to print in between strings
	 */
	static class Status{
		public boolean [] rotor; // 0 1 or 2
		public boolean reflector;
		public boolean plugboard;
		
		/**
		 * Initializes everything to false
		 */
		public Status(){
			rotor = new boolean[3];
			rotor[0] = rotor[1] = rotor[2] = reflector = plugboard = false;
		}
	}
	
	private boolean animationOn;
	private boolean silentModeOn;
	private EnigmaMachine machine; 
	
	/**
	 * Creates a UIPrinter with {@code animationOn} and {@code silentModeOn} set to false
	 */
	public UIPrinter(){
		super();
		animationOn = false;
		silentModeOn = false;
		machine = new EnigmaMachine();
	}
	
	/**
	 * If {@code textInfo[i]} is a string then it encrypts it and prints it. If it's a Status, it prints the status of Enigma specified by it
	 * @param textInfo an array consisting of Strings and Status's. Anything else is an error
	 */
	public void encryptText(Object[] textInfo){
		for (Object o : textInfo){
			if (o instanceof String){
				// encrypt and print
				String line = (String) o;
				
				if (this.isAnimationOn()){
					// for now just print with a delay
					for (char c : line.toCharArray()){
						System.out.print(machine.encrypt(c));
						System.out.flush();
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {} // move on. No one cares if animationMode does not work properly (for now)
					}
					System.out.println();
				} else {
					System.out.println(machine.encrypt(line));
				}
			} else {
				Status s = (Status) o;
				
				// print: |reflector name| |rotor1 name and pos|
				//   | plugboard |
				if (s.reflector){
					System.out.print(machine.getReflector().)
				}
			}
		}
	}
	
	/**
	 * Prints a help message
	 */
	public void printHelp(){
		
	}
	
	public void printInfo(){
		
	}

	/**
	 * @return animationOn
	 */
	public boolean isAnimationOn() {
		return animationOn;
	}

	/**
	 * @param animationOn whether or not the UIPrinter prints in "animation"
	 */
	public void setAnimationOn(boolean animationOn) {
		this.animationOn = animationOn;
	}

	/**
	 * @return silentModeOn
	 */
	public boolean isSilentModeOn() {
		return silentModeOn;
	}

	/**
	 * @param silentModeOn silent mode suppreses all output. It overrides animation mode
	 */
	public void setSilentModeOn(boolean silentModeOn) {
		this.silentModeOn = silentModeOn;
	}

	/**
	 * @return the machine
	 */
	public EnigmaMachine getMachine() {
		return machine;
	}

	/**
	 * @param machine the machine to set
	 */
	public void setMachine(EnigmaMachine machine) {
		if (machine == null) throw new IllegalArgumentException("machine cannot be null in UIPrinter");
		this.machine = machine;
	}
}

package enigma.commandline.ui;

import enigma.machine.EnigmaMachine;
import enigma.machine.Plugboard;
import enigma.machine.Reflector;
import enigma.machine.Rotor;

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
		// machine with default parts
		Rotor[] rotors = {Rotor.createRotor("I", null), Rotor.createRotor("II", null), Rotor.createRotor("III", null)};
		machine = new EnigmaMachine(rotors, Reflector.createReflectorType("Reflector A"), new Plugboard());
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
							Thread.sleep(500);
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
				String line1 = "";
				boolean line1Empty = true;
				String line2 = "";
				if (s.reflector){
					line1 += "| ";
					line1 += machine.getReflector();
					line1Empty = false;
					line1 += " |  ";
				}
				for (int i = 0; i < 3; i++){
					line1 += "  | ";
					if (s.rotor[i]){
						line1 +=  machine.getRotor(i);
						line1Empty = false;
					}
					line1 += " |  ";
				}
				
				if (s.plugboard){
					line2 = machine.getPlugboard().toString();
				}
				
				if (!line1Empty)
					System.out.println(line1);
				if (line2.length() > 0)
					System.out.println(line2);
			}
			System.out.flush();
		}
	}
	
	/**
	 * Prints a help message
	 */
	public void printHelp(){
		System.out.println();
		String message = "Most commands follow a LaTeX style syntax so they're easy to remember\n";
		message += "Possible commands are:\n";
		message += "\tAnimation and output\n";
		message += "\t\t\\animationOn and \\animationOff turn animation on and off. Right now animation does not do much\n";
		message += "\t\t\\outputOff and \\outputOn turn on and off all output except the actual encrypted message and errors. These two are always on\n";
		message += "\t animation and output commands affect the whole line\n\n";

		message += "\tState reporting\n";
		message += "\t\t\\rotor1 \\rotor2 \\rotor3 \\reflector \\plugboard tell me to print the machine part specified on a new line then resume reading\n";
		message += "\t\t\\state tells me to print them all\n";
		message += "\t These commands tell me to print the state of the machine at this point in the encryption process\n";
		message += "\t\tI print the state on a new line (actually several lines) and then continue the rest on the next line\n";
		message += "\tIf I find a command for the same part present twice without text in between them I print the state twice\n";
		message += "\tIf you don't know what these parts are type the command \\info\n";
		message += "\tIf you do know what they are, then thank me for not writing them in German :)\n\n";
		
		message += "\tSettings\n";
		message += "\t\t\\settings switches to settings mode with the following rules:\n";
		message += "\t\tA change to a single part of the machine is written between < and >\n";
		message += "\t\t\tA change to the reflector is written as < name of reflector >\n";
		message += "\t\t\tA change to a rotor is written as < name of rotor : number (between 1 and 3) : starting position (char) >\n";
		message += "\t\t\tA change to the plugboard is written as < (char, char) ... >\n";
		message += "\t\tTo specify a single change without going to settings mode type \\< ... >\n";
		message += "\t\tNewer settings completely override older ones\n";
		message += "\t\tExecute \\names for the reflector and rotor names\n\n";
		
		message += "\tOther\n";
		message += "\t\t\\exit exits the program\n";
		message += "\t\t\\help (...)\n";
		message += "\t\t\\info gives info about the program and the Enigma Machine\n";
		message += "\t\t\\names just prints all possible names for reflectors and rotors\n\n";
		System.out.print(message);
		System.out.flush();
	}
	
	public void printInfo(){
		System.out.println();
		String message = "Welcome to Enigma Emulator V.1.1\n";
		message += "\tDeveloped by John Rizkalla\n";
		message += "\thttps://github.com/jrizkalla\n";
		message += "An Engima Machine is a brilliant encryption device used by Germany in WW2";
		message += "For information about it, follow these links:\n";
		message += "\thttps://en.wikipedia.org/wiki/Enigma_machine\n";
		message += "\thttp://enigma.louisedade.co.uk/howitworks.html\n";
		message += "\tThere are a couple of really good videos about the Enigma and decrypting it on Numberphile\n";
		message += "\t\thttps://www.youtube.com/watch?v=G2_Q9FoD-oQ\n";
		message += "\t\thttps://www.youtube.com/watch?v=V4V2bpZlqx8\n\n";
		
		message += "The Model used has a reflector, three rotors, and a pluboard\n";
		message += "\tRotors can be one of:\n";
		message += "\t\tI\n\t\tII\n\t\tIII\n\t\tIV\n\t\tV\n\t\tVI\n\t\tVII\n\t\tVIII\n";
		message += "\tThe reflector can be one of:\n";
		message += "\t\tReflector A\n\t\tReflector B\n\t\tReflector C\n";
		
		System.out.print(message);
		System.out.flush();
	}

	public void printNames(){
		System.out.println();
		String message = "Rotors\t\tReflectors\n";
		message += "I     \t\tReflector A\n";
		message += "II    \t\tReflector B\n";
		message += "III   \t\tReflector C\n";
		message += "IV\n";
		message += "V\nVI\nVII\nVIII\n";
		System.out.print(message);
		System.out.flush();
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

package enigma.machine;

/**
 * A reflector is simply a mapping from the alphabet to itself
 * Unlike the Plugboard, a reflector does not change once it is created
 * @author John Rizkalla
 *
 */
public class Reflector {
	private char[] map;
	
	/**
	 * Creates a reflector with the specified mapping
	 * @param to letter x is translated to to[x - 'A']. to must be all uppercase and if x is mapped to y then y should be mapped to x. 
	 * This method does not check these conditions but the whole object will fail if they are not met
	 * @return new new Reflector or null if there is an error
	 */
	public static Reflector createReflector(String to){
		if (to.length() != 26)
			throw new IllegalArgumentException();

		Reflector ref = new Reflector();
		ref.map = to.toCharArray();
		
		return ref;
	}
	
	/**
	 * Creates one of the predefined Reflectors (http://en.wikipedia.org/wiki/Enigma_rotor_details)
	 * @param name the name of the reflector. Must be one of
	 * 		Reflector A
	 * 		Reflector B
	 * 		Reflector C
	 * @return the new reflector or null if there is an error
	 */
	public static Reflector createReflectorType(String name){
		if (name.equals("Reflector A"))
			return createReflector("EJMZALYXVBWFCRQUONTSPIKHGD");
		else if (name.equals("Reflector B"))
			return createReflector("YRUHQSLDPXNGOKMIEBFZCWVJAT");
		else if (name.equals("Reflector C"))
			return createReflector("FVPJIAOYEDRZXWGCTKUQSBNMHL");
		else
			throw new IllegalArgumentException();
	}
	
	/**
	 * Creates a reflector that maps a letter to itself
	 */
	public Reflector(){
		map = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
	}
	
	public char map(char input){
		boolean inputWasLowerCase = false;
		if (input >= 'a' && input <= 'z'){
			inputWasLowerCase = true;
		} else if (input < 'A' || input > 'Z') // not a char
			return input;
		input = Character.toUpperCase(input);
		
		input = map[(int) input - 'A'];
		
		if (inputWasLowerCase)
			return Character.toLowerCase(input);
		else
			return input;
	}
}

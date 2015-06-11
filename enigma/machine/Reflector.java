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
	 * @param to letter {@code x} is translated to {@code to[x - 'A']}. {@code to} must be all uppercase and if {@code x} is mapped to {@code y} then {@code y} should be mapped to {@code x}. 
	 * This method does not check these conditions but the whole object will fail if they are not met
	 * @return new new Reflector or {@code null} if there is an error
	 */
	public static Reflector createReflector(String to){
		if (to.length() != 26)
			throw new IllegalArgumentException();

		Reflector ref = new Reflector();
		ref.map = to.toCharArray();
		
		return ref;
	}
	
	/**
	 * Creates one of the predefined Reflectors ({@link http://en.wikipedia.org/wiki/Enigma_rotor_details})
	 * @param name the name of the reflector. Must be one of
	 * <ul>
	 * 		<li> Reflector A
	 * 		<li> Reflector B
	 * 		<li> Reflector C
	 * </ul>
	 * @return the new reflector or {@code null} if there is an error
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
	
	/**
	 * Maps {@code input} to another char
	 * @param input the char to map
	 * @return the mapped char
	 */
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

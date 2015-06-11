package enigma.machine;

/**
 * A Plugboard is the scrambler that converts from 1 letter to another
 * <p>
 * It is basically a mapping from Chars to Chars that goes both ways
 * Plugboards are mutable, pairings can be changed any time
 * @author John Rizkalla
 */
public class Plugboard {
	
	// LETTER is mapped to map[LETTER - 65]
	private char[] map;
	
	/**
	 * Creates an empty Plugboard (that maps a char to itself)
	 */
	public Plugboard(){
		map = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
	}
	
	/**
	 * Maps a char to another based on the current settings
	 * @param input the char to map
	 * @return the mapped char
	 */
	public char map(char input){
		boolean inputWasLowerCase = false;
		if (input > 'a' && input < 'z'){
			inputWasLowerCase = true;
		} else if (input < 'A' || input > 'Z') // not a char
			return input;
		input = Character.toUpperCase(input);
		
		input = map[(int)input - 'A'];
		
		if (inputWasLowerCase)
			return Character.toLowerCase(input);
		else
			return input;
	}
	
	/**
	 * Pairs {@code a} with {@code b}. It overrides any previous parings that involve {@code a} or {@code b}
	 * @param a the first letter
	 * @param b the second letter
	 */
	public void changeSettings(char a, char b){
		a = Character.toUpperCase(a);
		b = Character.toUpperCase(b);

		// see if a or b are already mapped, If they are, remove the old mapping
		int indexOfA = (int) a - 'A';
		int indexOfB = (int) b - 'A';
		
		if (map[indexOfA] != a){
			// unpair it
			char pairedChar = map[indexOfA];
			map[pairedChar - 'A'] = pairedChar;
		}
		if (map[indexOfB] != b){
			char pairedChar = map[indexOfB];
			map[pairedChar - 'A'] = pairedChar;
		}
		
		// map a to b and b to a
		map[indexOfA] = b;
		map[indexOfB] = a;
	}
}

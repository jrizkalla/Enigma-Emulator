package enigma.commandline.settings;

import enigma.machine.EnigmaMachine;
import enigma.machine.Plugboard;

/**
 * 
 * @author John Rizkalla
 */
public class PlugboardSettings extends Settings {

	private char[][] pairs;

	PlugboardSettings(String lexeme, char[][] pairs) {
		super(lexeme);
		
		this.pairs = pairs;
	}

	/**
	 * @return the pairs
	 */
	public char[][] getPairs() {
		return pairs;
	}
	
	@Override
	void apply(EnigmaMachine machine) {
		Plugboard newPlugboard = new Plugboard();
		for (char[] pair : pairs){
			newPlugboard.changeSettings(pair[0], pair[1]);
		}
		machine.switchPlugboard(newPlugboard);
	}
}

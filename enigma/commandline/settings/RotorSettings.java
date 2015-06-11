package enigma.commandline.settings;

import enigma.machine.EnigmaMachine;
import enigma.machine.Rotor;

public class RotorSettings extends Settings {
	
	private String name;
	private int rotorNum;
	private char pos;

	RotorSettings(String lexeme, String name, char rotorNum, char pos) throws SettingsParserException {
		super(lexeme);
		
		this.name = name;
		this.name = name.toUpperCase();
		if (Rotor.createRotor(this.name, null) == null)
			throw new SettingsParserException("invalid rotor name, must be one of (I, II, III, IV, V, VI, VII, or VIII");

		this.rotorNum = Integer.parseInt(Character.toString(rotorNum), 10);
		if (this.rotorNum < 1 || this.rotorNum > 3)
			throw new SettingsParserException("invalid rotor number, must be between 1 and 3 (inclusive)", lexeme);
		else
			this.rotorNum--;

		this.pos = pos;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the rotorNum
	 */
	public int getRotorNum() {
		return rotorNum;
	}

	/**
	 * @return the pos
	 */
	public char getPos() {
		return pos;
	}

	@Override
	void apply(EnigmaMachine machine) {
		Rotor newRotor = Rotor.createRotor(name, null);
		newRotor.setCurrentPos(pos);
		machine.switchRotor(rotorNum, newRotor);
	}

}

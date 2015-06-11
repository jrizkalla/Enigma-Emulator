package enigma.commandline.settings;

import enigma.machine.EnigmaMachine;
import enigma.machine.Reflector;

public class ReflectorSettings extends Settings {
	private String reflectorName;

	ReflectorSettings(String lexeme, String reflectorName) throws SettingsParserException {
		super(lexeme);
		this.reflectorName = reflectorName;
		try{
			Reflector.createReflectorType(this.reflectorName);
		} catch(IllegalArgumentException e){
			throw new SettingsParserException("Invalid reflector name, must be one of (\"Reflector A\", \"Reflector B\", or \"Reflector C\"", lexeme);
		}
	}

	/**
	 * @return the reflectorName
	 */
	public String getReflectorName() {
		return reflectorName;
	}

	@Override
	void apply(EnigmaMachine machine) {
		machine.switchReflector(Reflector.createReflectorType(this.reflectorName));
	}

}

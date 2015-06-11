package enigma.commandline.settings;

import enigma.machine.EnigmaMachine;

public abstract class Settings {
	private String lexeme;
	
	public static void apply(EnigmaMachine enigma, Settings[] settings){
		for (Settings s : settings)
			s.apply(enigma);
	}
	
	Settings(String lexeme){
		this.lexeme = lexeme;
	}
	
	public String getLexeme(){
		return lexeme;
	}
	
	abstract void apply(EnigmaMachine machine);
	
	@Override
	public String toString(){
		return lexeme;
	}
}

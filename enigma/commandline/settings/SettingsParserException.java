package enigma.commandline.settings;

public class SettingsParserException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 357561346976799762L;

	private String lexeme = "";
	
	public SettingsParserException(){
		super();
	}
	
	public SettingsParserException(String message){
		super(message);
	}
	
	public SettingsParserException(String message, String lexeme){
		super(message);
		this.lexeme = lexeme;
	}
	
	/**
	 * @return the lexeme
	 */
	public String getLexeme() {
		return lexeme;
	}

	/**
	 * @param lexeme the lexeme to set
	 */
	public void setLexeme(String lexeme) {
		this.lexeme = lexeme;
	}


}

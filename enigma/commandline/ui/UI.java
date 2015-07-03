package enigma.commandline.ui;

import enigma.commandline.settings.*;

import java.util.ArrayList;
import java.util.Scanner;
import java.io.IOException;
import java.io.Reader;
import java.io.BufferedReader;

/**
 * The text UI for Enigma Emulator
 * @author John Rizkalla
 *
 */
public class UI {
	static private UIPrinter printer = new UIPrinter();
	/**
	 * Parses a line of input based on the following rules:
	 * <ul>
	 * 	<li> Any character <tt>(a-z)|(A-Z)</tt> is encrypted
	 *  <li> Any non character (like *, & ...) is just passed through
	 *  <li> A <tt>\</tt> followed by <tt>(a-z)|(A-Z)|(0-9)</tt> followed by a space is a command (LaTeX style commands). Valid commands include
	 *  <ul>
	 *  	<li> <tt>\animationOn</tt> and <tt>\animationOff</tt>. Turns animation on and off. The last one of these determines animation mode for the whole line
	 *  	<li> <tt>\settings</tt> Switches to settings mode. And discards the whole line
	 *  	<li> <tt>\\<...\><\tt> applies the settings between \< and \> to the rest of the line without discarding the rest of the line
	 *  	<li> <tt>\rotor1</tt>, <tt>\rotor2</tt>, <tt>\rotor3</tt>, <tt>\reflector</tt>, or <tt>\plugboard</tt> prints the state of the specified part on a newline and continues parsing (on another new line)
	 *  	<li> <tt>\state</tt> prints the state of the whole machine (on a new line)
	 *  	<li> <tt>\outputOff</tt> suppresses all output except the actual encrypted message. <tt>\outputOn</tt> does the opposite
	 *  	<li> <tt>\exit</tt> causes the program to exit. Does not discard the whole line
	 *  	<li> <tt>\help</tt> prints a help message and discards line. @see enigma.commandline.ui.UIPrinter#printHelp
	 *  	<li> <tt>\info</tt> prints info message. @see enigma.commandline.ui.UIPrinter#printInfo
	 *  	<li> <tt>\names</tt> just prints the names of rotors and reflectors
	 *  </ul>
	 * @param line
	 * @throws SettingsParserException 
	 * @returns 0 to indicate that everything is fine, returns 1 to indicate that the user want to enter settings mode, and returns 2 to indicate that the user wants to exit
	 */
	public static int parseLineInNormalMode(String line) throws SettingsParserException{
		int state = 0; // 0 for text and 1 for command
		String command = "";
		String text = "";
		ArrayList<Object> result = new ArrayList<Object>();
		boolean exit = false;

		for (int i = 0; i < line.length() + 1; i++){
			char curr = 0;
			if (i < line.length()) curr = line.charAt(i);
			if (state == 0 && (curr == '\\' || curr == 0)){
				state = 1;
				if (!text.isEmpty()){
					result.add(text);
					text = "";
				}
			} else if (state == 1 && !((curr >= 'a' && curr <= 'z') || (curr >= 'A' && curr <= 'Z'))){ // commands are terminated by non letters
				// proccess command
				if (command.equals("")){
					throw new SettingsParserException("illegal command", "\\" + command);
				} else
				if (command.equals("animationOn")){
					printer.setAnimationOn(true);
				} else if (command.equals("animationOff")){
					printer.setAnimationOn(false);
				} else if (command.equals("outputOn")){
					printer.setSilentModeOn(true);
				} else if (command.equals("outputOff")){
					printer.setSilentModeOn(false);
				} else if (command.equals("settings")){
					// discard input
					return 1;
				} else if (command.equals("exit")){
					exit = true;
				} else if (command.equals("help")){
					printer.printHelp(); // discard input
					return 0;
				} else if (command.equals("names")){
					printer.printNames();
					return 0;
				} else if (command.charAt(0) == '<'){
					Settings.apply(printer.getMachine(), SettingsParser.parse(command)); // may throw an exception
				} else if ((command.length() == 6 && command.substring(0, 5).equals("rotor")) || command.equals("reflector") || command.equals("plugboard") || command.equals("state")){
					UIPrinter.Status status;
					if (result.size() == 0 || result.get(result.size()-1) instanceof String){
						// create a new one
						status = new UIPrinter.Status();
						result.add(status);
					} else {
						status = (UIPrinter.Status) result.get(result.size()-1);
					}
					if ((command.length() == 6 && command.substring(0, 5).equals("rotor"))){
						// get the number
						int num;
						try{
							num = Integer.parseInt(command.substring(5, 6), 10);
							if (num < 1 || num > 3)
								throw new NumberFormatException();
							num--;
						}catch(NumberFormatException e){
							// just abandon everything
							throw new SettingsParserException("Unknown command", "\\" + command);
						}
						if (status.rotor[num]){
							// create a new status
							status = new UIPrinter.Status();
							result.add(status);
						}
						status.rotor[num] = true;
					} else if (command.equals("reflector")){
						if (status.reflector){
							status = new UIPrinter.Status();
							result.add(status);
						}
						status.reflector = true;
					} else if (command.equals("plugboard")){
						if (status.plugboard){
							status = new UIPrinter.Status();
							result.add(status);
						}
						status.plugboard = true;
					} else if (command.equals("state")){
						if (status.rotor[0] || status.rotor[1] || status.rotor[2] || status.reflector || status.plugboard){
							status = new UIPrinter.Status();
							result.add(status);
						}
						status.rotor[0] = status.rotor[1] = status.rotor[2] = status.plugboard = status.reflector = true;
					} else {
						throw new SettingsParserException("Unknown command", "\\" + command);
					}
				}
			}
			else {
				if (state == 0){
					text += curr;
				} else {
					command += curr;
				}
			}
		}
		
		// print the result and exit
		printer.encryptText(result.toArray());
		if (exit) return 2;
		else return 0;
	}
	
	/**
	 * Starts the user interface. Exits when the user is done (<tt>\exit</tt> command)
	 * IsSilenMode in UIPrinter applies to everything in this method except the error message
	 * @param inputStream the input stream to read from. Of null to read from stdin. HAS TO BE NULL to read from stdin or bad things will happen
	 */
	public static void startReading(Reader inputStream) throws IOException{
		if (!printer.isSilentModeOn()) System.out.println("Welcome to Enigma Emulator V.1.1");
		if (!printer.isSilentModeOn()) System.out.println("Type \\help for help and \\exit to exit followed by a end of file (EOF) (Ctrl-D on a Unix system)");
		if (!printer.isSilentModeOn()) System.out.println("Type \\info EOF if you don't know what an Enigma Machine is");

		int exitStatus = 0;
		Scanner reader = null;
		if (inputStream != null)
			reader = new Scanner(inputStream);
		while (exitStatus != 2){
			if (inputStream == null) reader = new Scanner(System.in);
			// read until EOF
			String input = "";
			while (reader.hasNextLine()){
				String line = reader.nextLine();
				input += line;
			}
			if (input == "")
				break;
			
			try{
				if (exitStatus == 0){
					exitStatus = UI.parseLineInNormalMode(input);
				} else { // settings mode
					Settings.apply(printer.getMachine(), SettingsParser.parse(input));
				}
			} catch (SettingsParserException e) {
				System.err.println("Oops, looks like you misspelled something in \"" + e.getLexeme() + "\"");
				System.err.println("Apperantly the problem was caused by " + e.getMessage());
				System.err.println("Type \\help followed by EOF for the help message");
				// ignore line and continue (in normal mode)
				exitStatus = 0;
			}

		}
		if (!printer.isSilentModeOn()) System.out.println("Bye :D (or maybe Hail Hitler?)");
	}
}

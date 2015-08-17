package enigma.commandline.settings;

import java.util.ArrayList;
import java.util.HashMap;

public class SettingsParser {
	
	static ArrayList<HashMap<Character, Integer>> nextState = null;
	
	private static int nextStateFunction(int state, char next){
//		System.out.println("(state, next) = (" + state + ", " + next + ")");

		if (nextState == null){
			nextState = new ArrayList<HashMap<Character, Integer>>();
			for (int i = 0; i < 15; i++){
				nextState.add(new HashMap<Character, Integer>());
			}
			
			nextState.get(0).put('<', 1);
			nextState.get(1).put('(', 6);
			nextState.get(2).put('>', 3);
			nextState.get(2).put(':', 4);
			nextState.get(3).put('<', 1);
			nextState.get(5).put(':', 13);
			nextState.get(7).put(',', 8);
			nextState.get(9).put(')', 10);
			nextState.get(10).put('(', 6);
			nextState.get(10).put('>', 11);
			nextState.get(11).put('<', 1);
			nextState.get(12).put('<', 1);
			nextState.get(14).put('>', 12);
		}
		
		if (next == ' ' || next == '\n' || next == '\t')
			return state;
		
		if (state == 4){
			if (next >= '1' && next <= '9')
				return 5;
		}
		if (state == 1 || state == 6 || state == 8){
			next = Character.toUpperCase(next);
			if ((next >= 'A' && next <= 'Z') || (next >= '1' && next <= '9'))
				return state + 1;
		} 
		if (state == 6 || state == 8 || state == 13){
			next = Character.toUpperCase(next);
			if ((next >= 'A' && next <= 'Z'))
				return state + 1;
		}
		
		if (state == 2){
			next = Character.toUpperCase(next);
			if (next >= 'A' && next <= 'Z' || (next >= '1' && next <= '9'))
				return state;
		}
		
		if (state < 0 || state > 14)
			return -1;
		
		if (nextState.get(state).containsKey(next))
			return nextState.get(state).get(next);
		else 
			return -1;
		
	}
	
	public static Settings[] parse(String input) throws SettingsParserException{
		ArrayList<Settings> result = new ArrayList<Settings> ();
		
		if (input.trim() == "")
			return new Settings[0];

		// parses input using a DFA
		int state = 0; // 0 - 11 with 0, 3, 11, and 12 accepting. -1 is the error state
		
		String lexeme = "";
		String name = "";
		char rotorNum = '0';
		char rotorPos = 'A';
		ArrayList<char []> plugboardPairs = new ArrayList<char[]>();

		for (char next : input.toCharArray()){
			lexeme += next;
			int prevState = state;
			state = SettingsParser.nextStateFunction(state, next);

			if ((prevState == 1 || prevState == 2) && state == 2){
				name += next;
			} if (prevState == 6 && state == 7){
				plugboardPairs.add(new char[2]);
				plugboardPairs.get(plugboardPairs.size()-1)[0] = next;
			} if (prevState == 8 && state == 9){
				plugboardPairs.get(plugboardPairs.size()-1)[1] = next;
			} if (prevState == 4 && state == 5){
				rotorNum = next;
			} if (prevState == 13 && state == 14){
				rotorPos = next;
			}
			
			// accepting states
			if (prevState == 2 && state == 3){
				result.add(new ReflectorSettings(lexeme, name.trim()));
				lexeme = "";
				name = "";
			} else if (prevState == 14 && state == 12){
				result.add(new RotorSettings(lexeme, name.trim(), rotorNum, rotorPos));
				lexeme = "";
				name = "";
			} else if (prevState == 10 && state == 11){
				result.add(new PlugboardSettings(lexeme, plugboardPairs.toArray(new char[plugboardPairs.size()-1][2])));
				lexeme = "";
				plugboardPairs = new ArrayList<char []>();
			} else if (state == -1){
				throw new SettingsParserException("invalid sequence encountered", lexeme);
			}
		}
		
		if (state == 0 || state == 3 || state == 11 || state == 12)
			return result.toArray(new Settings[result.size()]);
		else
			throw new SettingsParserException("invalid sequence encountered", lexeme);
	}
}

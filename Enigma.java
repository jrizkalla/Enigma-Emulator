import java.io.*;
import enigma.commandline.ui.UI;

public class Enigma {
	public static void main (String[] args){
		try{
			UI.startReading(null);
		} catch (IOException e){
			System.err.println("Error reading\n");
			e.printStackTrace();
		}
	}
}

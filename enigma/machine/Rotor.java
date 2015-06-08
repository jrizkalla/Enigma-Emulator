package enigma.machine;

/**
 * A Rotor represents a physical rotor
 * It starts out with an initial position and moves using the methods provided
 * A rotor's physical structure can be changed using the constructor but they cannot be changed for a created object
 */
public class Rotor {
	public String name; // for debugging
    // settings of the Rotor
	String leftSide;
	String rightSide;
    // the first wiring is 1 ... 26
	// leftSide[i] maps to rightSide[i]

    // When this rotor completes a full turn, it turns the rotor in connection if there is one
    private Rotor connection;
    private int[] fullTurn; // length 1 or 2
    // Rotor completes a full when it steps from fullTurn to the next number


    // state
    private int currPos;


    // ----------------- STATIC METHODS ---------------------

    /**
     * Creates a Rotor with the specified settings
     * @param from from and to specify the wiring. from[i] -> to[i]
     * @param to both from and to must have length == 26 and both of them must contain all the alphabet in UPPERCASE
     * @param connection a connection to another rotor. When this rotor completes a full turn it turns connection. Can be null
     * @param fullTurn Where is the full turn. If fullTurn is null, it sets it to the default value of rotor "I"
     * @return a new Rotor or null if there is an error
     */
    public static Rotor createRotor(String from, String to, Rotor connection, int[] fullTurn){
        if (from.length() != 26 || to.length() != 26) {
        	throw new IllegalArgumentException();
        }

        Rotor newRotor = new Rotor("empty");
        newRotor.leftSide = to;
        newRotor.rightSide = from;

        newRotor.connection = connection;

        if (fullTurn == null){
            newRotor.fullTurn = new int[1];
            newRotor.fullTurn[0] = 17;
        } else{
            newRotor.fullTurn = fullTurn.clone();
        }
        for (int i = 0; i < fullTurn.length; i++){
            if (fullTurn[i] < 1 || fullTurn[i] > 26)
                return null;
        }

        return newRotor;
    }

    /**
     * Creates a Rotor with the specified settings
     * @param rotorName the name of a default rotor, acceptable names are (source: http://en.wikipedia.org/wiki/Enigma_rotor_details)
     *                  I
     *                  II
     *                  III
     *                  IV
     *                  V
     *                  VI
     *                  VII
     *                  VIII
     * @param connection a connection to another rotor. When this rotor completes a full turn it turns connection. Can be null
     * @return a new Rotor or null if there is an error
     */
    public static Rotor createRotor(String rotorName, Rotor connection){
        String from = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String to;
        int[] fullTurn = new int[2];
        fullTurn[1] = -1;

        rotorName = rotorName.toUpperCase();
        if (rotorName.equals("I")) {
            to = "EKMFLGDQVZNTOWYHXUSPAIBRCJ";
            fullTurn[0] = 'Q' - 'A' + 1;
        }
        else if (rotorName.equals("II")) {
            to = "AJDKSIRUXBLHWTMCQGZNPYFVOE";
            fullTurn[0] = 'E' - 'A' + 1;
        }
        else if (rotorName.equals("III")) {
            to = "BDFHJLCPRTXVZNYEIWGAKMUSQO";
            fullTurn[0] = 'V' - 'A' + 1;
        }
        else if (rotorName.equals("IV")) {
            to = "ESOVPZJAYQUIRHXLNFTGKDCMWB";
            fullTurn[0] = 'J' - 'A' + 1;
        }
        else if (rotorName.equals("V")) {
            to = "VZBRGITYUPSDNHLXAWMJQOFECK";
            fullTurn[0] = 'Z' - 'A' + 1;
        }
        else if (rotorName.equals("VI")) {
            to = "JPGVOUMFYQBENHZRDKASXLICTW";
            fullTurn[0] = 'Z' - 'A' + 1;
            fullTurn[1] = 'M' - 'A' + 1;
        }
        else if (rotorName.equals("VII")) {
            to = "NZJHGRCXMYSWBOUFAIVLPEKQDT";
            fullTurn[0] = 'Z' - 'A' + 1;
            fullTurn[1] = 'M' - 'A' + 1;
        }
        else if (rotorName.equals("VIII")) {
            to = "FKQHTLXOCBJSPDZRAMEWNIUYGV";
            fullTurn[0] = 'Z' - 'A' + 1;
            fullTurn[1] = 'M' - 'A' + 1;
        }
        else
        	throw new IllegalArgumentException();

        if (fullTurn[1] == -1){ // trim fullTurn
            int temp = fullTurn[0];
            fullTurn = new int[1];
            fullTurn[0] = temp;
        }

        return Rotor.createRotor(from, to, connection, fullTurn);
    }


    // ------------- NON STATIC METHODS -------------------

    private Rotor(String empty){
        // creates an empty Rotor
        this.currPos = 1;
    }

    /**
     * Create a Rotor with default settings starting at position 1
     */
    public Rotor(){
        this(1, null);
    }

    /**
     * Create a Rotor with default settings starting at position pos
     * @param pos has to be an int between 1 and 26 (inclusive)
     */
    public Rotor(int pos){
        this(pos, null);
    }

    /**
     * Creates a Rotor with default settings starting at position pos with a connection to another Rotor
     * When the rotor completes a full turn (as specified by fullTurn) it turns the connection
     * @param pos has to be an int between 1 and 26 (inclusive)
     * @param connection another rotor
     */
    public Rotor(int pos, Rotor connection){
        leftSide = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        rightSide = "EKMFLGDQVZNTOWYHXUSPAIBRCJ";

        this.fullTurn = new int[1];
        this.fullTurn[0] = 17;

        this.currPos = pos;
        if (pos < 1 || pos > 26){
            throw new IllegalArgumentException();
        }

        this.connection = connection;
        this.currPos = 1;
    }


    /**
     * @return the current position of the rotor
     */
    public int getCurrentPos() {
        return currPos;
    }

    /**
     * Set the current position of the rotor
     * @param currPos has to be an int between 1 and 26 (inclusive)
     */
    public void setCurrentPos(int currPos){
        if (currPos < 1 || currPos > 26)
            throw new IllegalArgumentException();
        this.currPos = currPos;
    }
    
    /**
     * Set the current position of the rotor. 'A' corresponds to 1
     * @param currPos current position as a letter. currPos must be a letter
     */
    public void setCurrentPos(char currPos){
    	currPos = Character.toUpperCase(currPos);
    	if (currPos < 'A' || currPos > 'Z')
    		throw new IllegalArgumentException();
    	
    	setCurrentPos((int)(currPos - 'A' + 1));
    }

    /**
     * @return the connection (to another Rotor)
     */
    public Rotor getConnection() {
        return connection;
    }

    /**
     * @param connection set the connection (to another Rotor)
     */
    public void setConnection(Rotor connection) {
        this.connection = connection;
    }

    /**
     * Rotates the rotor and if it rotates a full turn, rotates the next one
     */
    public void rotate(){
    	if (connection != null){
            for (int i = 0; i < fullTurn.length; i++){
                if (currPos == fullTurn[i]) {
                    connection.rotate();
                }
            }
        }

        currPos++;
        if (currPos > 26)
            currPos = 1;
    }

    /**
     * Translates a character to another (based on the wiring of the rotor)
     * This simulates a signal coming from the left side of the rotor and exiting from the right side
     * @param input the character to translate
     * @return the translated to a character
     */
    public char translateRightToLeft(char input){
        // Translation:
        // A -> A + shift --wiring--> B -> B + shift

        // shift input to the position of the rotor
        int intChar = (int)input;
        
    	boolean wasUpperCase = false;
        if (intChar >= 'A' && intChar <= 'Z'){
        	wasUpperCase = true;
        } else if (intChar < 'a' || intChar > 'z'){
        	// not a letter
        	return input;
        }
        intChar = (int)Character.toUpperCase(input);
        
        intChar += currPos - 1; //  (currPos - 1) has range 0 to 25 (inclusive)
        if (intChar > 'Z') {
            intChar -= 'Z' - 'A' + 1;
        }
        
        intChar = (int)leftSide.charAt(rightSide.indexOf(intChar));
        
//        // now shift it out of the rotor
        intChar -= currPos - 1;
        if (intChar < 'A'){
            intChar += 'Z' - 'A' + 1;
        }

        if (wasUpperCase){
        	return (char)intChar;
        } else {
        	return Character.toLowerCase((char)intChar);
        }
    }
    
    
    /**
     * Translates a character to another (based on the wiring of the rotor)
     * This simulates a signal coming from the right side of the rotor and exiting from the left side
     * @param input the character to translate
     * @return the translated to a character
     */
    public char translateLeftToRight(char input){
    	// Translation:
        // A -> A + shift --wiring--> B -> B + shift

        // shift input to the position of the rotor
        int intChar = (int)input;
        
    	boolean wasUpperCase = false;
        if (intChar >= 'A' && intChar <= 'Z'){
        	wasUpperCase = true;
        } else if (intChar < 'a' || intChar > 'z'){
        	// not a letter
        	return input;
        }
        intChar = (int)Character.toUpperCase(input);
        
        intChar += currPos - 1; //  (currPos - 1) has range 0 to 25 (inclusive)
        if (intChar > 'Z') {
            intChar -= 'Z' - 'A' + 1;
        }
        
        intChar = (int)rightSide.charAt(leftSide.indexOf(intChar));

        // no shift it out of the rotor
        intChar -= currPos - 1;
        if (intChar < 'A'){
            intChar += 'Z' - 'A' + 1;
        }
        
        if (wasUpperCase){
        	return (char)intChar;
        } else {
        	return Character.toLowerCase((char)intChar);
        }
    }

    @Override
    public String toString(){
        return "Rotor at position " + currPos;
    }
}

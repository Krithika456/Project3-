package application;

public class UserNameRecognizer {
    /**
     * <p> Title: FSM-translated UserNameRecognizer. </p>
     *
     * <p> Description: A demonstration of the mechanical translation of a Finite State Machine
     * diagram into an executable Java program using the UserName Recognizer. The code
     * detailed design is based on a while loop with a select list.</p>
     *
     * <p> Copyright: Lynn Robert Carter © 2024 </p>
     *
     * @author Lynn Robert Carter
     *
     * @version 1.00        2024-09-13    Initial baseline derived from the Even Recognizer
     * @version 1.01        2024-09-17    Correction to address UNChar coding error, improper error
     *                                  message, and improve internal documentation
     *
     * @2nd_author Daniel Lopez 1224071368
     *
     * @version 1.02        2025-01-23    Implementation of the Task 5 Extended Finite State Machine
     */

    /**********************************************************************************************
     *
     * Result attributes to be used for GUI applications where a detailed error message and a
     * pointer to the character of the error will enhance the user experience.
     *
     */

    public static String userNameRecognizerErrorMessage = "";   // The error message text
    public static String userNameRecognizerInput = "";            // The input being processed
    public static int userNameRecognizerIndexofError = -1;         // The index of error location
    private static int state = 0;                        // The current state value
    private static int nextState = 0;                    // The next state value
    private static boolean finalState = false;         // Is this state a final state?
    private static String inputLine = "";                // The input line
    private static char currentChar;                     // The current character in the line
    private static int currentCharNdx;                   // The index of the current character
    private static boolean running;                      // The flag that specifies if the FSM is running
    private static int userNameSize = 0;                 // The size of the username (should not exceed 16 characters)

    // Private method to display debugging data
    private static void displayDebuggingInfo() {
        // Display the current state of the FSM as part of an execution trace
        if (currentCharNdx >= inputLine.length()) {
            // Display the state when there is no current character.
            System.out.println(((state > 99) ? " " : (state > 9) ? "  " : "   ") + state +
                               ((finalState) ? "       F   " : "           ") + "None");
        } else {
            System.out.println(((state > 99) ? " " : (state > 9) ? "  " : "   ") + state +
                               ((finalState) ? "       F   " : "           ") + "  " + currentChar + " " +
                               ((nextState > 99) ? "" : (nextState > 9) || (nextState == -1) ? "   " : "    ") +
                               nextState + "     " + userNameSize);
        }
    }

    // Private method to move to the next character within the limits of the input line
    private static void moveToNextCharacter() {
        currentCharNdx++;
        if (currentCharNdx < inputLine.length()) {
			currentChar = inputLine.charAt(currentCharNdx);
		} else {
            currentChar = ' ';
            running = false;
        }
    }

    /**********
     * This method is a mechanical transformation of a Finite State Machine diagram into a Java
     * method.
     *
     * @param input     The input string for the Finite State Machine
     * @return          An output string that is empty if everything is okay or it is a String
     *                      with a helpful description of the error
     */
    public static String checkForValidUserName(String input) {
        // Check to ensure that there is input to process.
        if (input.length() <= 0) {
            userNameRecognizerIndexofError = 0;  // Error at first character.
            return "\n*** ERROR *** The input is empty";
        }

        // Initialize the FSM variables.
        state = 0;                          // This is the FSM state number.
        inputLine = input;                  // Save the reference to the input line.
        currentCharNdx = 0;                 // The index of the current character.
        currentChar = input.charAt(0);      // The current character from the above index.

        userNameRecognizerInput = input;    // Save a copy of the input.
        running = true;                     // Start the loop.
        nextState = -1;                     // There is no next state initially.
        System.out.println("\nCurrent Final Input  Next  Date\nState   State Char  State  Size");

        // Initialize the username size.
        userNameSize = 0;

        // The FSM continues until the end of the input is reached or an invalid transition occurs.
        while (running) {
            // Process based on the current state.
            switch (state) {
                case 0:
                    // State 0: Expect an alphabetic character (A-Z, a-z) to transition to State 1.
                    if ((currentChar >= 'A' && currentChar <= 'Z') ||
                        (currentChar >= 'a' && currentChar <= 'z')) {
                        nextState = 1;
                        userNameSize++; // Count the character.
                    } else {
                        running = false; // Invalid starting character.
                    }
                    break;

                case 1:
                    // State 1: Accept alphanumeric characters or a special character to transition.
                    if ((currentChar >= 'A' && currentChar <= 'Z') ||
                        (currentChar >= 'a' && currentChar <= 'z') ||
                        (currentChar >= '0' && currentChar <= '9')) {
                        nextState = 1;
                        userNameSize++;
                    } else if (currentChar == '-' || currentChar == '_' || currentChar == '.') {
                        nextState = 2;
                        userNameSize++;
                    } else {
                        running = false;
                    }
                    // Stop if username size exceeds 16 characters.
                    if (userNameSize > 16) {
						running = false;
					}
                    break;

                case 2:
                    // State 2: After a special character, expect an alphanumeric character to return to State 1.
                    if ((currentChar >= 'A' && currentChar <= 'Z') ||
                        (currentChar >= 'a' && currentChar <= 'z') ||
                        (currentChar >= '0' && currentChar <= '9')) {
                        nextState = 1;
                        userNameSize++;
                    } else {
                        running = false;
                    }
                    // Stop if username size exceeds 16 characters.
                    if (userNameSize > 16) {
						running = false;
					}
                    break;
            }

            if (running) {
                displayDebuggingInfo();
                // Move to the next character.
                moveToNextCharacter();
                // Transition to the next state.
                state = nextState;
                // Mark state 1 as a final state.
                if (state == 1) {
					finalState = true;
				}
                // Reset nextState.
                nextState = -1;
            }
        }
        displayDebuggingInfo();
        System.out.println("The loop has ended.");

        // Set the index of error and prepare the error message.
        userNameRecognizerIndexofError = currentCharNdx;
        userNameRecognizerErrorMessage = "\n*** ERROR *** ";

        // Determine the error based on the final state.
        switch (state) {
            case 0:
                // State 0 is not final; username must start with an alphabetic character.
                userNameRecognizerErrorMessage += "A UserName must start with A-Z, a-z.\n";
                return userNameRecognizerErrorMessage;

            case 1:
                // State 1 is a final state. Check the username length.
                if (userNameSize < 4) {
                    userNameRecognizerErrorMessage += "A UserName must have at least 4 characters.\n";
                    return userNameRecognizerErrorMessage;
                } else if (userNameSize > 16) {
                    userNameRecognizerErrorMessage += "A UserName must have no more than 16 characters.\n";
                    return userNameRecognizerErrorMessage;
                } else if (currentCharNdx < input.length()) {
                    // Extra characters beyond valid username.
                    userNameRecognizerErrorMessage += "A UserName may only contain the characters A-Z, a-z, 0-9, period (.), minus (-), underscore (_)\n";
                    return userNameRecognizerErrorMessage;
                } else {
                    // Username is valid.
                    userNameRecognizerIndexofError = -1;
                    userNameRecognizerErrorMessage = "";
                    return userNameRecognizerErrorMessage;
                }

            case 2:
                // State 2 is not final; a character following a special character must be alphanumeric.
                userNameRecognizerErrorMessage += "A UserName character after a period, minus, or underscore must be A-Z, a-z, 0-9.\n";
                return userNameRecognizerErrorMessage;

            default:
                // This should not happen.
                return "";
        }
    }
}

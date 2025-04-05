package application;

public class PasswordEvaluator {
    /**
     * <p> Title: Directed Graph-translated Password Assessor. </p>
     *
     * <p> Description: A demonstration of the mechanical translation of a Directed Graph
     * diagram into an executable Java program using the Password Evaluator Directed Graph.
     * The detailed design is based on a while loop with a cascade of if statements.</p>
     *
     * <p> Copyright: Lynn Robert Carter © 2022 </p>
     *
     * @author Lynn Robert Carter
     *
     * @version 0.00        2018-02-22    Initial baseline
     *
     * @2nd_author Daniel Lopez 1224071368
     *
     * @version 1.00        2025-01-23    Implementation of Task 8 EFSM
     */

    /**********************************************************************************************
     *
     * Result attributes for GUI applications where a detailed error message and an indicator
     * of the error position enhance the user experience.
     *
     */

    public static String passwordErrorMessage = "";    // The error message text
    public static String passwordInput = "";             // The input being processed
    public static int passwordIndexofError = -1;         // The index where the error was located
    public static boolean foundUpperCase = false;
    public static boolean foundLowerCase = false;
    public static boolean foundNumericDigit = false;
    public static boolean foundSpecialChar = false;
    public static boolean foundLongEnough = false;
    public static boolean foundOtherChar = false;        // Flag for when an invalid character is found
    private static String inputLine = "";                // The input line
    private static char currentChar;                     // The current character in the line
    private static int currentCharNdx;                   // The index of the current character
    private static boolean running;                      // Flag to indicate if the FSM is running

    /**
     * Displays the input line and, on the next line, an indicator at the error position.
     *
     * @param input             The input string.
     * @param currentCharNdx    The location where an error was found.
     */
    private static void displayInputState() {
        // Display the entire input line.
        System.out.println(inputLine);
        // Display a line with a pointer ("?") at the error location.
        System.out.println(inputLine.substring(0, currentCharNdx) + "?");
        System.out.println("The password size: " + inputLine.length() + "  |  The currentCharNdx: " +
                           currentCharNdx + "  |  The currentChar: \"" + currentChar + "\"");
    }

    /**
     * Evaluates the password by simulating a Directed Graph.
     *
     * @param input     The input string for processing.
     * @return          An empty string if the password is acceptable; otherwise, an error message
     *                  detailing which conditions were not met.
     */
    public static String evaluatePassword(String input) {
        // Initialize result attributes.
        passwordErrorMessage = "";
        passwordIndexofError = 0;         // Initialize the index of error.
        inputLine = input;                // Save the input line.
        currentCharNdx = 0;               // Start at the beginning of the input.

        if (input.length() <= 0) {
            return "*** Error *** The password is empty!";
        }

        // Access the first character.
        currentChar = input.charAt(0);

        // Initialize flags.
        passwordInput = input;
        foundUpperCase = false;
        foundLowerCase = false;
        foundNumericDigit = false;
        foundSpecialChar = false;
        foundLongEnough = false;
        foundOtherChar = false;
        running = true;                   // Begin simulation.

        // Process each character using a while loop to simulate the Directed Graph.
        while (running) {
            displayInputState();

            // Check for valid character transitions.
            if (currentChar >= 'A' && currentChar <= 'Z') {
                System.out.println("Upper case letter found");
                foundUpperCase = true;
            } else if (currentChar >= 'a' && currentChar <= 'z') {
                System.out.println("Lower case letter found");
                foundLowerCase = true;
            } else if (currentChar >= '0' && currentChar <= '9') {
                System.out.println("Digit found");
                foundNumericDigit = true;
            }
            // Check for special characters (matching EFSM requirements).
            else if ("~`!@#$%^&*()_-+={}[]|\\:,.?/".indexOf(currentChar) >= 0) {
                System.out.println("Special character found");
                foundSpecialChar = true;
            } else {
                // Invalid character encountered.
                passwordIndexofError = currentCharNdx;
                foundOtherChar = true;
                return "*** Error *** An invalid character has been found!";
            }

            // Check for password length condition (at least 8 characters).
            if (currentCharNdx >= 7) {
                System.out.println("At least 8 characters found");
                foundLongEnough = true;
            }

            // Move to the next character if available.
            currentCharNdx++;
            if (currentCharNdx >= inputLine.length()) {
                running = false;
            } else {
                currentChar = inputLine.charAt(currentCharNdx);
            }

            System.out.println();
        }

        // Build an error message if any conditions are not met.
        String errMessage = "";
        if (!foundUpperCase) {
			errMessage += "Upper case; ";
		}
        if (!foundLowerCase) {
			errMessage += "Lower case; ";
		}
        if (!foundNumericDigit) {
			errMessage += "Numeric digits; ";
		}
        if (!foundSpecialChar) {
			errMessage += "Special character; ";
		}
        if (!foundLongEnough) {
			errMessage += "Long Enough; ";
		}
        if (foundOtherChar) {
			errMessage += "No Other character; ";
		}

        // If all conditions are met, return an empty string.
        if (errMessage.isEmpty()) {
			return "";
		}

        passwordIndexofError = currentCharNdx;
        return errMessage + "conditions were not satisfied";
    }
}

package utility;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

/** Utility class for handling all console input. */
public final class InputService {
	/** The single, static scanner instance for the application. */
	private static final Scanner scanner = new Scanner(System.in);;

	/** Private constructor to prevent instantiation. */
	private InputService() {}
	
	/** Gets the scanner instance. 
	 * @return The scanner. 
	*/
	public static Scanner getScanner() {
		return scanner;
	}
	
	/** Prints a prompt and reads a non-empty string. 
	 * @param prompt The prompt to display. 
	 * @return The user input. 
	*/
	public static String readString(String prompt) {
		System.out.print(prompt);
		return readString();
	}
	
	/** Reads a non-empty string from the console, looping until valid input is received. 
	 * @return The user input. 
	*/
	public static String readString() {
		while(true) {
            String input = scanner.nextLine();
            if (input.trim().isEmpty()) {
                System.out.print("Input cannot be empty. Please try again: ");
                continue;
            }
            return input;
        }
	}
	
	/** Reads an integer from the console, looping until valid input is received. 
	 * @return The user input. 
	*/
	public static int readInt() {
        while(true) {
            String input = scanner.nextLine();
            if (input.trim().isEmpty()) {
                System.out.print("Input cannot be empty. Please enter a number: ");
                continue;
            }
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.print("Invalid input. Please enter a whole number: ");
            }
        }
    }

	/** Prints a prompt and reads an integer. 
	 * @param prompt The prompt to display. 
	 * @return The user input. 
	*/
	public static int readInt(String prompt) {
		System.out.print(prompt);
		return readInt();
	}

	/** Reads an integer within a specific range, looping until valid. 
	 * @param low The inclusive lower bound. 
	 * @param high The inclusive upper bound. 
	 * @return The user input. 
	*/
	public static int readIntRange(int low, int high) {
        while (true) {
            int input = readInt();
            if (input >= low && input <= high) {
                return input;
            } else {
                System.out.printf("Invalid range. Please enter a number between %d and %d: ", low, high);
            }
        }
    }

	/** Prints a prompt and reads an integer within a range. 
	 * @param low The inclusive lower bound. 
	 * @param high The inclusive upper bound. 
	 * @param prompt The prompt to display. 
	 * @return The user input. 
	*/
	public static int readIntRange(int low, int high, String prompt) {
		System.out.print(prompt);
		return readIntRange(low, high);
	}

	/** Prints a prompt and reads a date (dd/MM/yyyy). 
	 * @param prompt The prompt to display. 
	 * @return The parsed LocalDate, or null on failure. 
	*/
	public static LocalDate readDate(String prompt) {
		System.out.println(prompt);
		String input = scanner.nextLine();
		DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		try {
			LocalDate date = LocalDate.parse(input, format);
			return date;
		} catch (DateTimeParseException e) {
			return null;
		}
	}
	
	/** Reads an email address, verifying that it is of the correct format
	 * @return The parsed email address as a String.
	 */
	public static String readEmail() {
    	String email = getScanner().nextLine();
    	while (!InputValidator.nonEmpty(email) || !InputValidator.isValidEmail(email)){
      		System.out.print("Not a valid input. Please retry: ");
      		email = getScanner().nextLine();
    	}
    	return email;
  	}

	/** Closes the static scanner. */
	public static void closeScanner() {
		if (scanner != null) { scanner.close(); }
	}
}

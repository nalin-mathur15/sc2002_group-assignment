package utility;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;
//import utility.InputValidator;

public final class InputService {
	private static final Scanner scanner = new Scanner(System.in);;
	    
	// Prevent instantiation
	private InputService() {}
	
	public static Scanner getScanner() {
		return scanner;
	}
	
	// Convenience methods
	public static String readString(String prompt) {
		System.out.print(prompt);
		return readString();
	}
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

	public static int readInt(String prompt) {
		System.out.print(prompt);
		return readInt();
	}

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

	public static int readIntRange(int low, int high, String prompt) {
		System.out.print(prompt);
		return readIntRange(low, high);
	}

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
	
	// Don't close unless finish program
	public static void closeScanner() {
		if (scanner != null) { scanner.close(); }
	}
}

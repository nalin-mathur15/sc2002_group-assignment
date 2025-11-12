package utility;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;
//import utility.InputValidator;

public class InputService {
	private static final Scanner scanner = new Scanner(System.in);;
	    
	// Prevent instantiation
	private InputService() {}
	
	public static Scanner getScanner() {
		return scanner;
	}
	
	// Convenience methods
	public static String readString(String prompt) {
		System.out.print(prompt);
		String answer = getScanner().nextLine();
		while (!InputValidator.isNonEmptyString(answer)){
			System.out.print("Input cannot be empty. Please retry: ");
			answer = getScanner().nextLine();
		}
		return answer;
	}
	public static String readString() {
		String answer = getScanner().nextLine();
		while (!InputValidator.isNonEmptyString(answer)){
			System.out.print("Input cannot be empty. Please retry: ");
			answer = getScanner().nextLine();
		}
		return answer;
	}
	
	public static int readInt(String prompt) {
		System.out.print(prompt);
		String answer = getScanner().nextLine();
		while (!InputValidator.isValidInteger(answer)){
			System.out.print("Invalid integer. Please retry: ");
			answer = getScanner().nextLine();
		}
		int value = answer.isEmpty() ? 0 : Integer.parseInt(answer);
		return value;
	}

	public static int readIntRange(int low, int high) {
		int val = getScanner().nextInt();
		while (val < low || val > high) {
			System.out.println("[Input Service] Invalid choice. Please retry.");
			val = getScanner().nextInt();
		}
		return val;
	}
	public static int readInt() {
		String answer = getScanner().nextLine();
		while (!InputValidator.isValidInteger(answer)){
			System.out.print("Invalid integer. Please retry: ");
			answer = getScanner().nextLine();
		}
		int value = answer.isEmpty() ? 0 : Integer.parseInt(answer);
		return value;
	}

	public static LocalDate readDate(String prompt) {
		System.out.println(prompt);
		String input = getScanner().nextLine();
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
		scanner.close();
	}
}

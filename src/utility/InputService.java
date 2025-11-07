package utility;
import java.util.Scanner;
import utility.InputValidator;

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
	public static int readInt() {
		String answer = getScanner().nextLine();
		while (!InputValidator.isValidInteger(answer)){
			System.out.print("Invalid integer. Please retry: ");
			answer = getScanner().nextLine();
		}
		int value = answer.isEmpty() ? 0 : Integer.parseInt(answer);
		return value;
	}
	
	// Don't close unless finish program
	public static void closeScanner() {
		scanner.close();
	}
}

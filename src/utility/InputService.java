package utility;
import java.util.Scanner;

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
		return getScanner().nextLine();
	}
	public static String readString() {
		return getScanner().nextLine();
	}
	
	public static int readInt(String prompt) {
		System.out.print(prompt);
		int value = getScanner().nextInt();
		getScanner().nextLine(); 
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
		int value = getScanner().nextInt();
		getScanner().nextLine(); 
		return value;
	}
	
	// Don't close unless finish program
	public static void closeScanner() {
		scanner.close();
	}
}

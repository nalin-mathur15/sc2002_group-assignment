package utility;
import java.util.Scanner;

public class InputService {
	private static Scanner scanner;
	    
	    // Prevent instantiation
	    private InputService() {}
	    
	    public static Scanner getScanner() {
	        if (scanner == null) {
	            scanner = new Scanner(System.in);
	        }
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
	    public static int readInt() {
	        int value = getScanner().nextInt();
	        getScanner().nextLine(); 
	        return value;
	    }
	    
	    // Don't close unless finish program
	    public static void closeScanner() {
	        if (scanner != null) {
	            scanner.close();
	        }
	    }
}

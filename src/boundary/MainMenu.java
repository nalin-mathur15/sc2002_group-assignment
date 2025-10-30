package boundary;
import java.util.Scanner;
import utility.InputService;

public class MainMenu {
	// initial command-line interface
	public static void Start() {
		int choice;
		Scanner sc = new Scanner(System.in);
		do {
			System.out.println("Perform the following methods:");
			System.out.println("1: Login");
			System.out.println("2: Register as Company Representative");
			choice = InputService.readInt();
		
			switch (choice) {
				case 1:
					// call login
					break;
				case 2:
					// call register
					break;
				case 3: 
					System.out.println("Program terminating ….");
				}
		} while (choice < 3 && choice > 0);
		
		sc.close();
	}
}

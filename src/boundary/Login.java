package boundary;
import utility.InputService;
import controller.AuthManager;

public class Login {
    // login sequence for a user
	public void loginInput() {
		System.out.println("Please enter userID: ");
		String userID = InputService.readString();
		System.out.println("Please enter userID: ");
		String pword = InputService.readString();
		// AuthManager.login(userID, pword);
	}
}

package boundary;
import utility.InputService;
import controller.AuthManager;

public class Login {
    // login sequence for a user
	public void loginInput() {
		System.out.println("Enter UserID: ");
		String userID = InputService.readString();
		System.out.println("Enter Password: ");
		String pword = InputService.readString();
		// AuthManager.login(userID, pword);
	}
}

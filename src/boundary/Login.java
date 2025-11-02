package boundary;
import utility.InputService;
import controller.AuthManager;
import java.util.List;
import java.util.ArrayList;

public class Login {
    // login sequence for a user
	private AuthManager authManager;
	public Login(AuthManager authManager) {
		this.authManager = authManager;
	}
	
	public List<String> Start() {
		System.out.println("Enter UserID: ");
		String userID = InputService.readString();
		System.out.println("Enter Password: ");
		String pword = InputService.readString();
		if (authManager.login(userID, pword)) {
			List<String> loginDetails = new ArrayList<>();
			loginDetails.add(authManager.getUser(userID).getRole());
			loginDetails.add(authManager.getUser(userID).getUserID());
			return loginDetails;
		}
		return null;
	}
}

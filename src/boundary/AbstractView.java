package boundary;

import controller.AuthManager;
import entity.User;
import utility.InputService;
import utility.InputValidator;

public abstract class AbstractView {

    protected final AuthManager authManager;
    protected final User loggedInUser;

    public AbstractView(AuthManager authManager, User loggedInUser) {
        this.authManager = authManager;
        this.loggedInUser = loggedInUser;
    }

    public abstract void Menu();

    protected void changePassword() {
        System.out.println("\n--- Change Password ---");
        System.out.print("Please enter your old password: ");
        String oldPword = InputService.readString();

        String newPword;
        do {
            System.out.print("Please enter your new password: ");
            newPword = InputService.readString();
            if (!InputValidator.nonEmpty(newPword)) {
                System.out.println("Password cannot be empty.");
            }
        } while (!InputValidator.nonEmpty(newPword));

        boolean success = authManager.changePassword(loggedInUser, oldPword, newPword);

        if (success) {
            System.out.println("Password changed successfully!");
        } else {
            System.out.println("Failed to change password. Old password may be incorrect.");
        }
    }
}
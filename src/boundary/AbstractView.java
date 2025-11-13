package boundary;

import java.util.Collection;

import controller.AuthManager;
import entity.Internship;
import entity.User;
import utility.InputService;
import utility.InputValidator;

/** Abstract base class for all user-facing (boundary) views. */
public abstract class AbstractView {
    
    /** The authentication controller for managing user logins and passwords. */
    protected final AuthManager authManager;

    /** The User object representing the currently logged-in user. */
    protected final User loggedInUser;

    /** 
     * Constructs a new AbstractView. 
     * @param authManager The authentication manager. 
     * @param loggedInUser The user who is logged in. 
    */
    public AbstractView(AuthManager authManager, User loggedInUser) {
        this.authManager = authManager;
        this.loggedInUser = loggedInUser;
    }

    /** Abstract method to display the main menu for the specific view. */
    public abstract void Menu();

    /** Handles the user interface logic for changing the logged-in user's password. */
    protected void changePassword() {
        System.out.println("\n--- Change Password ---");
        System.out.print("Please enter your old password: ");
        String oldPword = InputService.readString();

        String newPword;
        do {
            System.out.print("Please enter your new password: ");
            newPword = InputService.readString();
        } while (!InputValidator.nonEmpty(newPword));

        boolean success = authManager.changePassword(loggedInUser, oldPword, newPword);

        if (success) {
            System.out.println("Password changed successfully!");
        } else {
            System.out.println("Failed to change password. Old password may be incorrect.");
        }
    }

    /** Handles the task of displaying a given list of internships. 
     * @param internships The Collection of internships to display.
    */
    protected void displayInternships(Collection<Internship> internships) {
        if (internships == null || internships.isEmpty()) {
            System.out.println("No internships found matching this filter.");
            return;
        }
        int i = 1;
        System.out.println("--------------------------------------------------------------------------------------------------");
        System.out.printf("%-3s | %-25s | %-20s | %-12s | %-12s | %-10s\n", 
            "#", "Title", "Company", "Status", "Level", "Major");
        System.out.println("--------------------------------------------------------------------------------------------------");
        for (Internship intern : internships) {
            System.out.printf("%-3d | %-25s | %-20s | %-12s | %-12s | %-10s\n",
                    i++,
                    intern.getTitle(),
                    intern.getCompanyName(),
                    intern.getStatus(),
                    intern.getLevel(),
                    intern.getPreferredMajor());
        }
        System.out.println("--------------------------------------------------------------------------------------------------");
    }
}
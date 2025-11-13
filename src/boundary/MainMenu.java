package boundary;

import utility.InputService;
import utility.InputValidator;
import controller.*;
import entity.*;

public class MainMenu {
	// initial command-line interface
	private ApplicationManager applicationManager;
	private ApprovalManager approvalManager;
	private AuthManager authManager;
	private InternshipManager internshipManager;

	public MainMenu(ApplicationManager applicationManager, ApprovalManager approvalManager, 
			AuthManager authManager, InternshipManager internshipManager) {
		this.applicationManager = applicationManager;
		this.approvalManager = approvalManager;
		this.authManager = authManager;
		this.internshipManager = internshipManager;
	}
	
	public void start() {
		int choice;
		do {
			System.out.println("\n--- Welcome to the Internship Portal ---");
			System.out.println("1: Login");
			System.out.println("2: Register as Company Representative");
			System.out.println("3: Exit");
			choice = InputService.readInt();
		
			switch (choice) {
				case 1 -> handleLogin();
				case 2 -> handleRegistration();
				case 3 -> System.out.println("Program terminating... ");
				default -> System.out.println("Invalid choice. Please try again. ");
			}
		} while (choice != 3);
	}

	private void handleLogin() {
		System.out.println("\n--- Login ---");
        System.out.print("Enter UserID: ");
        String userID = InputService.readString();
        System.out.print("Enter Password: ");
        String password = InputService.readString();

        User user = authManager.login(userID, password);

        if (user == null) {
            System.out.println("Login failed. Invalid UserID or Password.");
            return;
        }
        System.out.println("Login successful! Welcome, " + user.getName());

        if (user instanceof Student) {
            StudentView studentView = new StudentView(applicationManager, authManager, internshipManager, (Student) user);
            studentView.Menu();

        } else if (user instanceof Staff) {
            StaffView staffView = new StaffView(applicationManager, approvalManager, authManager, internshipManager, (Staff) user);
            staffView.Menu();

        } else if (user instanceof CompanyRepresentative) {
            CompanyRepresentative rep = (CompanyRepresentative) user;
            if (!rep.approvedRepresentative()) {
                System.out.println("Your account is pending approval by a Staff member. Please try again later.");
                return;
            }
            CompanyView companyView = new CompanyView(applicationManager, authManager, internshipManager, rep);
            companyView.Menu();
        }
	}

	private void handleRegistration() {
        System.out.println("\n--- Company Representative Registration ---");
        System.out.print("Enter your Email: ");
        String email = InputService.readString();
        if (!InputValidator.isValidEmail(email)) {
            System.out.println("Invalid email format.");
            return;
        }
        if (authManager.userExists(email)) {
            System.out.println("Error: A user with this email already exists.");
            return;
        }

        System.out.print("Enter your Full Name: ");
        String name = InputService.readString();
        System.out.print("Enter your Company Name: ");
        String companyName = InputService.readString();
        System.out.print("Enter your Department: ");
        String department = InputService.readString();
        System.out.print("Enter your Position: ");
        String position = InputService.readString();

        if (!InputValidator.nonEmpty(name) || !InputValidator.nonEmpty(companyName) ||
            !InputValidator.nonEmpty(department) || !InputValidator.nonEmpty(position)) {
            System.out.println("Registration failed. All fields must be filled.");
            return;
        }
        
        CompanyRepresentative newRep = authManager.registerRepresentative(
            email, name, email, companyName, department, position
		);
        
        if (newRep != null) {
            System.out.println("Registration successful!");
            System.out.println("Your account is now pending approval from a Staff member.");
        } else {
            System.out.println("An unexpected error occurred during registration.");
        }
    }
}
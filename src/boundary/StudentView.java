package boundary;

import utility.InputService;
import controller.ApplicationManager;
import controller.InternshipManager;
import controller.AuthManager;
import entity.Student;
import entity.Internship;
import java.util.List;

public class StudentView {
    // menu and options for a student user after logging in
	private ApplicationManager applicationManager;
	private InternshipManager internshipManager;
	private AuthManager authManager;
	private Student s;
	public StudentView(ApplicationManager applicationManager, AuthManager authManager,
			InternshipManager internshipManager, Student s) {
		this.applicationManager = applicationManager;
		this.internshipManager = internshipManager;
		this.authManager = authManager;
		this.s = s;
	}
	
	public void Menu() {
		int choice;
		do {
			System.out.println("Perform the following methods:");
			System.out.println("1: View Internships Created/Applied");
			System.out.println("2: Change Password");
			System.out.println("3: Logout");
			choice = InputService.readInt();
		
			switch (choice) {
				case 1 -> ViewInternship();
				case 2 -> ChangePassword();
				default -> System.out.println("Logging out ...");
				}
		} while (choice < 3 && choice > 0);
	}
	
	public void ViewInternship() {
		List<Internship> listInternship = internshipManager.getInternshipsForStudent(s);
		if (listInternship.isEmpty()) {
			System.out.println("No internships available to view.");
		}
		else {
			for (int i=0; i < listInternship.size(); i++) {
				System.out.println(i+1 + ". " + listInternship.get(i).getTitle()); // not sure what format to print the internships out yet
			}
			PerformAction(listInternship);
		}
	}
	
	public void PerformAction(List<Internship> listInternship) {
		System.out.println("Select an Internship to apply to or withdraw from.");
		System.out.println("Otherwise, enter '0' to go back to menu.");
		System.out.println("** Reminder that you can only apply for a maximum of 3 Internships. **");
		int choice = InputService.readInt();
		while (choice < 0 && choice > listInternship.size()) {
			System.out.println("Invalid input");
			choice = InputService.readInt();
		}
		
		if (choice == 0) {
			System.out.println("Back to menu ...");
		}
		else {
			Internship intern = listInternship.get(choice-1);
			String internTitle = intern.getTitle();
			if (s.getSubmittedApplicationIDs().contains(intern)) {
				System.out.printf("Do you wish to request withdrawal from %s? \n1. Yes \n2. No", internTitle);
				choice = InputService.readInt();
				if (choice == 1) {
					// request withdrawal method
					System.out.printf("** Successfully requested withdrawal from %s! **", internTitle);
				}
			}
			else {
				System.out.printf("Do you wish to apply for %s? \n1. Yes \n2. No", internTitle);
				if (choice == 1) {
					applicationManager.submitApplication(s, intern);
					System.out.printf("** Successfully applied for %s!", internTitle);
				}
			}
		}
		
		ViewInternship();
	}
	
	public void ChangePassword() {
		System.out.println("Please enter old password: ");
		String oldPword = InputService.readString();
		System.out.println("Please enter new password: ");
		String newPword = InputService.readString();
		authManager.changePassword(s, oldPword, newPword);
		Menu();
	}
}

package boundary;

import utility.InputService;
import controller.ApplicationManager;
import controller.InternshipManager;
import controller.AuthManager;
import entity.Student;
import entity.Internship;
import entity.InternshipApplication;
import entity.InternshipApplication.ApplicationStatus;

import java.util.List;

public class StudentView extends AbstractView {
    // menu and options for a student user after logging in
	private ApplicationManager applicationManager;
	private InternshipManager internshipManager;
	private Student s;
	public StudentView(ApplicationManager applicationManager, AuthManager authManager,
			InternshipManager internshipManager, Student s) {
		super(authManager, s);
		this.applicationManager = applicationManager;
		this.internshipManager = internshipManager;
		this.s = s;
	}
	
	@Override
	public void Menu() {
		int choice;
		do {
			System.out.println("\n ------ Student Menu ------");
			System.out.println("Currently logged in as: " + s.getName());
			System.out.println("1: View All Internships Available");
			System.out.println("2: View My Internship Applications");
			System.out.println("3: Change Password");
			System.out.println("4: Logout");
			choice = InputService.readInt();
		
			switch (choice) {
				case 1 -> ViewInternship();
				case 2 -> ViewApplications();
				case 3 -> changePassword();
				case 4 -> System.out.println("Logging out...");
				default -> System.out.println("Invalid choice. Please try again. ");
				}
		} while (choice != 4);
	}
	
	private void ViewInternship() {
		List<Internship> listInternship = internshipManager.getInternshipsForStudent(s);
		if (listInternship.isEmpty()) {
			System.out.println("No internships available to view.");
		}
		else {
			System.out.println("\n--- Internships ---");
			for (int i = 0; i < listInternship.size(); i++) {
				System.out.printf("%d. \nCompany: %-s \nTitle: %-s \nDescription %s \nOpen Date: %-s \nClose Date: %-s \nLevel: %-s \nPreferred Major: %-s \nAvailable Slots: %d\n\n",
                    (i + 1), listInternship.get(i).getCompanyName(), listInternship.get(i).getTitle(), listInternship.get(i).getDescription(),
					listInternship.get(i).getApplicationOpenDate(), listInternship.get(i).getApplicationCloseDate(), listInternship.get(i).getLevel(),
					listInternship.get(i).getPreferredMajor(), listInternship.get(i).getNumberOfSlotsAvailable(), listInternship.get(i).getNumberOfSlots(),
					listInternship.get(i).getNumberOfSlotsAvailable());
			}
			ApplyInternship(listInternship);
		}
	}
	
	private void ApplyInternship(List<Internship> listInternship) {
		System.out.println("Select an Internship to apply for.");
		System.out.println("Otherwise, enter '0' to go back to menu.");
		System.out.printf("** Reminder that you can only apply for a maximum of 3 Internships. Current: %d **\n", s.getSubmittedApplicationIDs().size());
		int choice = InputService.readInt();
		while (choice < 0 || choice > listInternship.size()) {
			System.out.println("Invalid input");
			choice = InputService.readInt();
		}
		
		if (choice == 0) {
			System.out.println("Back to menu ...");
		}
		else {
			Internship intern = listInternship.get(choice-1);
			String internTitle = intern.getTitle();
			if (s.getSubmittedApplicationIDs().contains(intern.getInternshipID())) {
				System.out.println("You have already applied for this internship.");
			}
			else {
				System.out.printf("Do you wish to apply for %s? \n1. Yes \n2. No", internTitle);
				if (choice == 1) {
					applicationManager.submitApplication(s, intern);
				}
			}
		}
	}

	private void ViewApplications() {
		List<InternshipApplication> listApplications = applicationManager.getApplicationsByStudent(s.getUserID());
		if (listApplications.isEmpty()) {
			System.out.println("No applications to view.");
		}
		else {
			System.out.println("\n--- My Internship Applications ---");
			for (int i = 0; i < listApplications.size(); i++) {
				InternshipApplication app = listApplications.get(i);
				System.out.printf("%d. Internship Title: %-s | Company Name: %-s | Application Status: %-s\n", (i + 1),
					internshipManager.getInternshipById(app.getInternshipID()).getTitle(),
					internshipManager.getInternshipById(app.getInternshipID()).getCompanyName(),
					app.getStatus());
			}
			ApplicationActions(listApplications);
		}
	}

	private void ApplicationActions(List<InternshipApplication> listApplications) {
		System.out.println("Select an Application to accept or withdraw.");
		System.out.println("Otherwise, enter '0' to go back to menu.");
		int choice = InputService.readInt();
		while (choice < 0 || choice > applicationManager.getApplicationsByStudent(s.getUserID()).size()) {
			System.out.println("Invalid input");
			choice = InputService.readInt();
		}
		
		if (choice == 0) {
			System.out.println("Back to menu ...");
		}
		else {
			InternshipApplication app = listApplications.get(choice-1);
			Internship intern = internshipManager.getInternshipById(app.getInternshipID());
			
			System.out.printf("%s: \n0. Go back \n1. Accept \n2. Withdraw", intern.getTitle());
			choice = InputService.readInt();
			if (choice == 1) {
				if (app.getStatus() == ApplicationStatus.SUCCESSFUL) {
					applicationManager.updateApplicationStatus(app.getApplicationID(), ApplicationStatus.ACCEPTED);
					System.out.printf("** Successfully accepted %s! **", intern.getTitle());
				}
				else {
					System.out.println("Application is still pending.");
				}
			}
			else if (choice == 2) {
				applicationManager.updateApplicationStatus(app.getApplicationID(), ApplicationStatus.PENDING_WITHDRAWAL);
				System.out.printf("** Successfully applied for withdrawal for %s! **", intern.getTitle());
				
			}
			else { System.out.println("Returning to menu ..."); }
		}
	}
}

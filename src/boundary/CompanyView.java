package boundary;

import utility.InputService;
import java.util.List;
import java.util.Map;

import controller.*;
import entity.*;
import entity.Internship.InternshipLevel;
import entity.InternshipApplication.ApplicationStatus;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class CompanyView {
    // menu and options for company rep after logging in
	private ApplicationManager applicationManager;
	private AuthManager authManager;
	private InternshipManager internshipManager;
	private CompanyRepresentative cr;
	private Map<String, Student> allStudents;
	public CompanyView(ApplicationManager applicationManager, AuthManager authManager, 
			InternshipManager internshipManager, CompanyRepresentative cr) {
		this.applicationManager = applicationManager;
		this.authManager = authManager;
		this.internshipManager = internshipManager;
		this.cr = cr;
	}
	
	public void Menu() {
		int choice;
		do {
			System.out.println("\n ------ Company Menu ------");
			System.out.println("Currently logged in as: " + cr.getName());
			System.out.println("1: Create Internship (max 5)");
			System.out.println("2: View Internships Created");
			System.out.println("3: Change Password");
			System.out.println("4: Logout");
			choice = InputService.readInt();
		
			switch (choice) {
				case 1 -> CreateInternship();
				case 2 -> ViewInternshipsCreated();
				case 3 -> ChangePassword();
				case 4 -> System.out.println("Logging out ...");
				default -> System.out.println("Invalid choice. Please try again. ");
				}
		} while (choice != 4);
	}
	
	public void CreateInternship() {
		if (cr.getPostedInternshipIDs().size() < 5) {
			String internID = cr.getCompanyName()+cr.getUserID()+InternshipManager.UseInternIDGen(); // any idea how to make this better
			
			System.out.println("Enter Internship Title: ");
			String internTitle = InputService.readString();
			
			System.out.println("Enter Description: ");
			String internDescription = InputService.readString();
			
			System.out.println("Enter Internship Level: \n1. Basic \n2. Intermediate \n3. Advanced");
			int choice;
			do {
				choice = InputService.readInt();
			} while (choice < 1 && choice > 3);
			InternshipLevel internLevel = switch (choice) {
			case 1 -> InternshipLevel.BASIC;
			case 2 -> InternshipLevel.INTERMEDIATE;
			case 3 -> InternshipLevel.ADVANCED;
			default -> null;
			};
			
			System.out.println("Enter Preferred Major: ");
			String internMajor = InputService.readString();
			
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			LocalDate internOpenDate = null;
			while (internOpenDate == null) {
	            System.out.print("Enter opening date (dd/MM/yyyy): ");
	            String dateOpen = InputService.readString().trim();
	            
	            try {
	            	internOpenDate = LocalDate.parse(dateOpen, formatter);
	            } catch (DateTimeParseException e) {
	                System.out.println("✗ Invalid date format! Please use dd/MM/yyyy (e.g., 24/10/2025)");
	            }
	        }
			
			LocalDate internCloseDate = null;
			while (internCloseDate == null) {
	            System.out.print("Enter closing date (dd/MM/yyyy): ");
	            String dateClose = InputService.readString().trim();
	            
	            try {
	            	internCloseDate = LocalDate.parse(dateClose, formatter);
	            	if (internCloseDate.isBefore(internOpenDate)) {
	            		System.out.println("Please input a later date than the opening: " + internOpenDate);
	            	}
	            } catch (DateTimeParseException e) {
	                System.out.println("✗ Invalid date format! Please use dd/MM/yyyy (e.g., 24/10/2025)");
	            }
	        }
			
			String compName = cr.getCompanyName();
			String compRepID = cr.getUserID();
			
			System.out.println("Enter Number of Slots (max 10): ");
			int internSlots = InputService.readInt();
			Internship newIntern = new Internship(internID, internTitle, internDescription, internLevel, internMajor, 
					internOpenDate, internCloseDate, compName, compRepID, internSlots);
			internshipManager.addInternship(newIntern);
		}
		else {
			System.out.println("Maximum number of internships created!");
		}
		Menu();
	}
	
	public void ViewInternshipsCreated() {
		List<Internship> listIntern = internshipManager.getInternshipsByCompany(cr.getCompanyName());
		for (Internship intern : listIntern) {
			System.out.println(intern.getTitle());
		}
		PerformAction(listIntern);
	}
	
	public void PerformAction(List<Internship> listIntern) {
		System.out.println("Select an Internship to view applications.");
		System.out.println("Otherwise, enter '0' to go back to menu.");
		int choice = InputService.readInt();
		while (choice < 0 && choice > listIntern.size()) {
			System.out.println("Invalid input");
			choice = InputService.readInt();
		}
		
		if (choice == 0) {
			System.out.println("Back to menu ...");
		}
		else {
			Internship intern = listIntern.get(choice-1);
			ViewApplicationsForInternship(intern);
		}
		Menu();
	}
	
	public void ViewApplicationsForInternship(Internship intern) {
		List<InternshipApplication> listApp = applicationManager.getApplicationsForInternship(intern.getInternshipID());
		if (listApp.isEmpty()) {
			System.out.println("No applications so far.");
			System.out.println("Going back to view Internships created.");
		}
		else {
			for (int i=0; i<listApp.size(); i++) {
				InternshipApplication intApp = listApp.get(i);
				Student s = allStudents.get(intApp.getStudentID());
				System.out.printf(i+1 + ". Name: " + s.getName() + " Major: " + s.getMajor() + " Year: " + s.getYear());
			}
			
			System.out.println("Select an Application to accept or reject.");
			System.out.println("Otherwise, enter '0' to go back to view internships created.");
			int choice = InputService.readInt();
			while (choice < 0 && choice > listApp.size()) {
				System.out.println("Invalid input");
				choice = InputService.readInt();
			}
			
			if (choice == 0) {
				System.out.println("Back to view internships created ...");
			}
			else {
				InternshipApplication intApp = listApp.get(choice-1);
				Student s = allStudents.get(intApp.getStudentID());
				System.out.printf("Student: %s \n1. Accept \n2. Reject", s.getName());
				choice = InputService.readInt();
				if (choice == 1) {
					applicationManager.updateApplicationStatus(intApp.getApplicationID(), ApplicationStatus.SUCCESSFUL);
					System.out.printf("** Successfully accepted %s! **", s.getName());
				}
				else {
					applicationManager.updateApplicationStatus(intApp.getApplicationID(), ApplicationStatus.UNSUCCESSFUL);
					System.out.printf("** Successfully rejected %s! **", s.getName());
				}
				
			}
		}
		ViewInternshipsCreated();
	}
	
	public void ChangePassword() {
		System.out.println("Please enter old password: ");
		String oldPword = InputService.readString();
		System.out.println("Please enter new password: ");
		String newPword = InputService.readString();
		authManager.changePassword(cr, oldPword, newPword);
		Menu();
	}

	public void Logout() {
		authManager.logout(cr);
	}	
}

package boundary;

import utility.InputService;
import java.util.List;

import controller.*;
import entity.*;
import entity.Internship.InternshipLevel;
import entity.InternshipApplication.ApplicationStatus;
import java.time.LocalDate;

public class CompanyView extends AbstractView {
    // menu and options for company rep after logging in
	private ApplicationManager applicationManager;
	private InternshipManager internshipManager;
	private CompanyRepresentative cr;

	public CompanyView(ApplicationManager applicationManager, AuthManager authManager, 
			InternshipManager internshipManager, CompanyRepresentative cr) {
		super(authManager, cr);
		this.applicationManager = applicationManager;
		this.internshipManager = internshipManager;
		this.cr = cr;
	}
	
	@Override
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
				case 1 -> createInternship();
				case 2 -> ViewInternshipsCreated();
				case 3 -> changePassword();
				case 4 -> System.out.println("Logging out ...");
				default -> System.out.println("Invalid choice. Please try again. ");
				}
		} while (choice != 4);
	}
	
	private void createInternship() {
        if (cr.getPostedInternshipIDs().size() >= 5) {
            System.out.println("Error: You have already posted the maximum of 5 internships.");
            return;
        }

        System.out.println("\n--- Create New Internship ---");
        System.out.print("Enter Internship Title: ");
        String internTitle = InputService.readString();
        
        System.out.print("Enter Description: ");
        String internDescription = InputService.readString();
        
        System.out.println("Enter Internship Level: \n1. Basic \n2. Intermediate \n3. Advanced");
        System.out.print("Enter choice: ");
        int levelChoice = InputService.readIntRange(1, 3);
        InternshipLevel internLevel = switch (levelChoice) {
            case 1 -> InternshipLevel.BASIC;
            case 2 -> InternshipLevel.INTERMEDIATE;
            case 3 -> InternshipLevel.ADVANCED;
            default -> InternshipLevel.BASIC;
        };
        
        System.out.print("Enter Preferred Major: ");
        String internMajor = InputService.readString();
        LocalDate internOpenDate = null;
        do {
			internOpenDate = InputService.readDate("Enter opening date (dd/MM/yyyy): ");
		} while(internOpenDate != null);

		LocalDate internCloseDate = null;
        do {
            internCloseDate = InputService.readDate("Enter closing date (dd/MM/yyyy): ");
            if (internCloseDate.isBefore(internOpenDate)) {
                System.out.println("Closing date must be on or after the opening date (" + internOpenDate + ")");
            }
        } while (internCloseDate.isBefore(internOpenDate));
        
        System.out.print("Enter Number of Slots (max 10): ");
        int internSlots = InputService.readIntRange(1, 10);
        
        internshipManager.createInternship(
            internTitle, internDescription, internLevel, internMajor, 
            internOpenDate, internCloseDate, cr, internSlots
        );
    }
	
	public void ViewInternshipsCreated() {
		System.out.println("\n ------ Internships Created ------");
		List<Internship> listInternship = internshipManager.getInternshipsByCompany(cr.getCompanyName());
		for (int i = 0; i < listInternship.size(); i++) {
			System.out.printf("%d. \n", (i + 1));
			listInternship.get(i).toStringDetailed();
			System.out.println();
		}
		PerformAction(listInternship);
	}
	
	public void PerformAction(List<Internship> listIntern) {
		System.out.println("Select an Internship to view applications.");
		System.out.println("Otherwise, enter '0' to go back to menu.");
		int choice = InputService.readInt();
		while (choice < 0 || choice > listIntern.size()) {
			System.out.println("Invalid input");
			choice = InputService.readInt();
		}
		
		if (choice == 0) {
			System.out.println("Back to menu ...");
		}
		else {
			Internship intern = listIntern.get(choice-1);
			viewApplicationsForInternship(intern);
		}
		return;
	}
	
	private void viewApplicationsForInternship(Internship intern) {
        List<InternshipApplication> listApp = applicationManager.getApplicationsForInternship(intern.getInternshipID());
        
        if (listApp.isEmpty()) {
            System.out.println("No applications for this internship yet.");
            return;
        }

        System.out.println("\n--- Student Applications for: " + intern.getTitle() + " ---");
        for (int i = 0; i < listApp.size(); i++) {
            InternshipApplication intApp = listApp.get(i);
            Student s = applicationManager.getStudentbyID(intApp.getStudentID());
            
            if (s != null) {
                System.out.printf("%d. Name: %-20s | Major: %-10s | Year: %d | Status: %s\n",
                    (i + 1),
                    s.getName(),
                    s.getMajor(),
                    s.getYear(),
                    intApp.getStatus()
                );
            }
        }
        handleApplicationApproval(listApp);
    }
    
    private void handleApplicationApproval(List<InternshipApplication> listApp) {
        System.out.println("\nSelect an Application to approve or reject (1-" + listApp.size() + ").");
        System.out.println("Otherwise, enter '0' to go back.");
        System.out.print("Enter choice: ");
        
        int choice = InputService.readIntRange(0, listApp.size());
        
        if (choice == 0) {
            System.out.println("Back to internships list ...");
            return;
        }

        InternshipApplication intApp = listApp.get(choice - 1);
        Student s = applicationManager.getStudentbyID(intApp.getStudentID());
        
        if (intApp.getStatus() != ApplicationStatus.PENDING) {
            System.out.println("This application is not pending (Status: " + intApp.getStatus() + "). No action can be taken.");
            return;
        }

        System.out.printf("Action for student %s: \n1. Approve \n2. Reject \n0. Cancel\n", s.getName());
        System.out.print("Enter choice: ");
        int action = InputService.readIntRange(0, 2);
        
        switch (action) {
            case 1:
                // FIXED: Calling the correct manager method
                applicationManager.updateApplicationStatus(intApp.getApplicationID(), ApplicationStatus.SUCCESSFUL);
                break;
            case 2:
                // FIXED: Calling the correct manager method
                applicationManager.updateApplicationStatus(intApp.getApplicationID(), ApplicationStatus.UNSUCCESSFUL);
                break;
            default:
                System.out.println("Action cancelled.");
                break;
        }
        // FIXED: No recursive call
    }
	
}
